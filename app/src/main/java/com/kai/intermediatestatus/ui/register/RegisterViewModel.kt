package com.kai.intermediatestatus.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.kai.intermediatestatus.data.Repository
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.data.response.RegisterResponse

class RegisterViewModel(private val repository: Repository): ViewModel() {

    private val _registerResponse = MediatorLiveData<Result<RegisterResponse>>()
    val registerResponse: LiveData<Result<RegisterResponse>> = _registerResponse

    fun register(name: String, email: String, password: String){
        val liveData = repository.register(name, email, password)
        _registerResponse.addSource(liveData){result ->
            _registerResponse.value = result
        }
    }
}