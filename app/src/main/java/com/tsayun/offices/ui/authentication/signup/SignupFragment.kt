package com.tsayun.offices.ui.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tsayun.offices.R
import com.tsayun.offices.databinding.FragmentSignupBinding
import com.tsayun.offices.ui.common.afterTextChanged

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val signupViewModel: SignupViewModel by activityViewModels()

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root

        val username = binding.signupUsername
        val password = binding.signupPassword
        val displayName = binding.signupDisplayName
        val signup = binding.signupSignupButton
        val login = binding.signupLoginButton

        signupViewModel.signupFormState.observe(viewLifecycleOwner, Observer {
            val signupState = it ?: return@Observer

            // disable signup button unless displayName / username / password are valid
            signup.isEnabled = signupState.isDataValid

            if (signupState.displayNameError != null) {
                displayName.error = getString(signupState.displayNameError)
            }
            if (signupState.usernameError != null) {
                username.error = getString(signupState.usernameError)
            }
            if (signupState.passwordError != null) {
                password.error = getString(signupState.passwordError)
            }
        })

        signupViewModel.signupResult.observe(viewLifecycleOwner, Observer {
            val signupResult = it ?: return@Observer

            if (signupResult.error != null) {
                showSignupFailed(signupResult.error, view)
            }
            if (signupResult.success != null) {
                updateUiWithUser(signupResult.success, view)

                // Complete and destroy signup activity once successful
//                setResult(Activity.RESULT_OK)
//                finish()
            }
        })

        displayName.afterTextChanged {
            signupViewModel.signupDataChanged(
                displayName.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        username.afterTextChanged {
            signupViewModel.signupDataChanged(
                displayName.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signupViewModel.signupDataChanged(
                    displayName.text.toString(),
                    username.text.toString(),
                    password.text.toString()
                )
            }

            //todo: remove setOnEditorActionListener as it is immune to logging checks
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signupViewModel.signup(
                            displayName.text.toString(),
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            signup.setOnClickListener {
                signupViewModel.signup(
                    displayName.text.toString(),
                    username.text.toString(),
                    password.text.toString()
                )
            }

            login.setOnClickListener{
                signupViewModel.requestLogin(
                    displayName.text.toString(),
                    username.text.toString(),
                    password.text.toString()
                )
            }

        }
        return view
    }

    private fun updateUiWithUser(model: SignedUpUserView, view: View) {
        binding.signupDisplayName.text.clear()
        binding.signupUsername.text.clear()
        binding.signupPassword.text.clear()

    }

    private fun showSignupFailed(@StringRes errorString: Int, view: View) {
        Toast.makeText(view.context, errorString, Toast.LENGTH_SHORT).show()
    }
}