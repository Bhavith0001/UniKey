package com.unikey.android.ui.loginscreen

import android.content.SharedPreferences
import android.opengl.Visibility
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.DefaultValue
import com.unikey.android.REG_NO
import com.unikey.android.database.cloud.Authentication
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.database.cloud.EmailSent
import kotlinx.coroutines.*

class LoginViewModel : ViewModel() {


    /** Mode of the authentication screen*/
    private var _loginMode = true
    val loginMode
    get() = _loginMode


    /** Text on the Sign in button*/
    private var _btnLoginTxt = "Sign in"
    val btnLoginTxt
    get() = _btnLoginTxt


    /** Register button text*/
    private var _signUpBtnTxt = "Sign up"
    val signUpBtnTxt
    get() = _signUpBtnTxt


    /** Forget password button text*/
    private var _forgetPasswordIsEnabled = true
    val forgetPasswordIsEnabled
    get() = _forgetPasswordIsEnabled


    /** Welcome text */
    private var _welcomeTxt = "Sign in to continue..."
    val welcomeTxt
    get() = _welcomeTxt

    /** Input label text */
    private var _inputHintTxt = "Password"
    val inputHintTxt
    get() = _inputHintTxt

    /** Input Placeholder text text */
    private var _inputPlaceholderTxt = "Enter your password"
    val inputPlaceholderTxt
    get() = _inputPlaceholderTxt

    /* Instance of auth */
    private val auth = Authentication()

    // Instance of CloudStorage
    private val dataStore = CloudStorage()

    /** Return true if the user is authenticated
     * else returns false*/
    fun isAuthenticated() = auth.isAuthenticated()

    val isResetEmailSent: MutableLiveData<EmailSent> = MutableLiveData(EmailSent.DISMISS)



    /** This function sets all the ui text in the [LoginViewModel]
     * when the login mode changes*/
    fun setAllLoginUiTxt(
        loginMode: Boolean,
        btnLoginTxt: String,
        signUpBtnTxt: String,
        forgetPasswordEnabled: Boolean,
        welcomeTxt: String,
        hintTxt: String,
        placeHolder: String
    ){
        this._loginMode = loginMode
        this._btnLoginTxt = btnLoginTxt
        this._signUpBtnTxt = signUpBtnTxt
        this._forgetPasswordIsEnabled = forgetPasswordEnabled
        this._welcomeTxt = welcomeTxt
        this._inputHintTxt = hintTxt
        this._inputPlaceholderTxt = placeHolder
    }

    fun userLogin(
        email: String,
        password: String,
        regNo: String,
        sharedPref: SharedPreferences,
        result: (LogInResult) -> Unit
    ){
        auth.login(email, password) {

            if (it == LogInResult.SUCCESSFUL) {
                dataStore
                setRegNo(sharedPref, regNo.toLong())
                Log.d("FFFF", "login: $it")
            }
            result(it)
        }
    }

    private fun setRegNo(sharedPref: SharedPreferences, regNo: Long){
        Log.d("JJJJ", "onCreate: $regNo")
        with(sharedPref.edit()){
            putLong(REG_NO, regNo)
            Log.d("JJJJ", "onCreate: $regNo")
            apply()
        }
        val temp = sharedPref.getLong(REG_NO, DefaultValue)
        Log.d("JJJJ", "Testin pref: $temp")
        dataStore.updateRegNo(regNo)
        dataStore.printLog()
    }

    fun signUpUser(
        email: String,
        password: String,
        regNo: String,
        sharedPref: SharedPreferences,
        result : (SignUpResult) -> Unit
    ){
        viewModelScope.launch {

            val isExists = dataStore.isUserExists(email)

            if (isExists) {
                auth.signUp(email, password) {
                    if (it == SignUpResult.SUCCESSFUL) {
                        setRegNo(sharedPref, regNo.toLong())
                    }
                    result(it)
                }
            } else {
                result(SignUpResult.UNREGISTERED)
            }
        }

    }


    fun resetUserPassword(email: String) {
        viewModelScope.launch {
            auth.resetPassword(email).collect{ isSent ->
                isResetEmailSent.value = isSent
            }
        }
    }

    fun makeEmailSentDismiss(){
        auth.emailSent.value = EmailSent.DISMISS
    }

    fun  getUserRegNo (email: String, onRetrieved: (String?)-> Unit){
        viewModelScope.launch {
            val regNo = dataStore.getRegisterNo(email)
            Log.d("DOM", "getUserRegNo: $regNo")
            onRetrieved(regNo)
        }

    }


}
