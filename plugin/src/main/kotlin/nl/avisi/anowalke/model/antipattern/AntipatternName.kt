package nl.avisi.anowalke.model.antipattern

enum class AntipatternName(val text: String) {
    LAVA_FLOW("Lava Flow");

    companion object {
        fun fromText(text: String): AntipatternName? =
            values().find { it.text == text }
    }
}
