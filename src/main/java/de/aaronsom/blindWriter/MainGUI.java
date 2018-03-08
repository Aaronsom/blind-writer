package de.aaronsom.blindWriter;

import de.aaronsom.blindWriter.writing.BlindWriterKeyListener;

import javax.swing.*;
import java.awt.*;

/**
 * The main window of this application.
 */
public class MainGUI extends JFrame{

    private Container contentPane;
    private JScrollPane scrollPane;
    private JTextArea documentTextArea;


    /**
     * Constructs the main window and its components.
     */
    public MainGUI(){
        contentPane = getContentPane();

        setupDocumentTextArea();

        setupScollPane();

        setupWindow();
    }

    /**
     * Creates a new {@link MainGUI} to start the program.
     * @param args not used
     */
    public static void main(String args[]){
        new MainGUI();
    }

    /**
     * Sets up documentTextArea.
     * Enables line wrap by words and adds the {@link BlindWriterKeyListener} to it.
     */
    private void setupDocumentTextArea(){
        documentTextArea = new JTextArea();
        documentTextArea.setLineWrap(true);
        documentTextArea.setWrapStyleWord(true);
        documentTextArea.addKeyListener(new BlindWriterKeyListener(documentTextArea));
    }

    /**
     * Sets up scrollPane.
     * Adds documentTextArea to it and then add it to contentPane.
     */
    private void setupScollPane(){
        scrollPane = new JScrollPane(documentTextArea);
        contentPane.add(scrollPane);
    }

    /**
     * Sets up the window.
     * Sets the size of the window to the maximum screen size and makes the window visible.
     */
    private void setupWindow(){
        setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }
}

