package nl.avisi.anowalke.service

import com.intellij.openapi.diagnostic.Logger
import nl.avisi.anowalke.dto.ExpressionsDto

class Sanitiser {

    private val restService = RestService()

    private var camelCaseTitleCaseRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()

    private val log = Logger.getInstance(this::class.java.name)

    fun sanitise(expressions: List<String>): List<String> =
        expressions
            .split()
            .toLowercase()
            .distinct()
            .translate()
            .also { log.info("Initiate sanitise function.") }

    private fun List<String>.split(): List<String> =
        this.flatMap { it.split(camelCaseTitleCaseRegex) }

    private fun List<String>.toLowercase(): List<String> =
        this.map { it.toLowerCase() }

    private fun List<String>.translate(): List<String> =
        restService.translateExpressions(ExpressionsDto(this))
            .also { log.info("Initiate translate function.") }

    private fun List<String>.removeIrrelevantWords(): List<String> =
        restService.removeIrrelevantWords(ExpressionsDto(this))
}