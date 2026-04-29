// Janaath Vijithavarnan
// W1979142

//This file contains data models for storing detailed information about skin diseases.

package com.example.dermoinspect.data.model



// The main model is DiseaseInformation, which holds complete disease data including the name,
// symptoms, causes, treatments, and frequently asked questions.
data class DiseaseInformation(
    val name: String,
    val shortName: String,
    val isMalignant: Boolean,
    val category: DiseaseCategory,
    val shortDescription: String,
    val overview: String,
    val symptoms: List<Symptom>,
    val causes: List<String>,
    val riskFactors: List<String>,
    val prevention: List<String>,
    val treatment: List<String>,
    val whenToSeeDoctor: List<String>,
    val prognosis: String,
    val faqs: List<FAQ>
)

// The DiseaseCategory enum classifies diseases as either malignant, benign, or precancerous.
// Supporting models include Symptom for representing individual symptoms with a title and description
enum class DiseaseCategory {
    MALIGNANT, BENIGN, PRECANCEROUS
}

data class Symptom(
    val title: String,
    val description: String,
    val icon: String = "•"
)

// FAQ for storing question answer pairs.
// These models are used throughout the app to display disease
// information and educate users about various skin conditions.
data class FAQ(
    val question: String,
    val answer: String
)
