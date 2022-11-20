package dev.mina.conversion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mina.conversion.ui.theme.Typography

@Preview
@Composable
fun CardSample() {
    val rates = listOf("USD", "EGP", "AED", "EUR")
    Column {
        Card(type = CardType.From, rateList = rates, assignedValue = "1", onValueChange = {})
        Card(type = CardType.To, rateList = rates, assignedValue = "1", onValueChange = {})
    }
}

@Composable
fun Card(
    type: CardType,
    rateList: List<String>,
    assignedValue: String,
    onValueChange: (String) -> Unit,
) {
    var value by remember { mutableStateOf(assignedValue) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(type.color,type.shape)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f),type.shape),
    ) {
        Row(modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically) {
            Dropdown(modifier = Modifier.weight(1F), type = type, rateList = rateList)
            when (type) {
                CardType.From -> TextField(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1F),
                    value = value,
                    textStyle = Typography.h1.copy(textAlign = TextAlign.End),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = type.color),
                    onValueChange = {
                        value = it
                        onValueChange.invoke(it)
                    },
                )
                CardType.To -> Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1F),
                    text = value,
                    textAlign = TextAlign.End,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }

}