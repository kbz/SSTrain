package com.fteams.sstrain.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.fteams.sstrain.entities.Beatmap;
import com.fteams.sstrain.entities.Metadata;
import com.fteams.sstrain.entities.Note;
import com.fteams.sstrain.util.SongUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedBeatmapLoader extends AsynchronousAssetLoader<List, SimplifiedBeatmapLoader.BeatmapParameter> {
    private List<Beatmap> beatmaps;

    public SimplifiedBeatmapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, BeatmapParameter parameter) {
        beatmaps = new ArrayList<>();
        if (fileName.endsWith(".json")) {
            loadAsyncStandard(manager, fileName, file, parameter);
        }
    }

    private void loadAsyncStandard(AssetManager manager, String fileName, FileHandle file, BeatmapParameter parameter) {

        FileHandle handle = resolve(fileName);
        String jsonDefinition = handle.readString("UTF-8");
        Beatmap info;
        try {
            info = new Gson().fromJson(jsonDefinition, Beatmap.class);
            info.metadata.fileName = fileName;
            beatmaps.add(info);
        } catch (Exception e) {
            // something went wrong.
            e.printStackTrace();
            Gdx.app.error("FILE_LOAD", "Failed to load beatmap from file: "+ fileName);
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BeatmapParameter parameter) {
        return null;
    }

    @Override
    public List<Beatmap> loadSync(AssetManager manager, String fileName, FileHandle file, BeatmapParameter parameter) {
        return beatmaps;
    }

    public class BeatmapParameter extends AssetLoaderParameters<List> {
    }
}
