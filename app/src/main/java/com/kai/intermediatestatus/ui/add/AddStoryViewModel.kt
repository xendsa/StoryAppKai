package com.kai.intermediatestatus.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kai.intermediatestatus.data.Repository
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.data.local.UserModel
import com.kai.intermediatestatus.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: Repository) : ViewModel() {

    private val _addStory = MediatorLiveData<Result<UploadResponse>>()
    val addStory: LiveData<Result<UploadResponse>> = _addStory

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) {
        val liveData = repository.addStory(token, file, description)
        _addStory.addSource(liveData) { result ->
            _addStory.value = result
        }
    }
}