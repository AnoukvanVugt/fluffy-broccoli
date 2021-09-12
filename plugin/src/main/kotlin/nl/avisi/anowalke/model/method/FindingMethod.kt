package nl.avisi.anowalke.model.method

import com.intellij.openapi.project.Project
import nl.avisi.anowalke.model.codesmell.CodeSmellName
import nl.avisi.anowalke.model.location.Location

interface FindingMethod {

    fun findLocation(project: Project, codeSmell: CodeSmellName): List<Location>
}