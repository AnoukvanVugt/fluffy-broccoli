package nl.avisi.anowalke.dto

data class TranslationsDto(
    val translations: List<TranslationDto>
)

data class TranslationDto(
    val expression: String,
    val language: String
)
