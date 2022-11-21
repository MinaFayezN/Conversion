package dev.mina.conversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.conversion.ui.DetailsViewModel
import dev.mina.conversion.ui.ExchangeViewModel
import dev.mina.conversion.ui.screens.DetailsScreen
import dev.mina.conversion.ui.screens.DetailsScreenUIState
import dev.mina.conversion.ui.screens.ExchangeScreen
import dev.mina.conversion.ui.screens.ExchangeScreenUIState
import dev.mina.conversion.ui.theme.ConversionTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val exchangeViewModel: ExchangeViewModel by viewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversionTheme {
                val navController = rememberNavController()
                val exchangeUiState by exchangeViewModel.uiState.collectAsState()
                val uiState by detailsViewModel.uiState.collectAsState()
                NavHost(navController = navController, startDestination = "exchange") {
                    composable("exchange") {
                        when (exchangeUiState) {
                            is ExchangeScreenUIState.ExchangeUIState -> ExchangeScreen(
                                uiState = exchangeUiState as ExchangeScreenUIState.ExchangeUIState,
                                onFromSelectionChange = { exchangeViewModel.changeSelection(from = it) },
                                onFromValueChange = { exchangeViewModel.changeValue(it) },
                                onToSelectionChange = { exchangeViewModel.changeSelection(to = it) },
                                onSwitchClick = { from, to ->
                                    exchangeViewModel.changeSelection(from = to, to = from)
                                }, onDetailsClick = { from, to, fromValue ->
                                    detailsViewModel.loadHistoricalRates(from,to,fromValue)
                                    navController.navigate("details")
                                }
                            )
                            is ExchangeScreenUIState.Loading -> {/*TODO*/
                            }
                            is ExchangeScreenUIState.Error -> {/*TODO*/
                            }
                        }
                    }
                    composable("details") {
                        when (uiState) {
                            is DetailsScreenUIState.DetailsUIState -> DetailsScreen(
                                uiState = uiState as DetailsScreenUIState.DetailsUIState,
                            )
                            is DetailsScreenUIState.Loading -> {/*TODO*/
                            }
                            is DetailsScreenUIState.Error -> {/*TODO*/
                            }
                        }
                    }
                }


            }
        }
    }
}