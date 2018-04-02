package de.aaronsom.blindWriter.writing;

import de.aaronsom.blindWriter.file.FileSaver;
import de.aaronsom.blindWriter.sound.SoundManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class BlindWriterKeyListenerTest {

    JTextArea testTextArea;
    BlindWriterKeyListener blindWriterKeyListener;
    FileSaver fileSaver;
    SoundManager soundManager;
    @BeforeEach
    void init(){
        testTextArea = new JTextArea();
        fileSaver = mock(FileSaver.class);
        soundManager = mock(SoundManager.class);
        blindWriterKeyListener = new BlindWriterKeyListener(testTextArea, fileSaver, soundManager);
    }

    @Test
    void keysPressedOnceEach() throws InterruptedException {
        KeyEvent keyEventA= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'a');
        KeyEvent keyEventB= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_B, 'b');
        KeyEvent keyEventBackspace= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_BACK_SPACE, '\u0008');
        KeyEvent keyEventEnter= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, '\u2386');
        KeyEvent keyEventSpace= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_SPACE, '\u0020');
        blindWriterKeyListener.keyPressed(keyEventA);
        blindWriterKeyListener.keyPressed(keyEventB);
        blindWriterKeyListener.keyPressed(keyEventBackspace);
        blindWriterKeyListener.keyPressed(keyEventSpace);
        blindWriterKeyListener.keyPressed(keyEventEnter);


        Thread.sleep(100);
        assertEquals("", testTextArea.getText(), "Pressing 'a' and 'b' results in no text");
        verify(fileSaver, never()).append(anyString());
        verify(fileSaver, never()).remove(anyInt());
    }

    @Test
    void normalKeyPressedTwice() throws InterruptedException {
        KeyEvent keyEventA= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'a');
        KeyEvent keyEventARelease= new KeyEvent(
                testTextArea, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_A, 'a');
        blindWriterKeyListener.keyPressed(keyEventA);
        blindWriterKeyListener.keyReleased(keyEventARelease);
        blindWriterKeyListener.keyPressed(keyEventA);
        blindWriterKeyListener.keyReleased(keyEventARelease);
        Thread.sleep(100);
        assertEquals("a", testTextArea.getText(), "Pressing 'a' twice results in 'a'");
        verify(fileSaver).append("a");
    }

    @Test
    void enterKeyPressedTwice() throws InterruptedException {
        int lineCount = testTextArea.getLineCount();
        KeyEvent keyEventEnter= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, '\n');
        KeyEvent keyEventEnterRelease= new KeyEvent(
                testTextArea, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_ENTER, '\n');
        blindWriterKeyListener.keyPressed(keyEventEnter);
        blindWriterKeyListener.keyReleased(keyEventEnterRelease);
        blindWriterKeyListener.keyPressed(keyEventEnter);
        blindWriterKeyListener.keyReleased(keyEventEnterRelease);
        Thread.sleep(100);
        assertEquals(lineCount+1, testTextArea.getLineCount(), "Pressing Enter twice results in a new line");
        verify(fileSaver).append("\n");
    }

    @Test
    void backspaceKeyPressedTwice() throws InterruptedException {
        testTextArea.append("aa");
        KeyEvent keyEventBackspace= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_BACK_SPACE, '\u0008');
        KeyEvent keyEventBackspaceRelease= new KeyEvent(
                testTextArea, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_BACK_SPACE, '\u0008');
        blindWriterKeyListener.keyPressed(keyEventBackspace);
        blindWriterKeyListener.keyReleased(keyEventBackspaceRelease);
        blindWriterKeyListener.keyPressed(keyEventBackspace);
        blindWriterKeyListener.keyReleased(keyEventBackspaceRelease);
        Thread.sleep(100);
        assertEquals("a", testTextArea.getText(), "Pressing Backspace twice removes one character");
        verify(fileSaver).remove(1);
    }

    @Test
    void charUndefinedKeyPressedTwice() throws InterruptedException {
        KeyEvent keyEventControle= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        KeyEvent keyEventControleRelease= new KeyEvent(
                testTextArea, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        blindWriterKeyListener.keyPressed(keyEventControle);
        blindWriterKeyListener.keyReleased(keyEventControleRelease);
        blindWriterKeyListener.keyPressed(keyEventControle);
        blindWriterKeyListener.keyReleased(keyEventControleRelease);
        Thread.sleep(100);
        assertEquals("", testTextArea.getText(), "KeyEvent.CHAR_UNDEFINED is ignored even if pressed twice");
    }

    @Test
    void keyHeldDown() throws InterruptedException {
        KeyEvent keyEventA= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'a');
        blindWriterKeyListener.keyPressed(keyEventA);
        blindWriterKeyListener.keyPressed(keyEventA);
        Thread.sleep(100);
        assertEquals("", testTextArea.getText(), "Key held down does not write text");
    }

    @Test
    void keyPressedTriggersSoundManager(){
        KeyEvent keyEvent= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_CONTROL, 'a');
        blindWriterKeyListener.keyPressed(keyEvent);
        verify(soundManager).play("a");
    }

    @Test
    void keyHeldDownTriggersSoundManagerOnce(){
        KeyEvent keyEvent= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_CONTROL, 'a');
        blindWriterKeyListener.keyPressed(keyEvent);
        blindWriterKeyListener.keyPressed(keyEvent);
        verify(soundManager, times(1)).play("a");
    }

    @Test
    void keyPressedConsumeEvent(){
        KeyEvent keyEvent= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        blindWriterKeyListener.keyPressed(keyEvent);
        assertTrue(keyEvent.isConsumed(), "Key events are consumed");
    }

    @Test
    void keyTypedConsumeEvent() {
        KeyEvent keyEvent = new KeyEvent(
                testTextArea, KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, 'a');
        blindWriterKeyListener.keyTyped(keyEvent);
        assertTrue(keyEvent.isConsumed(), "Key events are consumed");
    }

}