package nl.avisi.anowalke.service

import com.intellij.openapi.diagnostic.Logger
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.core.StopAnalyzer
import org.apache.lucene.analysis.en.PorterStemFilter
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import java.util.ArrayList

class Stemmer {

    private val log = Logger.getInstance(this::class.java.name)

    fun stem(term: String): String {
        log.info("Initiate stem function.")
        val analyzer: Analyzer = StandardAnalyzer()
        var result: TokenStream = analyzer.tokenStream(null, term)
        result = PorterStemFilter(result)
        result = StopFilter(result, StopAnalyzer.ENGLISH_STOP_WORDS_SET)
        val resultAttr: CharTermAttribute = result.addAttribute(CharTermAttribute::class.java)
        val tokens: MutableList<String> = ArrayList()
        try {
            result.reset()
            while (result.incrementToken()) {
                tokens.add(resultAttr.toString())
            }
        } catch (e: Exception) {
            log.error(e.message)
        }
        return tokens.toString()
    }
}