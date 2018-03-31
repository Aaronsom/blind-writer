package de.aaronsom.blindWriter.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class to play one sound file
 */
public class SoundPlayer {
    /**
     * The path to the sound file to play
     */
    private String soundFilePath;
    /**
     * Constructs a SoundPlayer for the given file path
     * If the given path references no sound file, nothing will happen on a call to play()
     * @param filePath the path to the sound file to play
     */
    public SoundPlayer(String filePath){
        soundFilePath = filePath;
    }

    /**
     * Play soundFile
     * If soundFile is no sound file or if the format is not supported or if other problems occur,
     * nothing will happen
     */
    public void play(){
        try (InputStream fileInputStream = getClass().getResourceAsStream(soundFilePath)) {
            Player player = new Player(fileInputStream);
            player.play();
            player.close();
        }catch(IOException e){
            e.printStackTrace();
        }catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}
