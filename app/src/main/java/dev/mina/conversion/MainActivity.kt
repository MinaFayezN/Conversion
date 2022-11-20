package dev.mina.conversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.conversion.ui.ExchangeViewModel
import dev.mina.conversion.ui.screens.ExchangeScreen
import dev.mina.conversion.ui.screens.ExchangeScreenUIState
import dev.mina.conversion.ui.screens.ExchangeScreenUIState.ExchangeUIState
import dev.mina.conversion.ui.screens.ExchangeScreenUIState.Loading
import dev.mina.conversion.ui.theme.ConversionTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ExchangeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversionTheme {
                val uiState by viewModel.uiState.collectAsState()
                when (uiState) {
                    is ExchangeUIState -> ExchangeScreen(
                        uiState = uiState as ExchangeUIState,
                        onFromSelectionChange = { viewModel.changeSelection(from = it) },
                        onFromValueChange = {},
                        onToSelectionChange = { viewModel.changeSelection(to = it) },
                        onSwitchClick = { from, to ->
                            viewModel.changeSelection(from = to, to = from)
                        }
                    )
                    is Loading -> {}
                    is ExchangeScreenUIState.Error -> {}
                }
            }
        }
    }
}