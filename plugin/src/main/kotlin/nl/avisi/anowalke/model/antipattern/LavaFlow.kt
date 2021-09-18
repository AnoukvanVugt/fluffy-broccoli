package nl.avisi.anowalke.model.antipattern

import nl.avisi.anowalke.model.codesmell.CodeSmellName

class LavaFlow : Antipattern {
    override val name = AntipatternName.LAVA_FLOW
    override val recognizableBy = listOf(CodeSmellName.SHOTGUN_SURGERY)

    override val urgency: Int
        get() = TODO("Not yet implemented")
}
