package nl.avisi.anowalke

object Sanitiser {

    fun List<String>.sanatise(): List<String> =
        // Stap 1: Remove correct calls

        // Kan hier niet want dit gebeurt recursief?


        // Stap 2: Split expressies aan de hand van regex
        // Stap 3: Transform to lowercase, misschien al bij stap 2 erin
        // Stap 4: Remove duplicates
        this.split().toLowercase().distinct()
        // Stap 5: Remove irrelevant words -> pas in de api uitvoeren? Op basis van database? Eerste scan op basis van stopwoordenlijst?

        // save for api.

    private fun List<String>.split(): List<String> =
        this.flatMap { it.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()) }

    private fun List<String>.toLowercase(): List<String> =
        this.map { it.toLowerCase() }
}