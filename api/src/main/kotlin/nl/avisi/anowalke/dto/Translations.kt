package nl.avisi.anowalke.dto

data class Translations(
    val translations: List<Translation>
)

data class Translation(
    val expression: String,
    val language: String
)
