// Janaath Vijithavarnan
// W1979142


package com.example.dermoinspect.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermoinspect.data.model.User
import com.example.dermoinspect.data.model.ViewState
import com.example.dermoinspect.data.repository.AuthRepository
import com.example.dermoinspect.utils.isValidEmail
import com.example.dermoinspect.utils.isValidPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// This is the viewmodel for authentication
class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // This is the state flows
    private val _loginState = MutableStateFlow<ViewState<User>>(ViewState.Idle)
    val loginState: StateFlow<ViewState<User>> = _loginState

    private val _signUpState = MutableStateFlow<ViewState<User>>(ViewState.Idle)
    val signUpState: StateFlow<ViewState<User>> = _signUpState


    // This is the login
    fun login(email: String, password: String) {
        // This is the Validation
        if (email.isBlank()) {
            _loginState.value = ViewState.Error("Email is required")
            return
        }

        if (!email.isValidEmail()) {
            _loginState.value = ViewState.Error("Please enter a valid email")
            return
        }

        if (password.isBlank()) {
            _loginState.value = ViewState.Error("Password is required")
            return
        }

        if (!password.isValidPassword()) {
            _loginState.value = ViewState.Error("Password must be at least 6 characters")
            return
        }

        // This is to perform login
        _loginState.value = ViewState.Loading

        viewModelScope.launch {
            val result = authRepository.login(email, password)

            result.fold(
                onSuccess = { user ->
                    _loginState.value = ViewState.Success(user)
                },
                onFailure = { exception ->
                    _loginState.value = ViewState.Error(
                        exception.message ?: "Login failed. Please try again."
                    )
                }
            )
        }
    }


    // This is the sign up process
    fun signUp(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String
    ) {
        // This is the Validation
        if (firstName.isBlank()) {
            _signUpState.value = ViewState.Error("First name is required")
            return
        }
        if (lastName.isBlank()) {
            _signUpState.value = ViewState.Error("Last name is required")
            return
        }
        if (email.isBlank()) {
            _signUpState.value = ViewState.Error("Email is required")
            return
        }
        if (!email.isValidEmail()) {
            _signUpState.value = ViewState.Error("Please enter a valid email")
            return
        }
        if (password.isBlank()) {
            _signUpState.value = ViewState.Error("Password is required")
            return
        }
        if (!password.isValidPassword()) {
            _signUpState.value = ViewState.Error("Password must be at least 6 characters")
            return
        }
        if (password != confirmPassword) {
            _signUpState.value = ViewState.Error("Passwords do not match")
            return
        }

        // This is tp perform sign up
        _signUpState.value = ViewState.Loading

        viewModelScope.launch {
            val result = authRepository.signUp(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName
            )

            result.fold(
                onSuccess = { user ->
                    _signUpState.value = ViewState.Success(user)
                },
                onFailure = { exception ->
                    _signUpState.value = ViewState.Error(
                        exception.message ?: "Sign up failed. Please try again."
                    )
                }
            )
        }
    }


    // This is to reset the state to idle
    fun resetState() {
        _loginState.value = ViewState.Idle
        _signUpState.value = ViewState.Idle
    }
}