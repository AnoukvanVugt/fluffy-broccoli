package nl.avisi.anowalke.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import nl.avisi.anowalke.FluffyToolWindow

class FluffyToolWindowFactory : ToolWindowFactory {

    private lateinit var fluffyToolWindow: FluffyToolWindow
    private lateinit var fluffyListener: FluffyListener

    private val displayName = ""
    private val isLockable = false

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        fluffyListener = FluffyListener(project)
        fluffyToolWindow = FluffyToolWindow(fluffyListener)
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(fluffyToolWindow.content, displayName, isLockable)
        toolWindow.contentManager.addContent(content)
    }

}