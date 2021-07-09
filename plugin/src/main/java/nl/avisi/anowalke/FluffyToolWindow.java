package nl.avisi.anowalke;

import nl.avisi.anowalke.toolWindow.FluffyListener;
import nl.avisi.anowalke.toolWindow.FluffyToolWindowContent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FluffyToolWindow {

    private JPanel content;
    private JLabel currentClass;
    private JButton scan;

    public FluffyToolWindow(FluffyListener listener) {
        scan.addActionListener(e -> listener.scan());
    }

    public JPanel getContent() {
        return content;
    }

    public void update(@NotNull FluffyToolWindowContent content) {

    }
}