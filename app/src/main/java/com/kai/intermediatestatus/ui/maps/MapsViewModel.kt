package com.kai.intermediatestatus.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kai.intermediatestatus.data.Repository
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.data.local.UserModel
import com.kai.intermediatestatus.data.response.ListStoryItem

class MapsViewModel(private val repository: Repository) : ViewModel() {

    private val _mapsResponse = MediatorLiveData<Result<List<ListStoryItem>>>()
    val mapsResponse: LiveData<Result<List<ListStoryItem>>> = _mapsResponse

    fun getLocation(token: String) {
        val liveData = repository.getStoriesWithLocation((token))
        _mapsResponse.addSource(liveData) { result ->
            _mapsResponse.value = result
        }
    }
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}