package nl.avisi.anowalke.model.codesmell

import com.intellij.openapi.project.Project
import nl.avisi.anowalke.model.location.Location
import nl.avisi.anowalke.model.method.FindingMethod

interface CodeSmell {
    val name: CodeSmellName
    val urgency: Int
    val findingMethods: List<FindingMethod>

    fun find(project: Project, findingMethods: List<FindingMethod>): List<Location> =
        findingMethods.flatMap { method ->
            method.findLocation(project, name)
        }
}
