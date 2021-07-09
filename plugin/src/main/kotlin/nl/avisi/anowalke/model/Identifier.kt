package nl.avisi.anowalke.model

data class Identifier(
    // Data class vs hashmap.. Makkelijker gebruik vs meer opslaan..
    val className: String,
    val expression: String

    // group by expresion. Expression met meerdere classnames.
)
