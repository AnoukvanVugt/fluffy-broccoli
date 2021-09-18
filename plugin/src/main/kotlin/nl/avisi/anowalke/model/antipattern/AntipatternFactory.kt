package nl.avisi.anowalke.model.antipattern

class AntipatternFactory {

    fun createAntipattern(antipattern: AntipatternName): Antipattern =
        when (antipattern) {
            AntipatternName.LAVA_FLOW -> LavaFlow()
        }
}
