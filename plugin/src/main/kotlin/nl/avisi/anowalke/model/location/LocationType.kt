package nl.avisi.anowalke.model.location

enum class LocationType(val type: String) {
    KEYWORD_LOCATION("Keyword Location");

    companion object {
        fun fromText(text: String): LocationType? =
            values().find { it.type == text }
    }
}
