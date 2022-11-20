package dev.mina.conversion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mina.conversion.ui.theme.Typography

@Preview
@Composable
fun CardSample() {
    val rates = listOf("USD", "EGP", "AED", "EUR")
    Column {
        FromCard(rateList = rates, assignedValue = 1.0, {}, {})
        ToCard(rateList = rates, convertedValue = 1.0) {}
    }
}

@Composable
fun FromCard(
    rateList: List<String>,
    assignedValue: Double,
    onValueChange: (Double) -> Unit,
    onSelectionChange: (String) -> Unit,
) {
    var value by remember { mutableStateOf(assignedValue.toString()) }
    val type by remember { mutableStateOf(CardType.From) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(type.color, type.shape)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f), type.shape),
    ) {
        Row(modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically) {
            Dropdown(modifier = Modifier.weight(0.8F),
                type = type,
                rateList = rateList,
                onMenuItemClick = onSelectionChange)
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1F),
                value = value,
                textStyle = Typography.h1.copy(textAlign = TextAlign.End),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = type.color),
                onValueChange = { newText ->
                    value =
                        newText
                            .filter { it.isDigit() || (it == '.' && newText.toDoubleOrNull() != null) }
                            .take(9)
                    onValueChange.invoke(value.toDoubleOrNull() ?: 0.0)
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = {
                    Text(text = "0.0",
                        modifier = Modifier.fillMaxWidth(),
                        style = Typography.h1.copy(textAlign = TextAlign.End))
                },
            )
        }
    }

}

@Composable
fun ToCard(
    rateList: List<String>,
    convertedValue: Double,
    onSelectionChange: (String) -> Unit,
) {
    val type by remember { mutableStateOf(CardType.To) }
    val textSize = remember { mutableStateOf(28.sp) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(type.color, type.shape)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f), type.shape),
    ) {
        Row(modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically) {
            Dropdown(modifier = Modifier.weight(0.8F),
                type = type,
                rateList = rateList,
                onMenuItemClick = onSelectionChange)
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1F),
                text = convertedValue.toString(),
                textAlign = TextAlign.End,
                fontSize = textSize.value,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1
                    if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                        textSize.value = textSize.value.times(TEXT_SCALE_REDUCTION_INTERVAL)
                    }
                },
            )
        }

    }
}

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f