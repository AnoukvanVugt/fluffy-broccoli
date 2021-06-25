package nl.avisi.anowalke.toolWindow

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import javax.swing.JLabel

class FluffyListener(val project: Project): ToolWindowManagerListener {

    private val chooseFiles = true
    private val chooseFolders = false
    private val chooseJars = false
    private val chooseJarsAsFiles = false
    private val chooseJarContents = false
    private val chooseMultiple = false

    override fun stateChanged() {

    }

    fun scanProject(currentClass: JLabel) {
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

}