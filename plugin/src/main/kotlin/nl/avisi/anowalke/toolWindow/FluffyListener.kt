package nl.avisi.anowalke.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import nl.avisi.anowalke.model.antipattern.Antipattern
import nl.avisi.anowalke.model.antipattern.AntipatternFactory
import nl.avisi.anowalke.model.antipattern.AntipatternName
import nl.avisi.anowalke.model.codesmell.CodeSmell
import nl.avisi.anowalke.model.codesmell.CodeSmellFactory
import nl.avisi.anowalke.model.location.KeywordLocation
import nl.avisi.anowalke.model.location.Location
import java.awt.Color

import javax.swing.JTextArea


class FluffyListener(val project: Project) : ToolWindowManagerListener {

    private val antipatternFactory = AntipatternFactory()
    private val codeSmellFactory = CodeSmellFactory()

    private lateinit var antipatternName: AntipatternName

    private val log = Logger.getInstance(this::class.java.name)

    override fun stateChanged() {}

    fun setAntipatternName(isSelected: Boolean, antipattern: String) {
        log.info("SetAntipatternName: $isSelected, $antipattern")
        if(isSelected) {
            antipatternName = AntipatternName.fromText(antipattern)!!
        }
    }

    fun checkAntipatternSelected(result: JTextArea, antipatternSelected: Boolean) {
        log.info("checkAntipatternSelected: $antipatternSelected")
        if(antipatternSelected) {
            result.text = scan()
            result.foreground = Color.BLACK
        } else {
            result.text = "Please select an antipattern for scanning."
            result.foreground = Color.RED
        }
    }

    private fun scan(): String {
        log.info("Initiate scan function")
        log.info(antipatternName.toString())
        when (antipatternName) {
            AntipatternName.LAVA_FLOW -> return searchForLavaFlow()
        }
    }

    private fun searchForLavaFlow(): String {
        val createdAntipattern: Antipattern = antipatternFactory.createAntipattern(antipatternName)
        val codeSmells: List<CodeSmell> = createdAntipattern.recognizableBy
            .map { codeSmellName -> codeSmellFactory.createCodeSmell(codeSmellName) }
        val locations: List<Location> = codeSmells
            .flatMap { codeSmell -> codeSmell.find(project, codeSmell.findingMethods) }
        return createResult(createdAntipattern, locations)
    }

    private fun createResult(antipattern: Antipattern, locations: List<Location>): String {
        log.info("initiate createResult function")
        val antipatternName: String = antipattern.name.text
        val found: String = when (locations.size) {
            0 -> "No"
            else -> "Possibly"
        }
        val basedOn: String = antipattern.recognizableBy.map { codeSmellName ->
            codeSmellName.text
        }.toString()
        log.info(locations.toString())
        val prettyLocation: List<String> = locations.map { location ->
            when (location) {
                is KeywordLocation ->
                    "Keyword: ${location.keywoard}. " +
                            "Found in classes: ${location.classesContainingKeyword}. ".also { log.info("Keywordlocation") }
                else -> "error: locationtype not found."
            }
        }
        return "Name: $antipatternName. " +
                "Found: $found. " +
                "Based on code smells: $basedOn. " +
                "Locations: $prettyLocation."
    }
}