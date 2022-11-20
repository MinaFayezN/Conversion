package dev.mina.conversion.ui

internal fun <T> MutableList<T>.moveItemToTop(item: T) = this.apply {
    remove(item)
    add(0, item)
}