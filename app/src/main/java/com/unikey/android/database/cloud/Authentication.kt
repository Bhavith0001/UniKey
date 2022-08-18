package com.unikey.android.database.cloud

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.unikey.android.TAG
import com.unikey.android.ui.loginscreen.LogInResult
import com.unikey.android.ui.loginscreen.SignUpResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext
import kotlin.math.log


class Authentication {

    /** Instance of FirebaseAuth **/
    private val auth: FirebaseAuth = Firebase.auth

    /** Instance of current user  */
    private val currentUser = auth.currentUser

    /** Represents the state of the reset password email */
    var emailSent: MutableStateFlow<EmailSent> = MutableStateFlow(EmailSent.DISMISS)

    /**
     * Returns true if the user us authenticated.
     * Else returns false
     *
     */
    fun isAuthenticated() = currentUser != null

    /** Makes the user login to the application if the user is already registered
     * It takes two parameters
     * @param email Email id of the user
     * @param password Password of the user 
     * @param isSuccess A lambda function that takes a boolean and returns nothing */
    fun login(
        email: String,
        password: String,
        result: (LogInResult) -> Unit
    ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    result(LogInResult.SUCCESSFUL)
                    Log.d("FFFF", "login 1: ${task.isSuccessful}")
                } else if (task.exception != null) {
                    when(task.exception){
                        is FirebaseAuthInvalidCredentialsException -> result(LogInResult.INVALID_PASSWORD)
                        is FirebaseAuthInvalidUserException -> result(LogInResult.INVALID_EMAIL)
                        else -> result(LogInResult.FAILED)
                    }
                    Log.d("FFFF", "login 2: ${task.exception}")
                }

            }

    }

    /** Makes the user Register to the application if the user is not already registered
     * It takes two parameters
     * @param email Email id of the user
     * @param password Password of the user
     * @param isSuccess A lambda function that takes a boolean and returns nothing */
    fun signUp(
        email: String,
        password: String,
        result: (SignUpResult) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                Log.d("FFFF", "login 1: ${task.isSuccessful}")

                if (task.isSuccessful){
                    result(SignUpResult.SUCCESSFUL)
                } else if (task.exception != null) {
                    when(task.exception){
                        is FirebaseAuthUserCollisionException -> result(SignUpResult.USER_ALREADY_EXISTS)
                        is FirebaseAuthWeakPasswordException -> result(SignUpResult.WEEK_PASSWORD)
                        else -> result(SignUpResult.FAILED)
                    }
                    Log.d("FFFF", "login 2: ${task.exception}")
                }
            }

    }


    /** Sign's out the user from the application */
    fun signOut() = auth.signOut()


    /** This function sends an email to the user to reset the password
     * @param email Email of the user */
    fun resetPassword(email: String): StateFlow<EmailSent> {
        Log.d(TAG, "resetPassword: entered")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                Log.d(TAG, "listener out: email sent ${task.exception}")

                if (task.isSuccessful) {
                    Log.d(TAG, "listener: email sent ${task.isSuccessful}")
                    emailSent.value = EmailSent.SENT
                } else {
                    emailSent.value = EmailSent.FAILED
                }

        }
        Log.d(TAG, "emitted: ")
        return emailSent
    }

}

/** Represents three modes of reset password email
 * @property SENT represents email has sent
 * @property FAILED represents email failed to send
 * @property DISMISS represents email sent and back to normal */
enum class EmailSent{
    SENT, FAILED, DISMISS
}