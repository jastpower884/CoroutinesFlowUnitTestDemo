package com.jastzeonic.coroutinesflowunittestdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestedViewModel : ViewModel() {


    val testStateFlow = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val testLiveData = MutableLiveData<Result<Boolean>>()
    val testSharedFlow = MutableSharedFlow<Result<Boolean>>()

    fun demoCoroutinesRunInSingleThread() {
        viewModelScope.launch {

            println("viewModelScope launch")
            Result
            launch {
                println("viewModelScope first inner launch")
                withContext(Dispatchers.IO) {
                    println("viewModelScope first dispatchers io")
                    delay(100)
                    testStateFlow.emit(Result.Success(true))
                }
            }

            launch {
                println("viewModelScope second inner launch")
                withContext(Dispatchers.IO) {
                    println("viewModelScope second dispatchers io")
                    delay(100)
                    testStateFlow.emit(Result.Success(true))
                }
            }

        }
    }

    fun demoHowToTestStateFlow() {
        viewModelScope.launch {
            testStateFlow.emit(withContext(Dispatchers.IO) {
                delay(100)
                Result.Success(true)
            })
        }
    }

    fun demoHowToTestLiveData() {
        viewModelScope.launch {
            testLiveData.value = withContext(Dispatchers.IO) {
                delay(100)
                Result.Success(true)
            }
        }
    }
    fun demoHowToTestSharedFlow() {
        viewModelScope.launch {
            testSharedFlow.emit(withContext(Dispatchers.IO) {
                delay(100)
                Result.Success(true)
            })
        }
    }

}