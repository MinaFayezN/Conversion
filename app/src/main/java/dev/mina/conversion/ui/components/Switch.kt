package dev.mina.conversion.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.mina.conversion.R
import dev.mina.conversion.ui.theme.LightBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SwitchComponents(
    isLoading: Boolean,
    rate: Double,
    onSwitchCLick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.exchange))
    val conversionRate by remember { mutableStateOf(rate) } //Loading
    var isPlaying by remember { mutableStateOf(isLoading) } //Loading
    val scope = rememberCoroutineScope() // to be removed after adding actual loading
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = CenterVertically) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(42.dp)
                .background(color = Color.White, shape = RoundedCornerShape(50))
                .border(border = BorderStroke(1.dp, LightBlue), shape = RoundedCornerShape(50))
                .clickable {
                    onSwitchCLick.invoke()
                    scope.launch {
                        //Simulating Loading
                        isPlaying = true
                        delay(2000)
                        isPlaying = false
                    }
                }
                .padding(4.dp),
            isPlaying = isPlaying,
            restartOnPlay = true,
        )
        Text(
            text = "Rate = $conversionRate",
            modifier = Modifier
                .wrapContentSize()
                .background(color = Color.White, shape = RoundedCornerShape(50))
                .border(border = BorderStroke(1.dp, LightBlue), shape = RoundedCornerShape(50))
                .padding(8.dp),
            fontSize = 16.sp,
            color = Color.Black.copy(alpha = 0.65F),
        )
    }
}