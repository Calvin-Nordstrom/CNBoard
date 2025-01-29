package com.calvinnordstrom.cnboard.board;

import com.calvinnordstrom.cnboard.util.Resources;
import org.jnativehook.keyboard.NativeKeyEvent;

public class Sounds {
    public static Sound createBruhSound() {
        return new Sound.Builder()
                .title("Bruh")
                .iconFile(Resources.BRUH_ICON_FILE)
                .soundFile(Resources.BRUH_SOUND_FILE)
                .keyCode(NativeKeyEvent.VC_1)
                .volume(100)
                .enabled(true)
                .build();
    }

    public static Sound createTacoBellSound() {
        return new Sound.Builder()
                .title("Taco Bell")
                .iconFile(Resources.TACO_BELL_ICON_FILE)
                .soundFile(Resources.TACO_BELL_SOUND_FILE)
                .keyCode(NativeKeyEvent.VC_2)
                .volume(100)
                .enabled(true)
                .build();
    }
}
