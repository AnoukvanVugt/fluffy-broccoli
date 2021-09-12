package nl.avisi.anowalke.model.antipattern

import nl.avisi.anowalke.model.codesmell.CodeSmellName

interface Antipattern {
    val name: AntipatternName
    val recognizableBy: List<CodeSmellName>
    val urgency: Int
}
