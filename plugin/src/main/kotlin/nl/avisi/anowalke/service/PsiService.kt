package nl.avisi.anowalke.service

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.collectDescendantsOfType
import nl.avisi.anowalke.toolWindow.FluffyListener

class PsiService(fluffyListener: FluffyListener) {

    private val log = Logger.getInstance(this::class.java.name)

    fun extract(psiFile: PsiFile): List<String> =
        psiFile.collectDescendantsOfType<PsiNameIdentifierOwner>()
            .mapNotNull { it.name }
            .also { log.info("Initiate extract function.") }
}