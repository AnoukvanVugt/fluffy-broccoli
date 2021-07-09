package nl.avisi.anowalke;

import nl.avisi.anowalke.toolWindow.FluffyListener;
import nl.avisi.anowalke.toolWindow.FluffyToolWindowContent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FluffyToolWindow {

    private JPanel content;
    private JLabel currentClass;
    private JButton scanProject;

    public FluffyToolWindow(FluffyListener listener) {
        scanProject.addActionListener(e -> listener.scanProject(currentClass));
    }

    public JPanel getContent() {
        return content;
    }

    public void update(@NotNull FluffyToolWindowContent content) {

    }
}