package nl.avisi.anowalke.service

import com.intellij.openapi.diagnostic.Logger
import nl.avisi.anowalke.dto.ExpressionsDto

class Sanitiser {

    private val restService = RestService()

    private var camelCaseTitleCaseRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()

    private val log = Logger.getInstance(this::class.java.name)

    fun sanitise(expressions: List<String>): List<String> =
        // Stap 1: Remove correct calls

        // Kan hier niet want dit gebeurt recursief?


        // Stap 2: Split expressies aan de hand van regex
        // Stap 3: Transform to lowercase, misschien al bij stap 2 erin
        // Stap 4: Remove duplicates
        // Stap 5: Remove irrelevant words -> pas in de api uitvoeren? Op basis van database? Eerste scan op basis van stopwoordenlijst?
        expressions.split().toLowercase().distinct().translate().also { log.warn("Initiate sanitise function.") }

    private fun List<String>.split(): List<String> =
        this.flatMap { it.split(camelCaseTitleCaseRegex) }

    private fun List<String>.toLowercase(): List<String> =
        this.map { it.toLowerCase() }

    private fun List<String>.translate(): List<String> =
        restService.translateExpressions(ExpressionsDto(this)).also { log.warn("Initiate translate function.") }

    private fun List<String>.removeIrrelevantWords(): List<String> =
        restService.removeIrrelevantWords(ExpressionsDto(this))
}