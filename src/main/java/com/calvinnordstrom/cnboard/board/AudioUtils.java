package com.calvinnordstrom.cnboard.board;

import javax.sound.sampled.*;

public class AudioUtils {
    public static final AudioFormat DEFAULT_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // Sample rate (Hz)
            16,    // Sample size (bits)
            2,     // Channels (stereo)
            4,     // Frame size (bytes)
            44100, // Frame rate (Hz)
            false  // Little-endian
    );
    private static final DataLine.Info DATA_LINE_INFO = new DataLine.Info(SourceDataLine.class, DEFAULT_FORMAT);

    public static TargetDataLine getDefaultTarget() {
        try {
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, DEFAULT_FORMAT);
            if (!AudioSystem.isLineSupported(micInfo)) {
                return null;
            }
            return (TargetDataLine) AudioSystem.getLine(micInfo);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static SourceDataLine getDefaultSource() {
        try {
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                System.out.println(info);
            }

            Mixer.Info mixerInfo = AudioSystem.getMixerInfo()[0];
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
//            return (SourceDataLine) mixer.getLine(mixer.getLineInfo());
            return (SourceDataLine) AudioSystem.getLine(DATA_LINE_INFO);
        } catch (LineUnavailableException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static SourceDataLine getSourceByName(String name) {
        try {
            Mixer.Info mixerInfo = getMixerInfoByName(name);
            if (mixerInfo == null) {
                return null;
            }
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            return (SourceDataLine) mixer.getLine(DATA_LINE_INFO);
        } catch (LineUnavailableException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static Mixer.Info getMixerInfoByName(String name) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equalsIgnoreCase(name)) {
                return info;
            }
        }
        return null;
    }
}
