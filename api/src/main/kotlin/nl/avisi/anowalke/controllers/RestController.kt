package nl.avisi.anowalke.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import mu.KotlinLogging
import nl.avisi.anowalke.VertaalService
import nl.avisi.anowalke.dto.ExpressionsDto
import nl.avisi.anowalke.services.FilterService

@RestController
@RequestMapping("/")
class RestController(
    private val vertaalService: VertaalService,
    private val filterService: FilterService
) {

    private val log = KotlinLogging.logger { }

    @PostMapping(value = ["/translate"])
    fun translate(@RequestBody expressions: ExpressionsDto): List<String> {
        log.info("Translate request received.")
        log.info("Requestbody: $expressions")
        val response = vertaalService.translate(expressions)
        log.info("Response received: $response")
        return response
    }

    @PostMapping(value = ["/filter"])
    fun filterIrrelevantWords(@RequestBody expressions: ExpressionsDto): List<String> {
        log.info("Filter request received.")
        return filterService.removeIrrelevantWords()
    }
}