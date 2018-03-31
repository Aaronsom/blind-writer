package de.aaronsom.blindWriter.sound;

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
        defaultPlayer = new SoundPlayer(makePathToMP3("default"));

        //backspace
        inputKeyToSoundMapping.put("\u0008", new SoundPlayer(makePathToMP3("loeschen")));
        //enter
        inputKeyToSoundMapping.put("\n", new SoundPlayer(makePathToMP3("zeilenumbruch")));
        //space
        inputKeyToSoundMapping.put("\u0020", new SoundPlayer(makePathToMP3("leerzeichen")));
        //dot and comma
        inputKeyToSoundMapping.put(".", new SoundPlayer(makePathToMP3("punkt")));
        inputKeyToSoundMapping.put(",", new SoundPlayer(makePathToMP3("komma")));
        //umlaute
        inputKeyToSoundMapping.put("ä", new SoundPlayer(makePathToMP3("ae")));
        inputKeyToSoundMapping.put("ö", new SoundPlayer(makePathToMP3("oe")));
        inputKeyToSoundMapping.put("ü", new SoundPlayer(makePathToMP3("ue")));
        //alphabet
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i<26; i++){
            String letter = String.valueOf(alphabet.charAt(i));
            inputKeyToSoundMapping.put(letter, new SoundPlayer(makePathToMP3(letter)));
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
     * Creates the path to the MP3 file by concatenation of the base URL, the name and the file extension.
     * @param name the name of the file
     * @return the the String "/de.aaronsom.blindWriter.sound/<name>.mp3"
     */
    private String makePathToMP3(String name){
        return baseURL+name+".mp3";
    }
}
