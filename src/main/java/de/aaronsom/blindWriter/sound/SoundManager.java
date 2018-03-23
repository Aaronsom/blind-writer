package de.aaronsom.blindWriter.sound;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The class to manage playing the right sound for a key input
 */
public class SoundManager {
    /**
     * Map of all supported keys
     */
    private Map<String, SoundPlayer> inputKeyToSoundMapping;
    /**
     * Default sound for not supported keys
     */
    private SoundPlayer defaultPlayer;

    /**
     * The base URL for all sound resources
     */
    private static String baseURL = "/de.aaronsom.blindWriter.sound/";

    /**
     * Constructs a new SoundManager.
     * If the required sound files with their required name are not found, this will throw a
     * NullPointerException.
     * Currently supported keys are:
     * -the alphabet
     * -german umlaute
     * -enter
     * -backspace
     * -space
     * -dot and comma
     */
    public SoundManager(){
        inputKeyToSoundMapping = new HashMap<>();
        defaultPlayer = new SoundPlayer(getMP3ByName("default"));

        //backspace
        inputKeyToSoundMapping.put("\u0008", new SoundPlayer(getMP3ByName("loeschen")));
        //enter
        inputKeyToSoundMapping.put("\n", new SoundPlayer(getMP3ByName("zeilenumbruch")));
        //space
        inputKeyToSoundMapping.put("\u0020", new SoundPlayer(getMP3ByName("leerzeichen")));
        //dot and comma
        inputKeyToSoundMapping.put(".", new SoundPlayer(getMP3ByName("punkt")));
        inputKeyToSoundMapping.put(",", new SoundPlayer(getMP3ByName("komma")));
        //umlaute
        inputKeyToSoundMapping.put("ä", new SoundPlayer(getMP3ByName("ae")));
        inputKeyToSoundMapping.put("ö", new SoundPlayer(getMP3ByName("oe")));
        inputKeyToSoundMapping.put("ü", new SoundPlayer(getMP3ByName("ue")));
        //alphabet
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i<26; i++){
            String letter = String.valueOf(alphabet.charAt(i));
            inputKeyToSoundMapping.put(letter, new SoundPlayer(getMP3ByName(letter)));
        }
    }

    /**
     * Plays a sound. If the key is supported, the sound for the key is played,
     * otherwise the default sound is played.
     * @param key the key for which a sound is to be played
     */
    public void play(String key){
        String normalizedInputKey = key.toLowerCase();
        if(inputKeyToSoundMapping.containsKey(normalizedInputKey)){
            inputKeyToSoundMapping.get(normalizedInputKey).play();
        } else {
            defaultPlayer.play();
        }
    }

    /**
     * Tries to find and return the file /de.aaronsom.blindWriter.sound/<name>.mp3.
     * If no file with the name can be found, a NullPointerException will be thrown.
     * @param name the name of the file
     * @return the {@link File} for /de.aaronsom.blindWriter.sound/<name>.mp3
     */
    private File getMP3ByName(String name){
        return new File(getClass().getResource(baseURL+name+".mp3").getFile());
    }
}
