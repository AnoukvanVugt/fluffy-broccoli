package nl.avisi.anowalke;

import nl.avisi.anowalke.toolWindow.FluffyListener;
import nl.avisi.anowalke.toolWindow.FluffyToolWindowContent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FluffyToolWindow {

    private JPanel content;
    private JButton scan;
    private JTextArea result;
    private JRadioButton lavaFlowRadioButton;

    public FluffyToolWindow(FluffyListener listener) {
        lavaFlowRadioButton.addActionListener(e ->
                listener.setAntipatternName(lavaFlowRadioButton.isSelected(), lavaFlowRadioButton.getText()));
        scan.addActionListener(e ->
                listener.checkAntipatternSelected(result, lavaFlowRadioButton.isSelected()));
    }

    public JPanel getContent() {
        return content;
    }

    public void update(@NotNull FluffyToolWindowContent content) {

    }
}