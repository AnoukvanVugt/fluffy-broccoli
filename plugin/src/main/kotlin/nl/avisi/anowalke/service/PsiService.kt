package nl.avisi.anowalke.service

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.forEachDescendantOfType
import nl.avisi.anowalke.toolWindow.FluffyListener

class PsiService(fluffyListener: FluffyListener) {

    private val log = Logger.getInstance(this::class.java.name)

    fun extract(psiFile: PsiFile): List<String> {
        val expressions = ArrayList<String>()

        psiFile.forEachDescendantOfType<PsiNameIdentifierOwner> { it ->
            it.name?.let { expressions.add(it) }
        }
        expressions.forEach { log.warn(it) }
        return expressions
    }
}