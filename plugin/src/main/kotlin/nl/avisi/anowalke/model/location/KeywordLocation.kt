package nl.avisi.anowalke.model.location

class KeywordLocation: Location {
    override val type = LocationType.KEYWORD_LOCATION
    var keywoard = ""
    var classesContainingKeyword = emptyList<String>()
}