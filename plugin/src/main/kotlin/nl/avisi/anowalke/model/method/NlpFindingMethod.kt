package nl.avisi.anowalke.model.method

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import nl.avisi.anowalke.model.Identifier
import nl.avisi.anowalke.service.PsiService
import nl.avisi.anowalke.service.Sanitiser
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import nl.avisi.anowalke.model.codesmell.CodeSmellName
import nl.avisi.anowalke.model.location.KeywordLocation
import nl.avisi.anowalke.model.location.Location
import nl.avisi.anowalke.service.Stemmer

class NlpFindingMethod : FindingMethod {

    private val psiService = PsiService()
    private val sanitiser = Sanitiser()
    private val stemmer = Stemmer()
    private val log = Logger.getInstance(this::class.java.name)

    override fun findLocation(project: Project, codeSmell: CodeSmellName): List<Location> {
        log.info("Initiate findLocation function in NlpFindingMethod.")
        val identifiers = findIdentifiers(project)
        identifiers.forEach { log.info("Stemmed identifier: $it") }
        return getCodeSmellLocations(identifiers, codeSmell)
    }

    private fun findIdentifiers(project: Project): List<Identifier> {
        log.info("Initiate scanProject function.")
        val identifiers = ProjectRootManager.getInstance(project).contentSourceRoots.flatMap {
            searchFilesRecursively(it, project).orEmpty()
        }
        identifiers.forEach { log.info("Expression: ${it.expression}") }.also { log.info("Aloha") }
        return identifiers
    }

    private fun searchFilesRecursively(file: VirtualFile, project: Project): List<Identifier>? {
        log.info("Initiate getFilesAsIdentifier function.")
        // Zoek naar files in de java map ipv files met filetype java, omdat hij sommige filetypes door elkaar haalt
        return if (file.isDirectory && file.name == "java") {
            log.info("Current file: '${file.name}' is a java directory.")
            val identifiers = getIdentifiersFromFile(file, project)
            log.info("getJavaFiles function retrieved ${identifiers?.size} identifiers.")
            identifiers
        } else if (file.isDirectory && file.name != "java") {
            log.info("Current file: ${file.name} is not a java directory. Check Children for a directory named 'java'.")
            file.children.flatMap { searchFilesRecursively(it, project).orEmpty() }
        } else {
            log.info("Current file: ${file.name} is not a directory. Ignore file.")
            null
        }
    }

    private fun getIdentifiersFromFile(file: VirtualFile, project: Project): List<Identifier>? {
        log.info("Initiate getJavaFiles function.")
        // Filter out hidden files
        if (!file.isDirectory && !file.name.startsWith(".")) {
            log.info("File: ${file.name} is not a directory nor a hidden file.")
            val expressions = PsiManager.getInstance(project).findFile(file)?.let { psiFile ->
                psiService.extract(psiFile)
            }
            if (expressions != null) {
                log.info("Extract function retrieved ${expressions.size} expressions.")
                return sanitiser
                    .sanitise(expressions)
                    .map { expression -> Identifier(file.name, stemmer.stem(expression)) }
            }
        } else if (file.isDirectory) {
            log.info("File: ${file.name} is a directory. Check children for java files.")
            return file.children.flatMap { getIdentifiersFromFile(it, project).orEmpty() }
        }
        return null
    }

    private fun getCodeSmellLocations(identifiers: List<Identifier>, codeSmell: CodeSmellName): List<Location> {
        var processedIdentifiers = identifiers
            .groupBy { it.expression }
        when (codeSmell) {
            CodeSmellName.SHOTGUN_SURGERY -> processedIdentifiers = processedIdentifiers
                .filter {
                    it.value.size > 1
                }
        }
        val listOfKeywordLocations = processedIdentifiers.map { mapOfKeywordAndIdentifier ->
            val keywordLocation = KeywordLocation()
            keywordLocation.keywoard = mapOfKeywordAndIdentifier.key
            keywordLocation.classesContainingKeyword = mapOfKeywordAndIdentifier.value
                .map { identifier ->
                    identifier.className
                }
            keywordLocation
        }
        log.info(processedIdentifiers.toString())
        listOfKeywordLocations.forEach { location -> log.info(location.toString()) }
        return listOfKeywordLocations
    }
}
