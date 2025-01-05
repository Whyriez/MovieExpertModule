package com.alimsuma.movieexpert.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alimsuma.core.core.data.source.local.preferences.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val settingPreferences: SettingPreferences) : ViewModel() {
    fun getThemeSetting() = settingPreferences.getThemeSetting().asLiveData()
}