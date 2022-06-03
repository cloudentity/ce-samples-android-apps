package com.example.simplepkce

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.simplepkce.model.ScopeData
import com.example.simplepkce.model.TokenResponse
import com.example.simplepkce.repository.Repository
import com.example.simplepkce.util.Utils.isNum
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val tokenResponse: MutableLiveData<Response<TokenResponse>> = MutableLiveData()
    val intent = MutableLiveData<Intent?>()
    val tokenInfo: MutableLiveData<TokenResponse> = MutableLiveData()
    val resourceResult: MutableLiveData<String> = MutableLiveData()

    fun getTokenInfo(clientID: String, redirectURI: String, grantType: String, verifier: String, code: String) {
        viewModelScope.launch {
            tokenResponse.value = repository.getTokenInfo(clientID, redirectURI, grantType, verifier, code)
        }
    }

    fun initiateAuth(ctx: Context, clientID: String, redirectURI: String, authURL: String) {
        viewModelScope.launch {
            repository.initiateAuth(ctx, clientID, redirectURI, authURL)
        }
    }

    fun setCallbackIntent(callbackIntent: Intent) {
        viewModelScope.launch {
            intent.value = callbackIntent
        }
    }

    fun setTokenInfo(tr: TokenResponse?) {
        viewModelScope.launch {
            tokenInfo.value = tr
            val gson = Gson()
            val trJson = gson.toJson(tr)
            repository.storeTokenInfo(trJson)
        }
    }

    fun checkStore() {
        viewModelScope.launch {
            val storeTokenInfo = repository.getTokenInfoStored()
            val gson = Gson()
            setTokenInfo(gson.fromJson(storeTokenInfo, TokenResponse::class.java))
        }
    }

    fun logOut() {
        viewModelScope.launch {
            setTokenInfo(null)
            intent.value = null
        }
    }

    fun getResource(scope: ScopeData, tokenInfo: TokenResponse) {
        viewModelScope.launch {
             repository.getResource(scope, tokenInfo) {
                 if (it != null) {
                     if (isNum(it) && it.toInt() != 200) {
                         resourceResult.postValue("Error ${it.toInt()}")
                     }
                     else {
                         resourceResult.postValue(it)
                     }
                 }
             }
        }
    }

    fun clearResource() {
        viewModelScope.launch {
            resourceResult.value = null
        }
    }
}