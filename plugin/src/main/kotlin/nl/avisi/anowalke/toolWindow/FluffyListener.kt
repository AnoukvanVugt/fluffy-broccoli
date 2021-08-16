package nl.avisi.anowalke.toolWindow

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.psi.PsiManager
import nl.avisi.anowalke.model.Identifier

import nl.avisi.anowalke.service.PsiService
import nl.avisi.anowalke.service.Sanitiser
import nl.avisi.anowalke.service.Stemmer
import javax.swing.JLabel
import javax.swing.JTextArea


class FluffyListener(val project: Project) : ToolWindowManagerListener {

    private val sanitiser = Sanitiser()
    private val stemmer = Stemmer()
    private val psiService = PsiService(this)
    private val log = Logger.getInstance(this::class.java.name)

    private val chooseFiles = true
    private val chooseFolders = true
    private val chooseJars = false
    private val chooseJarsAsFiles = false
    private val chooseJarContents = false
    private val chooseMultiple = false

    override fun stateChanged() {

    }

    fun showClassName(currentClass: JLabel) {
        val fileChooserDescriptor = FileChooserDescriptor(
            chooseFiles,
            chooseFolders,
            chooseJars,
            chooseJarsAsFiles,
            chooseJarContents,
            chooseMultiple
        )
        currentClass.text = FileChooser.chooseFiles(fileChooserDescriptor, project, null).first().name
    }

    fun scan(result: JTextArea) {
        log.warn("Initiate scan function.")
        val identifiers = scanProject()
        val stemmedIdentifiers: List<Identifier> = identifiers.map {
            Identifier(it.className, stemmer.stem(it.expression))
        }
        stemmedIdentifiers.forEach { log.warn("Stemmed identifier: $it") }
        val results = findShotgunSurgery(stemmedIdentifiers)
        result.text = results.toString()
    }

    private fun getJavaFiles(file: VirtualFile): List<Identifier>? {
        log.warn("Initiate getJavaFiles function.")
        // Filter out hidden files
        if (!file.isDirectory && !file.name.startsWith(".")) {
            log.warn("File: ${file.name} is not a directory nor a hidden file.")
            var expressions = PsiManager.getInstance(project).findFile(file)?.let { psiFile ->
                psiService.extract(psiFile)
            }
            if (expressions != null) {
                log.warn("Extract function retrieved ${expressions.size} expressions.")
                expressions = sanitiser.sanitise(expressions)
            }
            log.warn("Sanitise function retrieved ${expressions?.size} expressions.")
            return expressions?.map { expression ->
                Identifier(file.name, expression)
            }
        } else if (file.isDirectory) {
            log.warn("File: ${file.name} is a directory. Check children for java files.")
            return file.children.flatMap { getJavaFiles(it).orEmpty() }
        }
        return null
    }

    private fun getFilesAsIdentifier(file: VirtualFile): List<Identifier>? {
        log.warn("Initiate getFilesAsIdentifier function.")
        // Zoek naar files in de java map ipv files met filetype java, omdat hij sommige filetypes door elkaar haalt
        if (file.isDirectory && file.name == "java") {
            log.warn("Current file: '${file.name}' is a java directory.")
            val identifiers = getJavaFiles(file)
            log.warn("getJavaFiles function retrieved ${identifiers?.size} identifiers.")
            return identifiers
        } else if (file.isDirectory && file.name != "java") {
            log.warn("Current file: ${file.name} is not a java directory. Check Children for a directory named 'java'.")
            return file.children.flatMap { getFilesAsIdentifier(it).orEmpty() }
        } else {
            log.warn("Current file: ${file.name} is not a directory. Ignore file.")
        }
        return null
    }

    private fun scanProject(): List<Identifier> {
        log.warn("Initiate scanProject function.")
        val identifiers = ProjectRootManager.getInstance(project).contentSourceRoots.flatMap {
            getFilesAsIdentifier(it).orEmpty()
        }
        identifiers.forEach { log.warn("Expression: ${it.expression}") }.also { log.warn("Aloha") }
        return identifiers
    }

    private fun findShotgunSurgery(identifiers: List<Identifier>): Map<String, List<Identifier>> {
        // Kijk naar overeenkomsten in expressies
        // vereiste 1: Wanneer een expressie overeenkomt met een classname of een deel van een classname
        // (kijk voor de gehele class name als het maar met een deel overeenkomt)
        // en de expressie daarna een hit oplevert negeer die dan.
        // vereiste 2: Iets met initialisatie, voor nu niet meenemen.

        val testIdentifiers = listOf(
            Identifier("dier", "koe"),
            Identifier("dier", "pad"),
            Identifier("wegdek", "pad"),
            Identifier("meubel", "bank"),
            Identifier("instantie", "bank"),
            Identifier("meubel", "pad"),
        )
        val filteredIdentifiers = testIdentifiers
            .groupBy{ it.expression }
            .filter {
                it.value.size > 1
            }

        log.warn(filteredIdentifiers.toString())
        return filteredIdentifiers
    }

}