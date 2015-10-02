package com.fteams.sstrain.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.fteams.sstrain.util.SongUtils;

public class GlobalConfiguration {

    public static int songVolume;
    public static int feedbackVolume;
    // offset in milliseconds
    public static int offset;
    public static int inputOffset;
    public static int noteSpeed;
    // path to beatmaps
    public static String pathToBeatmaps;
    public static boolean playHintSounds;
    // sorting related
    public static int sortMode;

    // sync config
    public static int syncMode;

    public final static int BASE_HEIGHT = 720;


    public static void loadConfiguration() {
        Preferences prefs = Gdx.app.getPreferences("ss_train_config");
        offset = prefs.getInteger("offset", 0);
        inputOffset = prefs.getInteger("input_offset", 0);
        songVolume = prefs.getInteger("song_vol", 100);
        feedbackVolume = prefs.getInteger("feedback_vol", 100);
        pathToBeatmaps = prefs.getString("path_to_beatmaps", Gdx.files.getExternalStoragePath() + "sstrain");
        playHintSounds = prefs.getBoolean("play_hint_sounds", false);
         noteSpeed = prefs.getInteger("note_speed", 6);
        // default to song name sorting
        sortMode = prefs.getInteger("sorting_mode", SongUtils.SORTING_MODE_SONG_NAME);
        // sync mode
        syncMode = prefs.getInteger("sync_mode", SongUtils.SYNC_MODE_1);

    }

    public static void storeConfiguration() {
        Preferences prefs = Gdx.app.getPreferences("ss_train_config");
        prefs.putInteger("offset", offset);
        prefs.putInteger("input_offset", inputOffset);
        prefs.putInteger("song_vol", songVolume);
        prefs.putInteger("feedback_vol", feedbackVolume);
        prefs.putString("path_to_beatmaps", pathToBeatmaps);
        prefs.putBoolean("play_hint_sounds", playHintSounds);
        prefs.putInteger("note_speed", noteSpeed);
        prefs.putInteger("sorting_mode", sortMode);
        prefs.putInteger("sync_mode", syncMode);
        prefs.flush();
    }
}
