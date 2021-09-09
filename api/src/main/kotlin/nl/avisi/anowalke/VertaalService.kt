package nl.avisi.anowalke

import com.google.gson.Gson
import mu.KLogger
import mu.KotlinLogging
import nl.avisi.anowalke.dto.ExpressionsDto
import nl.avisi.anowalke.util.configureObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

interface VertaalService {

    val restTemplate: RestTemplate
        get() = RestTemplate().configureJsonMappers()

    val log: KLogger
        get() = KotlinLogging.logger { }

    val gson: Gson
        get() = Gson()

    fun translate(expressions: ExpressionsDto): List<String>

    fun <REQUEST> post(url: String, requestBody: REQUEST): ResponseEntity<String> {
        return restTemplate.postForEntity(url, HttpEntity(requestBody, getHeaders()), String::class.java)
    }

    fun getHeaders() = HttpHeaders().apply {
        this.contentType = org.springframework.http.MediaType.APPLICATION_JSON
    }

    private fun RestTemplate.configureJsonMappers() = this.apply {
        messageConverters.add(
            index = 0,
            element = MappingJackson2HttpMessageConverter().apply {
                objectMapper = configureObjectMapper()
            }
        )
    }
}
