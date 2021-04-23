package com.tsayun.offices.ui.authentification.login

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import com.tasyun.offices.R
import com.tasyun.offices.databinding.FragmentLoginBinding
import com.tsayun.offices.ui.common.afterTextChanged

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error, view)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success, view)

                // Complete and destroy login activity once successful
//                setResult(Activity.RESULT_OK)
//                finish()
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            //todo: remove setOnEditorActionListener as it is immune to logging checks
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
        return view
    }

    private fun updateUiWithUser(model: LoggedInUserView, view: View) {
        binding.username.text.clear()
        binding.password.text.clear()
    }

    private fun showLoginFailed(@StringRes errorString: Int, view: View) {
        Toast.makeText(view.context, errorString, Toast.LENGTH_SHORT).show()
    }
}