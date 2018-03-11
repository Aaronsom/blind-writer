package de.aaronsom.blindWriter.writing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BlindWriterKeyListenerTest {

    JTextArea testTextArea;
    BlindWriterKeyListener blindWriterKeyListener;

    @BeforeEach
    void init(){
        testTextArea = new JTextArea();
        blindWriterKeyListener = new BlindWriterKeyListener(testTextArea);
    }

    @Test
    void keyPressedOnceEach() throws InterruptedException {
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

    }

    @Test
    void normalKeyPressedTwice() throws InterruptedException {
        KeyEvent keyEventA= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'a');
        blindWriterKeyListener.keyPressed(keyEventA);
        blindWriterKeyListener.keyPressed(keyEventA);
        Thread.sleep(100);
        assertEquals("a", testTextArea.getText(), "Pressing 'a' twice results in 'a'");
    }

    @Test
    void enterKeyPressedTwice() throws InterruptedException {
        int lineCount = testTextArea.getLineCount();
        KeyEvent keyEventEnter= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, '\n');
        blindWriterKeyListener.keyPressed(keyEventEnter);
        blindWriterKeyListener.keyPressed(keyEventEnter);
        Thread.sleep(100);
        assertEquals(lineCount+1, testTextArea.getLineCount(), "Pressing Enter twice results in a new line");
    }

    @Test
    void spaceKeyPressedTwice() throws InterruptedException {
        KeyEvent keyEventSpace= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_SPACE, '\u0020');
        blindWriterKeyListener.keyPressed(keyEventSpace);
        blindWriterKeyListener.keyPressed(keyEventSpace);
        Thread.sleep(100);
        assertEquals(" ", testTextArea.getText(), "Pressing Space twice results in ' '");
    }

    @Test
    void backspaceKeyPressedTwice() throws InterruptedException {
        testTextArea.append("aa");
        KeyEvent keyEventBackspace= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_BACK_SPACE, '\u0008');
        blindWriterKeyListener.keyPressed(keyEventBackspace);
        blindWriterKeyListener.keyPressed(keyEventBackspace);
        Thread.sleep(100);
        assertEquals("a", testTextArea.getText(), "Pressing Backspace twice removes one character");
    }

    @Test
    void charUndefinedKeyPressedTwice() throws InterruptedException {
        KeyEvent keyEventControle= new KeyEvent(
                testTextArea, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        blindWriterKeyListener.keyPressed(keyEventControle);
        blindWriterKeyListener.keyPressed(keyEventControle);
        Thread.sleep(100);
        assertEquals("", testTextArea.getText(), "KeyEvent.CHAR_UNDEFINED is ignored even if pressed twice");
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