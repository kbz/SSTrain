package com.fteams.sstrain.util;

public class SongUtils {
    public final static Long NOTE_TYPE_NORMAL = 1l;
    public final static Long NOTE_TYPE_HOLD = 2l;

    public final static Integer NOTE_SYNC_OFF = 0;
    public final static Integer NOTE_SYNC_ON = 1;

    public final static Long NOTE_NO_SWIPE = 0L;
    public final static Long NOTE_SWIPE_LEFT = 1L;
    public final static Long NOTE_SWIPE_RIGHT = 2L;

    public final static Integer SORTING_MODE_FILE_NAME = 0;
    public final static Integer SORTING_MODE_SONG_NAME = 1;
    public final static Integer SORTING_MODE_SONG_ID = 2;
    public final static Integer SORTING_MODE_ATTRIBUTE = 3;

    public final static Integer SYNC_MODE_1 = 0;
    public final static Integer SYNC_MODE_2 = 1;
    public final static Integer SYNC_MODE_3 = 2;
    public final static Integer SYNC_DISABLED = 3;

    public final static String[] syncModes = {"Default", "Constant Sync", "Initial Sync", "Disabled"};
    public final static Long[] noteSpeeds = {1800L, 1680L, 1560L, 1440L, 1320L, 1200L, 1050L, 900L, 750L, 600L, 450L};

    public static Long getSpeedFromConfig(Integer noteSpeed) {
        return noteSpeeds[noteSpeed];
    }

    public final static String[] attributes = {"Cute", "Cool", "Passion", "ALL"};

    public static String getAttribute(Long attribute) {
        if (attribute == null) {
            return "Unknown";
        }
        if (attribute == 0) {
            return "TUTORIAL";
        }
        return attributes[(int) (attribute - 1)];
    }
}
