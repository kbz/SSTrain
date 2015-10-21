package com.fteams.sstrain.util;

import java.util.Random;

public class Tips {
    public static final String [] tips = {
            "Do you feel you're having ease clearing songs? Try increasing the Overall Difficulty in the Settings > Timing settings.",
            "The game feels slow? Try increasing the Approach Rate in the Settings > Timing settings.",
            "Did you know that you can load beatmaps without having to restart the game? Go into Settings > Other > Reload Beatmaps!",
            "Can't quite get the beat of the song? Try enabling hint sounds in Settings > Volume settings.",
            "Do you feel the timing window is too small? Try decreasing the Overall Difficulty in the Settings > Timing settings.",
            "If you can't react fast enough to tap on time, try decreasing the Approach Rate in the Settings > Timing settings.",
            "If the notes slow down and speed up, teleport, or  stutter, try changing the Synchronization mode in Settings > Timing settings.",
            "Did you know you can sort the song list in the song selection screen? Check Settings > Other. "
    };

    public static String getRandomTip()
    {
        Random random = new Random(System.nanoTime());
        return "Tip: " + tips[Math.abs(random.nextInt())%tips.length];
    }
}
