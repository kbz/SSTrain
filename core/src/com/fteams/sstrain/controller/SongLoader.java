package com.fteams.sstrain.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.fteams.sstrain.assets.Assets;

public class SongLoader {
    private static final String SONGFILE_PREFIX = "sstrain/soundfiles/";
    private static final String[] SONGFILE_PRIO = {".ogg", ".wav", ".mp3"};

    public static Music loadSongByName(String name) {
        try {
            // try loading the file
            FileHandle handle = Gdx.files.absolute(Gdx.files.getExternalStoragePath() + SONGFILE_PREFIX + name);
            return Gdx.audio.newMusic(handle);
        } catch(Exception e) {
            // if it failed, try loading the file with a different extension (in case the extension was not specified)
            FileHandle handle = null;
            String path = Gdx.files.getExternalStoragePath() + SONGFILE_PREFIX + name.replaceAll("\\.[a-zA-Z0-9]+$","");

            for(String ext : SONGFILE_PRIO) {
                try {
                    handle = Gdx.files.absolute(path + ext);
                    return Gdx.audio.newMusic(handle);
                } catch(Exception e2) {
                    continue;
                }
            }

            return null;
        }
    }

    public static Music loadSongFile() {
        Music result = null;

        if(Assets.selectedBeatmap.metadata.songFile != null)
            result = loadSongByName(Assets.selectedBeatmap.metadata.songFile);

        if(result == null)
            result = loadSongByName(Assets.selectedBeatmap.metadata.songName);

        return result;
    }
}
