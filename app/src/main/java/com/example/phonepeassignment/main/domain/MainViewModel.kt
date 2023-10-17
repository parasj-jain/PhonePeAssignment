package com.example.phonepeassignment.main.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonepeassignment.main.data.DataRepository
import com.example.phonepeassignment.main.data.RawDataRepository
import com.example.phonepeassignment.main.data.models.LogoModel
import com.example.phonepeassignment.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

//9819817483
//ronak.harkhani@phonepe.com

@HiltViewModel
class MainViewModel @Inject constructor(
    private val rawDataRepository: DataRepository
) : ViewModel() {

    private var logosList : List<LogoModel>? = null

    private var currentPosition = 0

    private val _logoModelFlow: MutableStateFlow<Resource<LogoModel>> =
        MutableStateFlow(Resource.Empty())
    val logoModelFlow: StateFlow<Resource<LogoModel>>
        get() = _logoModelFlow

    private val _validationFlow: MutableStateFlow<String?> =
        MutableStateFlow(null)
    val validationFlow: StateFlow<String?>
        get() = _validationFlow

    init {
        fetchLogosData()
    }

    private fun fetchLogosData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logosList = rawDataRepository.getLogosData()
                sendLogoToUI()
            } catch (e: Exception) {
                _logoModelFlow.value = Resource.Error("Something went wrong.")
            }
        }
    }

    fun validateAnswer(givenAnswer: String) {
        val correctAnswer = logosList?.get(currentPosition)?.name
        if (givenAnswer != correctAnswer)
            _validationFlow.value = "Answer not correct"
        else {
            currentPosition++
            if (currentPosition >= (logosList?.size ?: 0)) {
                _validationFlow.value = "Congratulation, Quiz Completed!"
            } else {
                _validationFlow.value = "Correct!!"
                sendLogoToUI()
            }
        }
    }

    private fun sendLogoToUI() {
        logosList?.get(currentPosition)?.let {
            _logoModelFlow.value = Resource.Success(it)
        }
    }

}