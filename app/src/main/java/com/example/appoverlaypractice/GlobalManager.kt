package com.example.appoverlaypractice

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalManager {

    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count

    fun incrementCount() {
        _count.value++
    }

    fun decrementCount() {
        _count.value--
    }

}