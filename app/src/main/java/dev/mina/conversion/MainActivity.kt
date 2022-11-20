package dev.mina.conversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.conversion.ui.MainViewModel
import dev.mina.conversion.ui.screens.ExchangeScreen
import dev.mina.conversion.ui.screens.ExchangeScreenUIState.ExchangeUIState
import dev.mina.conversion.ui.screens.ExchangeScreenUIState.Loading
import dev.mina.conversion.ui.theme.ConversionTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversionTheme {
                val uiState by viewModel.uiState.collectAsState()
                when (uiState) {
                    is ExchangeUIState -> ExchangeScreen(
                        uiState = uiState as ExchangeUIState,
                        onFromSelectionChange = {},
                        onFromValueChange = {},
                        onToSelectionChange = {},
                        onSwitchClick = {}
                    )
                    Loading -> {}
                }
            }
        }
    }
}