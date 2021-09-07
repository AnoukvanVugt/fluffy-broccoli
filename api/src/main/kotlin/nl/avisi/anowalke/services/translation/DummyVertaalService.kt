package nl.avisi.anowalke.services.translation

import nl.avisi.anowalke.TranslateConnector
import nl.avisi.anowalke.dto.ExpressionsDto
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["translation.service"], havingValue = "dummy")
class DummyVertaalService: TranslateConnector {
    override val api_key = null
    override val api_url = null

    private val dummyList = listOf("subscription","key","region","endpoint","url","client","inputlist","request","body","media","type","gson","response","list","of","my","class","object","translations","it","post","json_text","parser","json","prettify","translator")

    override fun translate(expressions: ExpressionsDto): List<String> = dummyList
}
