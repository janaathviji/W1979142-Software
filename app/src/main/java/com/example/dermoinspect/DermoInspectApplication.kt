// Janaath Vijithavarnan
// W1979142
package com.example.dermoinspect

import android.app.Application
import com.google.firebase.FirebaseApp


// This is the Application class for DermoInspect
// It initialises firebase here
class DermoInspectApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // This is to initialize Firebase
        FirebaseApp.initializeApp(this)
    }

    companion object {
        const val TAG = "DermoInspect"
    }
}