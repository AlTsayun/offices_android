package com.tsayun.offices.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsayun.offices.data.authentication.login.LoginRepository
import com.tsayun.offices.data.common.model.Result

import com.tasyun.offices.R
import com.tsayun.offices.ui.authentication.common.SignupCredentials
import com.tsayun.offices.ui.authentication.common.Validation

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _signupRequest = MutableLiveData<SignupCredentials>()
    val signupRequest: LiveData<SignupCredentials> = _signupRequest

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(id = result.data.userId,displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!Validation.isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!Validation.isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun requestSignup(username: String, password: String) {
        _signupRequest.value = SignupCredentials(username = username, password = password)
    }
}