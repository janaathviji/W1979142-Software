// Janaath Vijithavarnan
// W1979142

// This file handles the machine learning classification for identifying skin diseases from images.
package com.example.dermoinspect.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class DiseaseResult(
    val diseaseName: String,
    val confidence: Float
)

// This class manages the full ML pipeline:
// loading the TensorFlow Lite model, preprocessing images,
// running inference, and returning classification results
class SkinDiseaseClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()

    companion object {
        private const val MODEL_PATH = "skin_disease_model.tflite"
        private const val LABELS_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
        private const val TAG = "SkinDiseaseClassifier"

        // Image quality thresholds. The images outside of these ranges are
        // rejected before reaching the model
        private const val MIN_BRIGHTNESS = 20f       // Below this = too dark
        private const val MAX_BRIGHTNESS = 240f      // Above this = too bright
        private const val MIN_TEXTURE_STD = 8f       // Below this = no texture
    }

    // It loads a TensorFlow Lite model and disease labels when initialized.
    // The classifyImage function takes an image, resizes it to 224x224 pixels,
    // converts it to the correct format for the model, runs the prediction,
    init {
        loadModel()
        loadLabels()
    }

    // Here it loads the TensorFlow Lite model into memory and prepares the interpreter for inference
    private fun loadModel() {
        try {
            val modelBuffer = FileUtil.loadMappedFile(context, MODEL_PATH)
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)

        } catch (e: Exception) {
            Log.e(TAG, "Error loading model: ${e.message}")  // Error logs help debug issues with model loading or predictions
            e.printStackTrace()
        }
    }

    private fun loadLabels() {
        try {
            labels = FileUtil.loadLabels(context, LABELS_PATH)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading labels: ${e.message}")   // Error logs help debug issues with model loading or predictions
            e.printStackTrace()
        }
    }

    // Checks image brightness and texture before classification
    // Returns a rejection reason string if the image fails quality checks, or null if the image is acceptable
    private fun getImageQualityIssue(bitmap: Bitmap): String? {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var totalBrightness = 0.0
        val brightnessValues = FloatArray(pixels.size)

        // This calculate average brightness across all pixels using perceived luminance weights
        for (i in pixels.indices) {
            val r = ((pixels[i] shr 16) and 0xFF).toFloat()
            val g = ((pixels[i] shr 8) and 0xFF).toFloat()
            val b = (pixels[i] and 0xFF).toFloat()
            val brightness = 0.299f * r + 0.587f * g + 0.114f * b  // Standard luminance formula
            brightnessValues[i] = brightness
            totalBrightness += brightness
        }

        val avgBrightness = (totalBrightness / pixels.size).toFloat()

        // It Rejects images that are too dark
        if (avgBrightness < MIN_BRIGHTNESS) {
            Log.w(TAG, "Image rejected: too dark (brightness=$avgBrightness)")
            return "Image is too dark. Please ensure good lighting."
        }

        // It reject images that are too bright
        if (avgBrightness > MAX_BRIGHTNESS) {
            Log.w(TAG, "Image rejected: too bright (brightness=$avgBrightness)")
            return "Image is too bright or overexposed. Avoid direct light."
        }

        // Calculate standard deviation of brightness
        // When the std is low it means the image is a flat
        var varianceSum = 0.0
        for (b in brightnessValues) {
            varianceSum += (b - avgBrightness) * (b - avgBrightness)
        }
        val stdDev = Math.sqrt(varianceSum / pixels.size).toFloat()

        // This is where it rejects images with no texture
        if (stdDev < MIN_TEXTURE_STD) {
            Log.w(TAG, "Image rejected: no texture (stdDev=$stdDev)")
            return "No detail detected. Please point the camera at a skin lesion."
        }

        // The image passed all quality checks which safe to send to the model
        return null
    }

    // Runs the full classification pipeline:
    // Validates image quality
    // Preprocesses image
    // Runs ML inference
    // Returns top predictions

    fun classifyImage(inputBitmap: Bitmap): List<DiseaseResult> {
        if (interpreter == null || labels.isEmpty()) {
            Log.e(TAG, "Model or labels not loaded") // Error logs help debug issues with model loading or predictions
            return emptyList()
        }

        try {
            // This is the software bitmap
            val softwareBitmap = inputBitmap.copy(Bitmap.Config.ARGB_8888, false)

            // Run image quality checks before resizing or sending to model
            // If the image fails, return a clear rejection result immediately without running inference
            val qualityIssue = getImageQualityIssue(softwareBitmap)
            if (qualityIssue != null) {
                return listOf(DiseaseResult(diseaseName = qualityIssue, confidence = 0f))
            }

            val resizedBitmap = Bitmap.createScaledBitmap(
                softwareBitmap,
                INPUT_SIZE,
                INPUT_SIZE,
                true
            )

            val inputBuffer = bitmapToByteBuffer(resizedBitmap)

            val outputShape = interpreter!!.getOutputTensor(0).shape()
            val outputBuffer = TensorBuffer.createFixedSize(
                outputShape,
                org.tensorflow.lite.DataType.FLOAT32
            )

            interpreter!!.run(inputBuffer, outputBuffer.buffer)

            val probabilities = outputBuffer.floatArray

            // DiseaseResult stores each prediction's disease name and confidence percentage
            val results = mutableListOf<DiseaseResult>()

            for (i in probabilities.indices) {
                if (i < labels.size) {
                    results.add(
                        DiseaseResult(
                            diseaseName = labels[i],
                            confidence = probabilities[i]
                        )
                    )
                }
            }

            // It returns the top 3 disease results with confidence scores
            val topResults = results.sortedByDescending { it.confidence }.take(3)

            return topResults

        } catch (e: Exception) {
            Log.e(TAG, "Error during classification: ${e.message}") // This is the logs
            // This is to help debug issues with model loading or predictions
            e.printStackTrace()
            return emptyList()
        }
    }

    // Converts bitmap image into normalized ByteBuffer format required by the ML model
    // This the bitmapToByteBuffer function converts image pixels
    // into normalized RGB values that the model can process

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(
            pixels,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )

        for (pixel in pixels) {
            val red = ((pixel shr 16) and 0xFF) / 255.0f
            val green = ((pixel shr 8) and 0xFF) / 255.0f
            val blue = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(red)
            byteBuffer.putFloat(green)
            byteBuffer.putFloat(blue)
        }

        return byteBuffer
    }
    // Here it releases ML model resources to prevent memory leaks
    fun close() {
        interpreter?.close()
        interpreter = null
    }
}


