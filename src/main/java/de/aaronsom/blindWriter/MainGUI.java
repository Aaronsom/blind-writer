package de.aaronsom.blindWriter;

import de.aaronsom.blindWriter.file.FileSaver;
import de.aaronsom.blindWriter.file.TxtFileSaver;
import de.aaronsom.blindWriter.sound.SoundManager;
import de.aaronsom.blindWriter.writing.BlindWriterKeyListener;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.text.Paragraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The main window of this application.
 */
public class MainGUI extends JFrame{

    private Container contentPane;
    private JScrollPane scrollPane;
    private JTextArea documentTextArea;
    private JToolBar toolBar;
    private JButton openFileButton;
    private JButton saveFileButton;

    private SoundManager soundManager;
    private FileSaver fileSaver;
    /**
     * Constructs the main window and the toolbar
     */
    public MainGUI(){
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        soundManager = new SoundManager();

        setupToolbar();

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
     * Sets up the toolbar with the 'Save' and 'Open Document' buttons.
     */
    private void setupToolbar(){
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        contentPane.add(toolBar, BorderLayout.PAGE_START);

        openFileButton = new JButton("Dokument öffnen");
        openFileButton.addActionListener(this::openDocumentAction);
        toolBar.add(openFileButton);

        saveFileButton = new JButton("Speichern");
        saveFileButton.addActionListener(this::saveDocumentAction);
        toolBar.add(saveFileButton);
    }

    /**
     * Sets up documentTextArea for the selected file.
     * Enables line wrap by words and adds the {@link BlindWriterKeyListener} to it.
     * At the end, the JScrollPane for the text area is set up
     * @param file the file on which the text area is based and for which changes are stored
     */
    private void setupDocumentTextArea(File file){
        documentTextArea = new JTextArea();
        documentTextArea.setLineWrap(true);
        documentTextArea.setWrapStyleWord(true);
        documentTextArea.addKeyListener(new BlindWriterKeyListener(documentTextArea, fileSaver, soundManager));

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while (true){
                line = reader.readLine();
                if(line != null) documentTextArea.append(line+"\n");
                else break;
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        setupScollPane();
    }

    /**
     * Sets up scrollPane.
     * Adds documentTextArea to it and then add it to contentPane, packs the JFrame and maximises it
     */
    private void setupScollPane(){
        scrollPane = new JScrollPane(documentTextArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        pack();
        setExtendedState(MAXIMIZED_BOTH);
    }

    /**
     * Sets up the window.
     * Adds a WindowListener that asks for confirmation if unsaved changes would get lost by closing.
     * Sets the size of the window to the maximum screen size and makes the window visible.
     */
    private void setupWindow(){
        setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(fileSaver != null && fileSaver.hasUnsavedChanges()){

                    int confirmResult = JOptionPane.showConfirmDialog(MainGUI.this,
                                                                      "Änderungen sind noch nicht gespeichert und gehen beim Schließen verloren. Wirklich schließen?",
                                                                      "Wirklich schließen?", JOptionPane.YES_NO_OPTION);
                    if(confirmResult == JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }

        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    /**
     * When triggered, a file selection dialog is created and documentTextArea is set up with the file.
     * @param e
     */
    private void openDocumentAction(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
                "txt Dokumente", "txt");
        chooser.setFileFilter(extensionFilter);
        int chooseResult = chooser.showOpenDialog(MainGUI.this);
        if (chooseResult == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            fileSaver = new TxtFileSaver(file);
            setupDocumentTextArea(file);
        }
    }

    /**
     * When triggered, changes to the open file are saved
     */
    private void saveDocumentAction(ActionEvent e) {
        if (fileSaver != null) {
            fileSaver.save();
        }
    }
}

