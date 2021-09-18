package nl.avisi.anowalke

import nl.avisi.anowalke.service.Stemmer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StemmerTest {

    private val stemmer = Stemmer()

    @Test
    fun stemTestEndsWith() {
        val testList = listOf("birthday", "monday", "holliday", "yesterday")

        val result = testList.map { stemmer.stem(it) }
        println(testList)
        println(result)

        Assertions.assertEquals(testList.distinct().size, result.distinct().size)
    }

    @Test
    fun stemTestStartsWith() {
        val testList = listOf("coffee", "coffeepot", "coffeemaker")

        val result = testList.map { stemmer.stem(it) }

        Assertions.assertEquals(testList.distinct().size, result.distinct().size)
        //Shows that searching on collections based on words is not possible through stemming
        //Because the stemmer cuts off the -e
        Assertions.assertFalse(result[0].contains("coffee"))
        Assertions.assertTrue(result[1].contains("coffee"))
        Assertions.assertTrue(result[2].contains("coffee"))
    }

    @Test
    fun stemTestOddOneOut() {
        val testList = listOf("chair", "armchair", "wheelchair", "chairlift")

        val result = testList.map { stemmer.stem(it) }
        println(testList)
        println(result)

        Assertions.assertEquals(testList.size, result.size)
        Assertions.assertTrue(result[0].contains("chair"))
        Assertions.assertTrue(result[1].contains("chair"))
        Assertions.assertTrue(result[2].contains("chair"))
        //Shows that searching on collections based on words is not possible through stemming
        //Because the stemmer would list chairlift as being a 'chair'
        Assertions.assertTrue(result[3].contains("chair"))
    }

}