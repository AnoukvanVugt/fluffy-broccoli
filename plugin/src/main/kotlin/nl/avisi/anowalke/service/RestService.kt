package nl.avisi.anowalke.service

import com.google.gson.Gson
import com.intellij.openapi.diagnostic.Logger
import nl.avisi.anowalke.dto.ExpressionsDto
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class RestService {

    private val apiUrl = "http://localhost:8080"

    private val log = Logger.getInstance(this::class.java.name)

    // Instantiates the OkHttpClient.
    var client: OkHttpClient = OkHttpClient()

    val gson = Gson()
    val JSON: MediaType? = ("application/json; charset=utf-8").toMediaTypeOrNull()


    @Throws(IOException::class)
    fun makeApiCall(expressions: ExpressionsDto, url: String): List<String> {
        val requestBody = gson.toJson(expressions)
        log.warn(requestBody)
        val body: RequestBody = requestBody.toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url).post(body)
            .addHeader("Content-type", "application/json").build()
        val response: Response = client.newCall(request).execute()
        log.warn("Response received with status: ${response.code}")
        val responseList = gson.fromJson(response.body?.string(), List::class.java)
        return responseList.map { it.toString() }
    }

    fun removeIrrelevantWords(expressions: ExpressionsDto): List<String> {
        val url = "$apiUrl/filter"
        return makeApiCall(expressions, url)
    }

    fun translateExpressions(expressions: ExpressionsDto): List<String> {
        val url = "$apiUrl/translate"
        log.warn("Make api call to: $url")
        return makeApiCall(expressions, url)
    }
}