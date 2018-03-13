package de.aaronsom.blindWriter.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TxtFileSaverTest {

    File testFile;
    TxtFileSaver fileSaver;

    @BeforeEach
    void init() throws IOException {
        testFile = File.createTempFile("blind-writer-test", "txt");
        fileSaver = new TxtFileSaver(testFile);
    }

    @AfterEach
    void cleanUp(){
        testFile.delete();
    }

    @Test
    void unsavedChangesTest(){
        fileSaver.append("something");
        assertTrue(fileSaver.hasUnsavedChanges(), "There should be unsaved changes");
    }

    @Test
    void noUnsavedChangesTest(){
        assertTrue(!fileSaver.hasUnsavedChanges(), "Newly created object has no unsaved changes");
        fileSaver.append("something");
        fileSaver.save();
        assertTrue(!fileSaver.hasUnsavedChanges(), "There should be no unsaved changes after saving");
    }

    @Test
    void appendAndSaveTest() throws IOException {
        String toAppend = "a";
        fileSaver.append(toAppend);
        fileSaver.save();

        BufferedReader reader = new BufferedReader(new FileReader(testFile));
        String read = reader.readLine();
        assertEquals(toAppend, read, "Content of test file should be the appended text");
    }

    @Test
    void removeAndSaveTest() throws IOException {
        String toAppend = "aaa";
        fileSaver.append(toAppend);
        fileSaver.remove(1);
        fileSaver.save();

        BufferedReader reader = new BufferedReader(new FileReader(testFile));
        String read = reader.readLine();
        assertEquals(toAppend.substring(0, toAppend.length()-1), read,
                     "Content of test file should be the appended text without the removed text");
    }
}