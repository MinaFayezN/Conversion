package dev.mina.conversion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mina.conversion.ui.theme.LightGray02

@Preview
@Composable
fun HistoricItemSample() {
    HistoricItem(date = "2022-11-21",
        base = "USD",
        baseValue = "1.0",
        target = "EGP",
        convertedValue = " 24.5")
}

@Composable
fun HistoricItem(
    date: String,
    base: String,
    baseValue: String,
    target: String,
    convertedValue: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(20.dp), color = LightGray02)
            .border(0.5.dp,
                MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                RoundedCornerShape(20.dp)),
    ) {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            text = date,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1F),
                text = "$base\n$baseValue",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
            )
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = dev.mina.conversion.R.drawable.ic_arrow),
                contentDescription = "$base to $target")
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1F),
                text = "$target\n$convertedValue",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
            )
        }

    }
}
