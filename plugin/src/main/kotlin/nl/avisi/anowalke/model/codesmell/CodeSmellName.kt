package nl.avisi.anowalke.model.codesmell

enum class CodeSmellName(val text: String) {
    SHOTGUN_SURGERY("Shotgun Surgery");

    companion object {
        fun fromText(text: String): CodeSmellName? =
            values().find { it.text == text }
    }
}
