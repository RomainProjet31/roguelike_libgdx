package fr.romainprojet31.my_roguelike.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import fr.romainprojet31.my_roguelike.constants.SoundsNames;

import java.util.HashMap;
import java.util.Map;

public class SoundManager implements IManager {
    /**
     * Cannot set final because add() is called before the instantiation of the singleton.
     */
    private static SoundManager instance = null;

    private final Map<String, Music> dictSounds;

    private SoundManager() {
        dictSounds = new HashMap<>();
        add0(SoundsNames.WIN.getName(), SoundsNames.WIN.getPath());
        add0(SoundsNames.HIT.getName(), SoundsNames.HIT.getPath());
    }

    private static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public static void add(String soundName, String filePath) {
        getInstance().add0(soundName, filePath);
    }

    public static void play(SoundsNames soundName) {
        Music music = getInstance().dictSounds.get(soundName.getName());
        if (!music.isPlaying()) {
            music.play();
        }
    }

    public static void disposeInstance(){
        getInstance().dispose();
    }

    public void dispose() {
        dictSounds.forEach((key, value) -> value.dispose());
    }

    private void add0(String soundName, String filePath) {
        Music music = Gdx.audio.newMusic(new FileHandle(filePath));
        dictSounds.put(soundName, music);
    }
}
