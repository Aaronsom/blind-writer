package de.aaronsom.blindWriter.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class to play one sound file
 */
public class SoundPlayer {
    /**
     * The sound file to play
     */
    private File soundFile;

    /**
     * Constructs a SoundPlayer for the given {@link File}
     * If the given file is no sound file, nothing will happen on a call to play()
     * @param file the sound file to play
     */
    public SoundPlayer(File file){
        soundFile = file;
    }

    /**
     * Play soundFile
     * If soundFile is no sound file or if the format is not supported or if other problems occur,
     * nothing will happen
     */
    public void play(){
        try (FileInputStream fileInputStream = new FileInputStream(soundFile)) {
            Player player = new Player(fileInputStream);
            player.play();
            player.close();
        }catch(IOException e){
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}
