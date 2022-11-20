package dev.mina.conversion.ui

import kotlin.math.pow
import kotlin.math.roundToInt


internal fun Double.roundDecimalPlaces(places: Int) =
    (this * 1.0.pow(places)).roundToInt() / 1.0.pow(places)

internal fun <T> MutableList<T>.moveItemToTop(item: T) = this.apply {
    remove(item)
    add(0, item)
}