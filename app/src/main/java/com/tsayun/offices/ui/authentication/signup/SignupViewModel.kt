package com.tsayun.offices.ui.authentication.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsayun.offices.R
import com.tsayun.offices.data.authentication.signup.SignupRepository
import com.tsayun.offices.data.common.model.Result
import com.tsayun.offices.ui.authentication.common.LoginCredentials
import com.tsayun.offices.ui.authentication.common.Validation

class SignupViewModel(private val signupRepository: SignupRepository) : ViewModel() {

    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupFormState: LiveData<SignupFormState> = _signupForm

    private val _signupResult = MutableLiveData<SignupResult>()
    val signupResult: LiveData<SignupResult> = _signupResult

    private val _loginRequest = MutableLiveData<LoginCredentials>()
    val loginRequest: LiveData<LoginCredentials> = _loginRequest

    fun signup(displayName: String, username: String,password: String) {
        // can be launched in a separate asynchronous job
        val result = signupRepository.signup(displayName, username, password)

        if (result is Result.Success) {
            _signupResult.value = SignupResult(success = SignedUpUserView(id = result.data.userId,displayName = result.data.displayName))
        } else {
            _signupResult.value = SignupResult(error = R.string.toast_text_error_while_signing_up)
        }
    }

    fun signupDataChanged(displayName: String, username: String, password: String) {
        if (!Validation.isDisplayNameValid(displayName)) {
            _signupForm.value = SignupFormState(displayNameError = R.string.invalid_display_name)
        } else if (!Validation.isUserNameValid(username)) {
            _signupForm.value = SignupFormState(usernameError = R.string.invalid_username)
        } else if (!Validation.isPasswordValid(password)) {
            _signupForm.value = SignupFormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = SignupFormState(isDataValid = true)
        }
    }

    fun requestLogin(displayName: String, username: String,password: String){
        _loginRequest.value = LoginCredentials(username = username, password = password)
    }
}