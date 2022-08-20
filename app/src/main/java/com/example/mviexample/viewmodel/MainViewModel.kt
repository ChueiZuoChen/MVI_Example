package com.example.mviexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviexample.UiIntent
import com.example.mviexample.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel : ViewModel() {

    // 1. User Intent use coroutines 'Channel' component.
    val userIntent = Channel<UiIntent>(Channel.UNLIMITED)

    // 2. State use MutableStateFlow or MutableSharedFlow to emit data into the Flow, depends on the which context or view's senario.
    // 3. When the application open first time you need to initial intent(User's action)
    // In here I set UiState to DoNothing.
    private val _state = MutableStateFlow<UiState>(UiState.DoNothing)
    val state: StateFlow<UiState>
        get() = _state.asStateFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            // 4. view model only collect user's intent when Channel changed.
            userIntent.consumeAsFlow().collect {
                when (it) {
                    // 5. if user intent is get data
                    is UiIntent.GetData -> getData()
                }
            }
        }
    }

    // 6. Intent process
    private fun getData() {
        viewModelScope.launch {
            _state.value = try {
                //throw Exception()
                UiState.Success(data)
            } catch (e: Exception) {
                UiState.Error("Loading Error")
            }
        }
    }

    val data = listOf(
        "1", "2", "3"
    )
}