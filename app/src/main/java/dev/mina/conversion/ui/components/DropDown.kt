package dev.mina.conversion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mina.conversion.ui.theme.LightGray01
import dev.mina.conversion.ui.theme.LightGray02

@Preview
@Composable
fun MenuSample() {
    val rates = listOf("USD", "EGP", "AED", "EUR")
    Box {
        Dropdown(rateList = rates, type = CardType.From)
    }
}

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    type: CardType,
    rateList: List<String>,
    onMenuItemClick: (String) -> Unit = {},
) {
    var menuExpandedState by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { menuExpandedState = true }),
    ) {
        Text(
            text = type.text,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            color = Color.Black.copy(alpha = 0.75F),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = rateList[0],
                modifier = Modifier.padding(end = 16.dp),
                fontSize = 28.sp,
            )
            val displayIcon: Painter = painterResource(android.R.drawable.arrow_down_float)
            Icon(
                painter = displayIcon,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colors.onSurface
            )

            DropdownMenu(
                expanded = menuExpandedState,
                onDismissRequest = { menuExpandedState = false },
                modifier = Modifier
                    .fillMaxWidth(0.35F)
                    .background(MaterialTheme.colors.surface)
            ) {
                rateList.forEachIndexed { index, title ->
                    DropdownMenuItem(
                        onClick = {
                            if (0 != index) {
                                onMenuItemClick(rateList[index])
                            }
                            menuExpandedState = false
                        }
                    ) {
                        Text(text = title,
                            fontSize = 20.sp,
                            fontWeight = if (index == 0) Medium else Normal)
                    }
                }
            }
        }
    }
}

enum class CardType(val text: String, val shape: RoundedCornerShape, val color: Color) {
    From("from:", RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), LightGray01),
    To("to:", RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp), LightGray02)
}