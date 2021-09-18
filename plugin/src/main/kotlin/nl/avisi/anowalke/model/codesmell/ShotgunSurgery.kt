package nl.avisi.anowalke.model.codesmell

import nl.avisi.anowalke.model.method.NlpFindingMethod

class ShotgunSurgery: CodeSmell {
    override  val name = CodeSmellName.SHOTGUN_SURGERY
    override val urgency: Int
        get() = TODO("Not yet implemented")

    override val findingMethods = listOf(NlpFindingMethod())
}
