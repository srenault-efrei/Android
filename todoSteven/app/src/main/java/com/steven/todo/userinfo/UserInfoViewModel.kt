package com.steven.todo.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steven.todo.tasklist.Task
import com.steven.todo.tasklist.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private val _infos = MutableLiveData<UserInfo>()
    val infos: LiveData<UserInfo> = _infos


    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            repository.updateAvatar(avatar)
        }
    }

    fun getInfos() {
        viewModelScope.launch {
            _infos.value = repository.getInfo()
        }
    }

    fun update(body: UserInfo) {
        viewModelScope.launch {
            _infos.value = repository.update(body)
        }
    }


}
