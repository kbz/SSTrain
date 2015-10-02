package com.fteams.sstrain.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fteams.sstrain.assets.Assets;
import com.fteams.sstrain.config.GlobalConfiguration;
import com.fteams.sstrain.controller.Crossfader;
import com.fteams.sstrain.controller.SongLoader;
import com.fteams.sstrain.entities.Beatmap;
import com.fteams.sstrain.entities.BeatmapGroup;

@SuppressWarnings("unchecked")
public class SongSelectionScreen implements Screen, InputProcessor {

    private Stage stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    private List<BeatmapGroup> songList = new List<>(Assets.menuSkin);
    private ScrollPane songListPane = new ScrollPane(null, Assets.menuSkin);
    private List<Beatmap> diffList = new List<>(Assets.menuSkin, "diff_list");
    private ScrollPane diffListPane = new ScrollPane(null, Assets.menuSkin);
    private Table table = new Table();
    private TextButton nextButton = new TextButton("Next", Assets.menuSkin, "item1");
    private TextButton backButton = new TextButton("Back", Assets.menuSkin, "item1");
    private Image backgroundImage = new Image(Assets.mainMenuBackgroundTexture);
    private Crossfader previewCrossfader = new Crossfader();

    private void stopPreviewSong() {
        previewCrossfader.dispose();
    }

    private void updatePreviewSong() {
        if(Assets.selectedGroup == null)
            return;

        Music previewMusic = null;
        String musicFile = Assets.selectedGroup.metadata.songFile;

        if(musicFile != null)
            previewMusic = SongLoader.loadSongByName(musicFile);

        if(previewMusic == null)
            previewMusic = SongLoader.loadSongByName(Assets.selectedGroup.metadata.songName);

        previewCrossfader.enqueue(previewMusic);
    }

    @Override
    public void show() {
        float scaleFactor = stage.getHeight() / GlobalConfiguration.BASE_HEIGHT;
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);

        Assets.songGroup.sort();
        songList.setItems(Assets.songGroup);

        if (Assets.selectedGroup != null) {
            songList.setSelected(Assets.selectedGroup);
            diffList.setItems(Assets.selectedGroup.beatmaps);
        } else {
            if (songList.getItems().size != 0)
            {
                Assets.selectedGroup = songList.getItems().get(0);
                diffList.setItems(Assets.selectedGroup.beatmaps);
            }

        }

        songList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BeatmapGroup previousGroup = Assets.selectedGroup;
                BeatmapGroup newSelected = (BeatmapGroup) ((List) actor).getSelected();
                if (previousGroup == newSelected) {
                    // if the same group was selected we ignore it
                    return;
                }

                Assets.selectedGroup = newSelected;
                diffList.setItems(newSelected.beatmaps);
                updatePreviewSong();
            }
        });

        if (Assets.selectedBeatmap != null) {
            diffList.setSelected(Assets.selectedBeatmap);
        } else {
            diffList.setSelected(diffList.getItems().size == 0 ? null : diffList.getItems().first());
        }

        diffList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Beatmap previous = Assets.selectedBeatmap;
                Beatmap newSelection = (Beatmap) ((List) actor).getSelected();
                if (previous == newSelection) {
                    return;
                }
                Assets.selectedBeatmap = newSelection;
            }
        });

        nextButton.getLabel().setFontScale(scaleFactor);
        backButton.getLabel().setFontScale(scaleFactor);

        songListPane.setWidget(songList);
        songListPane.setWidth(stage.getWidth());

        diffListPane.setWidget(diffList);
        diffListPane.setWidth(stage.getWidth());

        table.add(songListPane).colspan(3).size(stage.getWidth() * 0.87f, stage.getHeight() * 0.49f).padBottom(stage.getHeight() * 0.01f).row();
        table.add(diffListPane).colspan(3).size(stage.getWidth() * 0.87f, stage.getHeight() * 0.23f).padBottom(stage.getHeight() * 0.01f).padTop(stage.getHeight() * 0.01f).row();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        backButton.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.selectedGroup = songList.getSelected();
                Assets.selectedBeatmap = diffList.getSelected();
                stopPreviewSong();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        }));
        nextButton.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (diffList.getSelected() == null) {
                    return;
                }
                stopPreviewSong();
                Assets.selectedBeatmap = diffList.getSelected();
                 ((Game) Gdx.app.getApplicationListener()).setScreen(new SongScreen());
            }
        }));
        table.add(backButton).size(stage.getWidth() * 0.87f / 2, stage.getHeight() * 0.12f);
        table.add(nextButton).size(stage.getWidth() * 0.87f / 2, stage.getHeight() * 0.12f);
        stage.addActor(table);

        InputMultiplexer impx = new InputMultiplexer();
        impx.addProcessor(this);
        impx.addProcessor(stage);

        Gdx.input.setInputProcessor(impx);
        Gdx.input.setCatchBackKey(true);

        updatePreviewSong();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        previewCrossfader.update(delta);
        songListPane.act(delta);
        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stopPreviewSong();
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            Assets.selectedBeatmap = diffList.getSelected();
            Assets.selectedGroup = songList.getSelected();
            stopPreviewSong();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            // do nothing
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
