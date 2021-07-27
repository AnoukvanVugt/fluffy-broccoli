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
import javax.swing.JLabel


class FluffyListener(val project: Project) : ToolWindowManagerListener {

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

    fun scan() {
        scanProject()
    }

    private fun getJavaFiles(file: VirtualFile): List<String>? {
        // Filter out hidden files
        if (!file.isDirectory && !file.name.startsWith(".")) {
            PsiManager.getInstance(project).findFile(file)?.let { it -> log.warn(it.name) } ?: log.warn("no file found")
            val expressions = PsiManager.getInstance(project).findFile(file)?.let { psiFile -> psiService.extract(psiFile) }
//            log.warn("And now pretty")
//            expressions?.sanatise()?.forEach { log.warn(it) }
            return expressions
        } else {
            file.children.forEach { getJavaFiles(it) }
        }
        return null
    }

    private fun getFilesAsIdentifier(file: VirtualFile): List<Identifier>? {
        if (file.isDirectory) {
            // Zoek naar files in de java map ipv files met filetype java, omdat hij sommige filetypes door elkaar haalt
            if (file.name == "java") {
                return getJavaFiles(file)?.map { expression ->
                    Identifier(file.name, expression)
                }
            }
        } else {
            file.children.forEach { getFilesAsIdentifier(it) }
        }
        return null
    }

    private fun scanProject() {
        val identifiers = ProjectRootManager.getInstance(project).contentSourceRoots.flatMap {
            getFilesAsIdentifier(it).orEmpty()
        }
    }

}