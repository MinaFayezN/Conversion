package dev.mina.conversion.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mina.conversion.ui.screens.DetailsScreenUIState
import dev.mina.conversion.ui.theme.LightGray02

@Composable
fun OthersItems(
    modifier: Modifier,
    uiState: DetailsScreenUIState.DetailsUIState,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(uiState.others) {
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .background(shape = RoundedCornerShape(20.dp), color = LightGray02)
                    .border(0.5.dp,
                        MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp))
                    .padding(4.dp),
                text = if (it.contains("Date:")) it else it.substring(3),
                fontSize = if (it.contains("Date:")) 24.sp else 20.sp,
                textAlign = if (it.contains("Date:")) TextAlign.Start else TextAlign.Center,
                fontWeight = if (it.contains("Date:")) FontWeight.Bold else FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
            )
        }
    }

}