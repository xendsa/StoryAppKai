package com.kai.intermediatestatus.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kai.intermediatestatus.data.Repository
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.data.local.UserModel
import com.kai.intermediatestatus.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _listStory = MediatorLiveData<Result<List<ListStoryItem>>>()
    val listStory: LiveData<Result<List<ListStoryItem>>> = _listStory

    fun getStories(token: String){
        val liveData = repository.getStories(token)
        _listStory.addSource(liveData){ result ->
            _listStory.value = result
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
