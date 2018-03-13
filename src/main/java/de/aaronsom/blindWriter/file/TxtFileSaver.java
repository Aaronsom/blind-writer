package de.aaronsom.blindWriter.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * An Implementation of {@link FileSaver} for .txt files
 */
public class TxtFileSaver implements FileSaver{
    /**
     * the unsaved text addition for the file
     */
    private String changes;
    /**
     * the file in which all additions are stored
     */
    private File file;

    /**
     * Constructs a new {@link TxtFileSaver} for the file
     * @param file the file that should have changes saved into
     */
    public TxtFileSaver(File file){
        this.file = file;
        changes = "";
    }

    @Override
    public void save() {
        try(BufferedWriter writer =
                    Files.newBufferedWriter(file.toPath(), Charset.forName("utf-8"),
                                            new StandardOpenOption[]{StandardOpenOption.APPEND})){
            writer.append(changes);
            changes = "";
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasUnsavedChanges() {
        return !changes.isEmpty();
    }

    @Override
    public void append(String toAppend) {
        changes += toAppend;
    }

    @Override
    public void remove(int toRemoveCount) {
        if(toRemoveCount >= changes.length()){
            changes = "";
        } else {
            changes = changes.substring(0, changes.length()-toRemoveCount);
        }
    }
}
