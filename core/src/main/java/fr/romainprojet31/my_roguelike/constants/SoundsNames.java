package fr.romainprojet31.my_roguelike.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SoundsNames {
    WIN("win.mp3", "sounds/win.mp3"),
    HIT("bip.mp3", "sounds/bip.mp3");

    private final String name;
    private final String path;
}
