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
        log.info("Initiate scan function.")
        val identifiers = scanProject()
        val stemmedIdentifiers: List<Identifier> = identifiers.map {
            Identifier(it.className, stemmer.stem(it.expression))
        }
        stemmedIdentifiers.forEach { log.info("Stemmed identifier: $it") }
        val results = findShotgunSurgery(stemmedIdentifiers)
        result.text = results.toString()
    }

    private fun getJavaFiles(file: VirtualFile): List<Identifier>? {
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
                    .map { expression -> Identifier(file.name, expression) }
            } else if (file.isDirectory) {
                log.info("File: ${file.name} is a directory. Check children for java files.")
                return file.children.flatMap { getJavaFiles(it).orEmpty() }
            }
        }
        return null
    }

    fun getFilesAsIdentifier(file: VirtualFile): List<Identifier>? {
        log.info("Initiate getFilesAsIdentifier function.")
        // Zoek naar files in de java map ipv files met filetype java, omdat hij sommige filetypes door elkaar haalt
        return if (file.isDirectory && file.name == "java") {
            log.info("Current file: '${file.name}' is a java directory.")
            val identifiers = getJavaFiles(file)
            log.info("getJavaFiles function retrieved ${identifiers?.size} identifiers.")
            identifiers
        } else if (file.isDirectory && file.name != "java") {
            log.info("Current file: ${file.name} is not a java directory. Check Children for a directory named 'java'.")
            file.children.flatMap { getFilesAsIdentifier(it).orEmpty() }
        } else {
            log.info("Current file: ${file.name} is not a directory. Ignore file.")
            null
        }
    }

    fun scanProject(): List<Identifier> {
        log.info("Initiate scanProject function.")
        val identifiers = ProjectRootManager.getInstance(project).contentSourceRoots.flatMap {
            getFilesAsIdentifier(it).orEmpty()
        }
        identifiers.forEach { log.info("Expression: ${it.expression}") }.also { log.info("Aloha") }
        return identifiers
    }

    fun findShotgunSurgery(identifiers: List<Identifier>): Map<String, List<Identifier>> {
        // Kijk naar overeenkomsten in expressies
        // vereiste 1: Wanneer een expressie overeenkomt met een classname of een deel van een classname
        // (kijk voor de gehele class name als het maar met een deel overeenkomt)
        // en de expressie daarna een hit oplevert negeer die dan.
        // vereiste 2: Iets met initialisatie, voor nu niet meenemen.

        val filteredIdentifiers = identifiers
            .groupBy { it.expression }
            .filter {
                it.value.size > 1
            }

        log.info(filteredIdentifiers.toString())
        return filteredIdentifiers
    }

}