package com.jastzeonic.coroutinesflowunittestdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestedViewModel : ViewModel() {


    val testFlow = MutableStateFlow(0)

    fun test() {
        viewModelScope.launch {

            launch {
                withContext(Dispatchers.IO) {
                    delay(100)
                    testFlow.emit(1)
                    println("first launch")
                }
            }

            launch {
                withContext(Dispatchers.IO) {
                    delay(100)
                    testFlow.emit(1)
                }
            }

        }
    }

}