package com.example.mviexample.view

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.mviexample.UiIntent
import com.example.mviexample.UiState
import com.example.mviexample.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun MainView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints {
        ConstraintLayout(
            constraintSet = loginViewConstraints(),
            modifier = modifier
        ) {
            val text = remember {
                mutableStateOf("")
            }
            Button(onClick = {
                scope.launch {
                    // 1. When user click button then emit the intent to view model and tell then which user intent.
                    viewModel.userIntent.send(UiIntent.GetData)
                }
            }, modifier = Modifier.layoutId("button")) {
                Text(text = "Button")
            }
            Text(text = text.value, modifier = Modifier.layoutId("text"))

            // 2. LaunchedEffect only process when state changed
            LaunchedEffect(Unit) {
                // 3. Collect view model state when state has been changed.
                viewModel.state.collect {
                    when (it) {
                        // 4. Update UI through state unwrap the result.
                        is UiState.DoNothing -> {}
                        is UiState.Error -> {
                            text.value = it.message
                        }
                        is UiState.Success -> {
                            text.value = it.data.toString()
                        }
                    }
                }
            }
        }
    }
}

fun loginViewConstraints(): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")
        constrain(button) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(text.top)
        }
        constrain(text) {
            top.linkTo(button.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
    }
}