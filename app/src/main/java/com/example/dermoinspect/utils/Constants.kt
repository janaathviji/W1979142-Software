// Janaath Vijithavarnan
// W1979142

package com.example.dermoinspect.utils


object Constants {

    // This is the firebase Collections
    const val COLLECTION_USERS = "users"



    // This is the validation
    const val MIN_PASSWORD_LENGTH = 6

}


// This is to check that the email is valid
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// This is to check that the email is valid
fun String.isValidPassword(): Boolean {
    return this.length >= Constants.MIN_PASSWORD_LENGTH
}