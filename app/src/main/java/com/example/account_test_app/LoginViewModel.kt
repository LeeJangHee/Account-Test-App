package com.example.account_test_app

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var idState  = mutableStateOf<String>("")
    var pwState = mutableStateOf<String>("")


    fun setIdState(id: String?) {
        idState.value = id ?: ""
    }

    fun setPwState(pw: String?) {
        pwState.value = pw ?: ""
    }
}