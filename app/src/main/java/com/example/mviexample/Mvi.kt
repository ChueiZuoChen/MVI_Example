package com.example.mviexample

// Intent is not Android.Intent, it's just a describe or a concept to present the User "actions".
sealed class UiIntent {
    // User want to get a data.
    object GetData : UiIntent()
}


// State will hold the state after User 'action', and through the view model lifecycle observation to update the View.
sealed class UiState {
    object DoNothing : UiState()
    data class Success(val data: List<String>) : UiState()
    data class Error(val message: String) : UiState()
}


