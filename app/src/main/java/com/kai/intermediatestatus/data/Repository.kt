package com.kai.intermediatestatus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kai.intermediatestatus.data.api.ApiService
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.data.local.UserModel
import com.kai.intermediatestatus.data.local.UserPreference
import com.kai.intermediatestatus.data.response.ListStoryItem
import com.kai.intermediatestatus.data.response.LoginResponse
import com.kai.intermediatestatus.data.response.RegisterResponse
import com.kai.intermediatestatus.data.response.UploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody


class Repository private constructor(
    private val userPreference: UserPreference,
    private val service: ApiService,
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = service.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = service.login(email, password)
            val token = response.loginResult.token
            saveSession(UserModel(email, token))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = service.getStories("Bearer $token")
                val story = response.storyList
                emit(Result.Success(story))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun addStory(
        token: String,
        file:MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<UploadResponse>> =
        liveData(Dispatchers.IO){
            emit(Result.Loading)
            try {
                val response = service.uploadStory("Bearer $token", file, description)
                emit(Result.Success(response))
            }catch (e: Exception){
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            service: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, service)
            }.also { instance = it }
    }
}