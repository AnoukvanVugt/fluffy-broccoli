package nl.avisi.anowalke.services.translation

import com.google.gson.reflect.TypeToken
import nl.avisi.anowalke.VertaalService
import nl.avisi.anowalke.dto.ExpressionsDto
import nl.avisi.anowalke.dto.Translations
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException

@Service
@ConditionalOnProperty(name = ["translation.service"], havingValue = "microsoft")
class MicrosoftVertaalService: VertaalService {

    @Value("\${translation.microsoft.api_key}") lateinit var api_key: String
    @Value("\${translation.microsoft.url}") lateinit var api_url: String
    @Value("\${translation.microsoft.region}") lateinit var subscriptionRegion: String

    override fun translate(expressions: ExpressionsDto): List<String> {
        try {
            log.info { "POST $api_url" }
            val response = post(api_url, expressions)
            val typeReference = object : TypeToken<ArrayList<Translations?>?>() {}.type
            val translations: List<Translations> = gson.fromJson(response.body, typeReference)
            return translations.map { it.translations.first().expression }
        } catch (e: HttpStatusCodeException) {
            log.error { "Received ${e.statusCode.name} for $api_url: ${e.responseBodyAsString}" }
        }
        return emptyList()
    }

    override fun getHeaders() = org.springframework.http.HttpHeaders().apply {
        this.set("Ocp-Apim-Subscription-Key", api_key)
        this.set("Ocp-Apim-Subscription-Region", subscriptionRegion)
        this.contentType = org.springframework.http.MediaType.APPLICATION_JSON
    }
}
