package com.unikey.android.ui.loginscreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.*
import com.unikey.android.database.cloud.EmailSent
import com.unikey.android.databinding.FragmentLoginScreenBinding
import com.unikey.android.ui.loginscreen.SignUpResult.*


class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var  sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.Z)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getUnikeySharedPref()

        binding.signUpBtn.setOnClickListener {
            binding.emailInputLayout.error = null
            binding.passwordInputLayout.error = null
            binding.regNoInputLayout.error = null
            changeAuthMode()
        }

        binding.regNoInputLayout.setEndIconOnClickListener {
            binding.regNoInputLayout.helperText = null
            val email = binding.emailTextInput.text.toString()

            viewModel.getUserRegNo(email){ regNo ->
                if (regNo == null) {
                    binding.regNoInputLayout.helperText = "Check your email id"
                    binding.regNoInput.text = regNo
                }
                else {
                    binding.regNoInput.setText(regNo)
                }
            }
        }

        binding.addAllTexts()

        binding.btnLogin.setOnClickListener {
            binding.emailInputLayout.error = null
            binding.passwordInputLayout.error = null

            authenticate(
                binding.emailTextInput.text.toString(),
                binding.passwordTextInput.text.toString(),
                binding.regNoInput.text.toString()
            )
        }

        binding.apply {
            emailTextInput.doOnTextChanged { text, _, _, _ ->

                if (text?.contains(" ", true) == true )
                    binding.emailInputLayout.error = "Remove white spaces"
                else
                    binding.emailInputLayout.error = null
            }

            passwordTextInput.doOnTextChanged { _, _, _, _ ->
                binding.passwordInputLayout.error = null
            }

        }

        binding.forgetPasswordBtn.setOnClickListener {
            showPasswordResetDialog()
        }

        viewModel.isResetEmailSent.observe(viewLifecycleOwner){ isSent ->
            Log.d(TAG, "onViewCreated: $isSent")

            when (isSent){
                EmailSent.SENT -> showEmailSentDialog()
                EmailSent.FAILED -> showToast("Something went wrong. Try again")
                EmailSent.DISMISS -> Log.d(TAG, "onViewCreated: isSent $isSent")
                null -> showToast("Something went wrong")
            }
        }

    }


    /** Shows error in the TextInputLayouts if the email and/or password is not correct  */
    private fun showLoginError() {
        binding.emailInputLayout.error = "Incorrect email"
        binding.passwordInputLayout.error = "Incorrect password"
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    /**   This function sets appropriate texts to all views for [LoginScreenFragment]   */
    private fun FragmentLoginScreenBinding.addAllTexts() {
        btnLogin.text = viewModel.btnLoginTxt
        signUpBtn.text = viewModel.signUpBtnTxt
        forgetPasswordBtn.isEnabled = viewModel.forgetPasswordIsEnabled
        welcomeTxt.text = viewModel.welcomeTxt
        passwordInputLayout.hint = viewModel.inputHintTxt
        passwordInputLayout.placeholderText = viewModel.inputPlaceholderTxt
        emailTextInput.text = null
        passwordTextInput.text = null
        regNoInput.text = null
    }

    /**  This function changes the mode of the screen
     * Login mode to Sign up mode
     * and vice versa*/
    private fun changeAuthMode() {
        if (viewModel.loginMode) {
            viewModel.setAllLoginUiTxt(
                loginMode = false,
                btnLoginTxt = "Sign up",
                signUpBtnTxt = "Sign in",
                forgetPasswordEnabled = false,
                welcomeTxt = "Sign up to continue...",
                hintTxt = "New password",
                placeHolder = "Enter new password"
            )

            binding.addAllTexts()

        } else {
            viewModel.setAllLoginUiTxt(
                loginMode = true,
                btnLoginTxt = "Sign in",
                signUpBtnTxt = "Sign up",
                forgetPasswordEnabled = true,
                welcomeTxt = "Sign in to continue...",
                hintTxt = "Password",
                placeHolder = "Enter your password"
            )

            binding.addAllTexts()

        }
    }

    private fun authenticate(email: String, password: String, regNo: String) {

        val isEmailBlank = binding.emailTextInput.text?.isBlank()
        val isPasswordBlank = binding.passwordTextInput.text?.isBlank()
        val isRegNoIsBlank =  binding.regNoInput.text?.isBlank()
        val containsWhiteSpace = binding.emailTextInput.text?.contains(" ", true) == true

        if(isRegNoIsBlank == false && isEmailBlank == false && isPasswordBlank == false && !containsWhiteSpace) {
            binding.applyProgressBar(true)


            if (viewModel.loginMode) {
                viewModel.userLogin(email, password, regNo, requireActivity().getUnikeySharedPref()) { result ->
                    binding.applyProgressBar(false)

                    when(result){
                        LogInResult.SUCCESSFUL -> findNavController().navigate(LoginScreenFragmentDirections
                                .actionLoginScreenFragmentToMainNav())
                        LogInResult.INVALID_PASSWORD -> binding.passwordInputLayout.error = "Invalid Password"
                        LogInResult.INVALID_EMAIL -> binding.emailInputLayout.error = "Invalid Email"
                        else -> Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                    viewModel.signUpUser(email, password, regNo, requireActivity().getUnikeySharedPref()) { result ->

                        binding.applyProgressBar(false)
                        when(result){
                            SUCCESSFUL -> findNavController().navigate(LoginScreenFragmentDirections.actionLoginScreenFragmentToMainNav())
                            UNREGISTERED -> showSignUpErrorDialogue(requireContext())
                            WEEK_PASSWORD -> binding.passwordInputLayout.error = "Weak password"
                            USER_ALREADY_EXISTS -> Toast.makeText(requireContext(), "User already exists", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        } else if(containsWhiteSpace){
            binding.emailInputLayout.error = "Remove white spaces"
        } else {
            showEmptyInputFieldError()
        }
    }

    private fun showEmptyInputFieldError() {
        if (
            binding.emailTextInput.text?.isBlank() == true &&
            binding.passwordTextInput.text?.isBlank() == true &&
            binding.regNoInput.text?.isBlank() == true
        ){
            showToast("Input fields are empty")
        } else if (binding.emailTextInput.text?.isBlank() == true)
            binding.emailInputLayout.error = "Enter email ID"
        else if (binding.passwordTextInput.text?.isBlank() == true)
            binding.passwordInputLayout.error = "Enter password"
        else
            binding.regNoInputLayout.helperText = "Add Reg.no"
    }


    /** This function enables the progressbar and disables all other interactive views */
    private fun  FragmentLoginScreenBinding.applyProgressBar(enabled: Boolean) {

        if (enabled)
            progressBarHorizontal.visibility = View.VISIBLE
        else
            progressBarHorizontal.visibility = View.GONE

        emailInputLayout.isEnabled = !enabled
        passwordInputLayout.isEnabled = !enabled
        btnLogin.isEnabled = !enabled
        signUpBtn.isEnabled = !enabled
        forgetPasswordBtn.isEnabled = viewModel.loginMode
    }

    private fun showSignUpErrorDialogue(context: Context){
        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.not_registered)
            .setPositiveButton("OK"){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }


    private fun showPasswordResetDialog() {

        val builder = MaterialAlertDialogBuilder(requireContext())
        val fEmail = layoutInflater.inflate(R.layout.password_reset_email_input, null)
        val emailTxt = fEmail.findViewById<EditText>(R.id.email_input)
        val emailLayout = fEmail.findViewById<TextInputLayout>(R.id.email_layout)

        with(builder) {
            setTitle("Reset password")
            setMessage("Please enter email id to reset your password")
            setView(fEmail)
            setPositiveButton("Proceed") { dialog, _ ->
                Log.d(TAG, "showPasswordResetDialog: ${emailTxt.text}")
                Log.d(TAG, "sendResetMail: not blank")
                viewModel.resetUserPassword(emailTxt.text.toString())
                dialog.cancel()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }

        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        emailTxt.doOnTextChanged { text, _, _, _ ->

            if (text?.contains(" ", true) == true) {
                emailLayout.error = "Remove white spaces"
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            }
            else {
                emailLayout.error = null
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = text?.isNotBlank() == true
            }
        }

    }

    private fun showEmailSentDialog() {
       MaterialAlertDialogBuilder(requireContext())
            .setTitle("Email sent")
            .setMessage(R.string.email_sent_msg)
            .setPositiveButton("Okay") { dialog, _ ->
                viewModel.makeEmailSentDismiss()
                dialog.cancel()
            }
           .show()
    }

}

/** Represents the result arrived on user signUp
 * @property FAILED signup was failed if user already signed up or any other error
 * @property SUCCESS sign up was successful
 * @property UNREGISTERED represents that the user is not not registered in the server*/
enum class SignUpResult {
    FAILED, SUCCESSFUL, UNREGISTERED, WEEK_PASSWORD, USER_ALREADY_EXISTS
}

enum class LogInResult {
    SUCCESSFUL, INVALID_EMAIL, INVALID_PASSWORD, FAILED
}