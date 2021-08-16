package nl.avisi.anowalke.services

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import nl.avisi.anowalke.dto.ExpressionsDto
import nl.avisi.anowalke.dto.Translations
import nl.avisi.anowalke.util.configureObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Component
class VertaalService {

    val dummylist = listOf("subscription","key","region","endpoint","url","client","inputlist","request","body","media","type","gson","response","list","of","my","class","object","translations","it","post","json_text","parser","json","prettify","translator")

    private val subscriptionKey = "f2a9a9f29d7d4e878669cdfed04f99ce"
    private val subscriptionRegion = "westeurope"
    private val endpoint = "https://api-eur.cognitive.microsofttranslator.com/"
    var url = "$endpoint/translate?api-version=3.0&from=nl&to=en"

    private val restTemplate = RestTemplate().configureJsonMappers()

    private val log = KotlinLogging.logger { }

    val gson = Gson()

    fun requestTranslation(inputlist: ExpressionsDto): List<String> {
        try {
            log.warn { "POST $url" }
            val response = post(url, inputlist)
            val listOfMyClassObject = object : TypeToken<ArrayList<Translations?>?>() {}.type
            val translations: List<Translations> = gson.fromJson(response.body, listOfMyClassObject)
//            return translations.map { it.translations.first().expression }
        } catch(e: HttpStatusCodeException) {
            log.error { "Received ${e.statusCode.name} for $url: ${e.responseBodyAsString}" }
        }
        return dummylist
    }

    // This function performs a POST request.
//    @Throws(IOException::class)
//    fun post(inputlist: ExpressionsDto): List<String> {
//        val gson = Gson()
//        val body: RequestBody = inputlist.toString().toRequestBody(JSON)
//        val request: Request = Request.Builder()
//            .url(url).post(body)
//            .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
//            .addHeader("Ocp-Apim-Subscription-Region", subscriptionRegion)
//            .addHeader("Content-type", "application/json").build()
//        val response: Response = client.newCall(request).execute()
//        val listOfMyClassObject = object : TypeToken<ArrayList<Translations?>?>() {}.type
//        val translations: List<Translations> = gson.fromJson(response.body?.string(), listOfMyClassObject)
//        return translations.map { it.translations.first().expression }
//    }

    fun translate(expressions: ExpressionsDto): List<String> =
        requestTranslation(expressions)

    private fun <REQUEST> post(url: String, requestBody: REQUEST): ResponseEntity<String> {
        return restTemplate.postForEntity(url, HttpEntity(requestBody, getHeaders()), String::class.java)
    }

    private fun getHeaders() = HttpHeaders().apply {
        this.set("Ocp-Apim-Subscription-Key", subscriptionKey)
        this.set("Ocp-Apim-Subscription-Region", subscriptionRegion)
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