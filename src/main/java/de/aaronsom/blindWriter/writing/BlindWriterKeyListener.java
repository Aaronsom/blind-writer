package de.aaronsom.blindWriter.writing;

import de.aaronsom.blindWriter.file.FileSaver;
import de.aaronsom.blindWriter.sound.SoundManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A {@link KeyListener} for a {@link JTextArea}
 * This class intercepts all KeyEvents for the text area to prevent writing in the text area
 * except if the same {@link KeyEvent} has been triggered back to back.
 * When this happens, this class appends the KeyChar of the event to the text area.
 */
public class BlindWriterKeyListener implements KeyListener{
    /**
     * The FileSaver that keeps track of all changes
     */
    private FileSaver fileSaver;
    /**
     * The SoundManager that plays a sound for the key presses
     */
    private SoundManager soundManager;
    /**
     * The JTextArea into which is written by this
     */
    private JTextArea textArea;
    /**
     * The current state of the writing process
     */
    private WritingState writingState;
    /**
     * The KeyCode of the last KeyEvent that occured
     */
    private int lastKeyPress;

    /**
     * Constructs a new {@link BlindWriterKeyListener} for a {@link JTextArea} with a {@link FileSaver}
     * @param textArea the {@link JTextArea} for which to supervise the key presses
     * @param fileSaver the {@link FileSaver} that keeps track of the changes
     * @param soundManager the {@link SoundManager} that plays a sound for the key presses
     */
    public BlindWriterKeyListener(JTextArea textArea, FileSaver fileSaver, SoundManager soundManager){
        this.textArea = textArea;
        this.fileSaver = fileSaver;
        this.soundManager = soundManager;
        writingState = WritingState.NONE;
        lastKeyPress = 0;
    }

    /**
     * Consumes the {@link KeyEvent} so that the text area does not use it.
     * @param e the {@link KeyEvent} that occurred
     */
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    /**
     * Check if a key was selected and if the typed key matches the last typed key.
     * If this is the case, the typed key will be appended to textArea and fileSaver or if the
     * pressed key was Backspace, removes the last character of textArea and fileSaver.
     * Afterwards the state is reset to WrintingState.NONE
     *
     * If no key was selected and if the pressed key results in a valid Unicode character,
     * the state is set to WritingState.SELECTED and the last typed key is updated
     *
     * Makes soundManager play a sound according to the key pressed.
     *
     * Consumes the {@link KeyEvent} so that the text area does not use it.
     * @param e the {@link KeyEvent} that occurred
     */
    public void keyPressed(KeyEvent e) {
        if(writingState == WritingState.SELECTED && lastKeyPress == e.getKeyCode()){
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                SwingUtilities.invokeLater(new RemoveAction());
            } else {
                SwingUtilities.invokeLater(new AppendAction(String.valueOf(e.getKeyChar())));
            }
            writingState = WritingState.NONE;
        } else if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED){
            lastKeyPress = e.getKeyCode();
            writingState = WritingState.SELECTED;
        }
        triggerSoundManager(String.valueOf(e.getKeyChar()));
        e.consume();
    }

    public void keyReleased(KeyEvent e) {
        //do nothing
    }

    /**
     * Makes soundManger play a sound for the pressed key
     * @param key the key for which a sound is to be played
     */
    private void triggerSoundManager(String key){
        soundManager.play(key);
    }

    /**
     * Runnable to append a String to the end of the JTextArea textArea
     */
    private class AppendAction implements Runnable{
        String toAppend;

        /**
         * Constructs a new AppendAction with the String to append to textArea and fileSaver
         * @param toAppend the String to append to textArea
         */
        AppendAction(String toAppend) {
            this.toAppend = toAppend;
        }

        /**
         * Appends the String to the end of textArea and fileSaver
         */
        @Override
        public void run() {
            textArea.append(toAppend);
            fileSaver.append(toAppend);
        }
    }

    /**
     * Runnable to remove the last character of textArea and fileSaver
     */
    private class RemoveAction implements Runnable{

        /**
         * Removes the last character of textArea and fileSaver
         */
        @Override
        public void run() {
            int documentLength = textArea.getDocument().getLength();
            textArea.replaceRange("",documentLength-1, documentLength);
            fileSaver.remove(1);
        }
    }
}
