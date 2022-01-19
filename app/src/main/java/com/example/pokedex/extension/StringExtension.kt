package com.example.pokedex.extension

fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar(Char::uppercase)
}
