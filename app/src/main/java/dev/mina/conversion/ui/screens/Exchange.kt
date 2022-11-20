package dev.mina.conversion.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.mina.conversion.R.raw.exchange
import dev.mina.conversion.ui.components.CardSample
import dev.mina.conversion.ui.theme.LightBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun ExchangeScreen() {
    Box(Modifier.wrapContentSize(Alignment.Center)) {
        CardSample()
        Text(
            text = "rate: 123.22",
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .background(color = Color.White, shape = RoundedCornerShape(50))
                .border(border = BorderStroke(1.dp, LightBlue), shape = RoundedCornerShape(50))
                .padding(8.dp),
            fontSize = 16.sp,
            color = Color.Black.copy(alpha = 0.65F),
        )
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(exchange))
        var isPlaying by remember { mutableStateOf(false) } //Loading
        val scope = rememberCoroutineScope()
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(42.dp)
                .align(Alignment.CenterStart)
                .background(color = Color.White, shape = RoundedCornerShape(50))
                .border(border = BorderStroke(1.dp, LightBlue), shape = RoundedCornerShape(50))
                .clickable {
                    scope.launch {
                        //Simulating Loading
                        isPlaying = true
                        delay(2000)
                        isPlaying = false
                    }
                }.padding(4.dp),
            isPlaying = isPlaying,
            restartOnPlay = true,
            )

    }
}