package com.fteams.sstrain.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.fteams.sstrain.entities.BaseMetadata;
import com.fteams.sstrain.entities.Beatmap;
import com.fteams.sstrain.entities.BeatmapGroup;
import com.fteams.sstrain.entities.Metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

    public static AssetManager internalManager = new AssetManager(new InternalFileHandleResolver());
    public static AssetManager externalManager = new AssetManager(new ExternalFileHandleResolver());

    static {
        externalManager.setLoader(List.class, new SimplifiedBeatmapLoader(new ExternalFileHandleResolver()));
    }

    public static final String BEATMAP_HOME = "sstrain/beatmaps/";
    public static final String SOUNDFILES_HOME = "sstrain/soundfiles/";

    public static Beatmap selectedBeatmap;
    public static BeatmapGroup selectedGroup;

    public static TextureAtlas atlas;

    public static Skin menuSkin;
    public static Sound noHitTapSound;
    public static Sound niceTapSound;
    public static Sound greatTapSound;
    public static Sound perfectTapSound;
    public static Sound niceSwipeSound;
    public static Sound greatSwipeSound;
    public static Sound perfectSwipeSound;

    public static BitmapFont font;

    public static Texture mainMenuBackgroundTexture;
    public static Texture holdBG;

    public static Array<BeatmapGroup> songGroup;

    // In here we'll put everything that needs to be loaded in this format:
    // manager.load("file location in assets", fileType.class);
    //
    // libGDX AssetManager currently supports: Pixmap, Texture, BitmapFont,
    //     TextureAtlas, TiledAtlas, TiledMapRenderer, Music and Sound.
    public static void queueLoading() {
        internalManager.load("textures/textures.pack.atlas", TextureAtlas.class);
        internalManager.load("hitsounds/no_hit_tap.mp3", Sound.class);
        internalManager.load("hitsounds/tap_nice.mp3", Sound.class);
        internalManager.load("hitsounds/tap_great.mp3", Sound.class);
        internalManager.load("hitsounds/tap_perfect.mp3", Sound.class);
        internalManager.load("hitsounds/swipe_nice.mp3", Sound.class);
        internalManager.load("hitsounds/swipe_great.mp3", Sound.class);
        internalManager.load("hitsounds/swipe_perfect.mp3", Sound.class);
        internalManager.load("bigimages/main_menu_background.jpg", Texture.class);
        internalManager.load("images/hold_background.png", Texture.class);
        internalManager.load("fonts/song-font.fnt", BitmapFont.class);
        reloadBeatmaps();
    }

    // thanks to libgdx, the manager will not actually load maps which were already loaded,
    // so if the same file comes again, it will be skipped
    public static void reloadBeatmaps() {
        if (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + BEATMAP_HOME).exists()) {
            for (String fileName : Gdx.files.absolute(Gdx.files.getExternalStoragePath() + BEATMAP_HOME).file().list()) {
                String fullPath = Gdx.files.getExternalStoragePath() + BEATMAP_HOME + fileName;
                // if for any reason the user placed .osu/.osz files in the datafiles, we process them
                if (Gdx.files.absolute(fullPath).isDirectory() || (!fileName.endsWith(".json")))
                    continue;

                externalManager.load(BEATMAP_HOME + fileName, List.class);
            }
        } else {
            (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + "beatmaps")).mkdirs();
            (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + BEATMAP_HOME)).mkdirs();
            (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + SOUNDFILES_HOME)).mkdirs();
        }
    }

    // unlike the simple reload, in the hard reload we unload everything from the external manager
    // and force a reload of the beatmaps - this will cause .osz files which weren't extracted
    // to be processed, .osu files to be converted and music files within the .osz packages
    // to be copied over to the /beatmaps/soundfiles/ folder.
    public static void hardReloadBeatmaps() {
        selectedBeatmap = null;
        selectedGroup = null;
        externalManager.clear();
        reloadBeatmaps();
    }

    //In here we'll create our skin, so we only have to create it once.
    public static void setMenuSkin() {
        if (menuSkin == null)
            menuSkin = new Skin(Gdx.files.internal("skins/menuSkin.json"), internalManager.get("textures/textures.pack.atlas", TextureAtlas.class));
    }

    public static void setTextures() {
        if (atlas == null)
            atlas = internalManager.get("textures/textures.pack.atlas");

        if (mainMenuBackgroundTexture == null)
            mainMenuBackgroundTexture = internalManager.get("bigimages/main_menu_background.jpg");

        if (holdBG == null)
            holdBG = internalManager.get("images/hold_background.png");
    }

    public static void setFonts() {
        if (font == null)
            font = internalManager.get("fonts/song-font.fnt");

    }

    public static void setHitsounds() {
        if (noHitTapSound == null)
            noHitTapSound = internalManager.get("hitsounds/no_hit_tap.mp3");
        if (niceTapSound == null)
            niceTapSound = internalManager.get("hitsounds/tap_nice.mp3");
        if (greatTapSound == null)
            greatTapSound = internalManager.get("hitsounds/tap_great.mp3");
        if (perfectTapSound == null)
            perfectTapSound = internalManager.get("hitsounds/tap_perfect.mp3");
        if (niceSwipeSound == null)
            niceSwipeSound = internalManager.get("hitsounds/swipe_nice.mp3");
        if (greatSwipeSound == null)
            greatSwipeSound = internalManager.get("hitsounds/swipe_great.mp3");
        if (perfectSwipeSound == null)
            perfectSwipeSound = internalManager.get("hitsounds/swipe_perfect.mp3");
    }

    @SuppressWarnings("unchecked")
    public static void setSongs() {
        if (songGroup == null) {
            songGroup = new Array<>();
        } else {
            songGroup.clear();
        }

        Array<String> assets = externalManager.getAssetNames();
        Map<Long, BeatmapGroup> groupMap = new HashMap<>();

        for (String string : assets) {
            List<Beatmap> beatmaps = externalManager.get(string, List.class);
            if (!beatmaps.isEmpty()) {
                Metadata metadata = beatmaps.get(0).metadata;
                Long liveId = metadata.id;
                if (groupMap.get(liveId) == null) {
                    BeatmapGroup group = new BeatmapGroup();
                    group.metadata = new BaseMetadata();
                    group.metadata.id = metadata.id;
                    group.metadata.composer = metadata.composer;
                    group.metadata.lyricist = metadata.lyricist;
                    group.metadata.songFile = metadata.songFile;
                    group.metadata.songName = metadata.songName;
                    group.metadata.attribute = metadata.attribute;
                    group.metadata.duration = metadata.duration;
                    group.beatmaps = new Array<>();
                    groupMap.put(liveId, group);
                }

                BeatmapGroup group = groupMap.get(liveId);
                for (Beatmap beatmap : beatmaps) {
                    group.beatmaps.add(beatmap);
                }
                group.beatmaps.sort();
            }
        }
        for (Long liveId: groupMap.keySet()) {
            songGroup.add(groupMap.get(liveId));
        }
        songGroup.sort();
    }

    public static boolean update() {
        return internalManager.update() && externalManager.update();
    }

    public static float getProgress() {
        return (internalManager.getProgress() + externalManager.getProgress()) / 2;
    }

    public static void setSelectedBeatmap(Beatmap song) {
        selectedBeatmap = song;
    }
}
