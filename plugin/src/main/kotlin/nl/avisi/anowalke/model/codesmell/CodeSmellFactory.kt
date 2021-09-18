package nl.avisi.anowalke.model.codesmell

class CodeSmellFactory {

    fun createCodeSmell(codeSmell: CodeSmellName): CodeSmell =
        when (codeSmell) {
            CodeSmellName.SHOTGUN_SURGERY -> ShotgunSurgery()
        }
}
