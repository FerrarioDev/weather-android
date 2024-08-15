package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.ui.theme.api.Constant
import com.example.weatherapp.ui.theme.api.NetworkResponse
import com.example.weatherapp.ui.theme.api.RetrofitInstance
import com.example.weatherapp.ui.theme.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData(city: String) {
        _weatherData.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response =weatherApi.getWeather(Constant.apiKey, city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherData.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherData.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                _weatherData.value = NetworkResponse.Error("Failed to load data")
            }
        }
    }
}