package de.aaronsom.blindWriter.file;

/**
 * An interface to keep track of changes to a file and save them
 */
public interface FileSaver {
    /**
     * Saves the changes to the file
     */
    public void save();

    /**
     * Indicates if there are unsaved changes for the file
     * @return true if there are unsaved changes otherwise false
     */
    public boolean hasUnsavedChanges();

    /**
     * Stores the text to append it to the file once save() is called
     * @param toAppend the text to append
     */
    public void append(String toAppend);

    /**
     * Remove text of indicated length at the end of the currently stored changes
     * @param toRemoveCount the number of character to remove
     */
    public void remove(int toRemoveCount);
}
