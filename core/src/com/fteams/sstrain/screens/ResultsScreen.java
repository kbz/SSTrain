package com.fteams.sstrain.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fteams.sstrain.assets.Assets;
import com.fteams.sstrain.config.GlobalConfiguration;
import com.fteams.sstrain.entities.Results;

public class ResultsScreen implements Screen {

    Stage stage = new Stage();
    private Texture texture = Assets.mainMenuBackgroundTexture;
    private Image splashImage = new Image(texture);
    private Table table = new Table();
    private Label approachRateLabel = new Label("Note Speed (Approach Rate):", Assets.menuSkin, "song_style_result_values");
    private Label approachRateConfigLabel;
    private Label accuracyLabel = new Label("Avg. Accuracy:", Assets.menuSkin, "song_style_result_values");
    private Label accuracyResultLabel;
    private Label normalizedAccuracyLabel = new Label("% Accuracy:", Assets.menuSkin, "song_style_result_values");
    private Label normalizedAccuracyResultLabel;
    private Label accuracyRangeLabel = new Label("Accuracy Range:", Assets.menuSkin, "song_style_result_values");
    private Label accuracyRangeResultLabel;
    private Label unstableRatingLabel = new Label("Unstable Rating:", Assets.menuSkin, "song_style_result_values");
    private Label unstableRatingValueLabel;
    private Label badLabel = new Label("Bad:", Assets.menuSkin, "song_style_result_values");
    private Label badResultLabel;
    private Label goodLabel = new Label("Nice:", Assets.menuSkin, "song_style_result_values");
    private Label goodResultLabel;
    private Label greatLabel = new Label("Great", Assets.menuSkin, "song_style_result_values");
    private Label greatResultLabel;
    private Label perfectLabel = new Label("Perfect:", Assets.menuSkin, "song_style_result_values");
    private Label perfectResultLabel;
    private Label missLabel = new Label("Miss:", Assets.menuSkin, "song_style_result_values");
    private Label missResultLabel;
    private Label comboLabel = new Label("Largest Combo:", Assets.menuSkin, "song_style_result_values");
    private Label comboResultLabel;
    private Label titleLabel = new Label("Results/結果発表", Assets.menuSkin, "default");

    @Override
    public void show() {
        // title font scale = 1 for a 720 height
        float fontScale = stage.getHeight() / GlobalConfiguration.BASE_HEIGHT;

        splashImage.setHeight(stage.getHeight());
        splashImage.setWidth(stage.getWidth());
        stage.addActor(splashImage);
        titleLabel.setX(stage.getWidth() / 2 - titleLabel.getWidth() / 2);
        titleLabel.setY(stage.getHeight() - stage.getHeight() * 0.3f);
        titleLabel.setFontScale(fontScale);

        table.setFillParent(true);
        approachRateConfigLabel = new Label("AR-"+GlobalConfiguration.noteSpeed, Assets.menuSkin, "song_style_result_values");
        accuracyResultLabel = new Label(String.format("%.2f", Results.accuracy * 1000) + " ms.", Assets.menuSkin, "song_style_result_values");
        accuracyRangeResultLabel = new Label(String.format("%.2f", Results.minAccuracy * 1000) + " ms. to " + String.format("%.2f", Results.maxAccuracy * 1000) + " ms.", Assets.menuSkin, "song_style_result_values");
        normalizedAccuracyResultLabel = new Label(String.format("%.2f", Results.normalizedAccuracy * 100f) + "%", Assets.menuSkin, "song_style_result_values");
        unstableRatingValueLabel = new Label(String.format("%.2f", Results.unstableRating * 1000), Assets.menuSkin, "song_style_result_values");
        missResultLabel = new Label(Integer.toString(Results.miss), Assets.menuSkin, "song_style_result_values");
        badResultLabel = new Label(Integer.toString(Results.bads), Assets.menuSkin, "song_style_result_values");
        goodResultLabel = new Label(Integer.toString(Results.goods), Assets.menuSkin, "song_style_result_values");
        greatResultLabel = new Label(Integer.toString(Results.greats), Assets.menuSkin, "song_style_result_values");
        perfectResultLabel = new Label(Integer.toString(Results.perfects), Assets.menuSkin, "song_style_result_values");
        comboResultLabel = new Label(Integer.toString(Results.combo) + (Results.combo == Assets.selectedBeatmap.notes.size() ?" (FC)" : ""), Assets.menuSkin, "song_style_result_values");

        approachRateConfigLabel.setFontScale(fontScale);
        accuracyResultLabel.setFontScale(fontScale);
        accuracyRangeResultLabel.setFontScale(fontScale);
        normalizedAccuracyResultLabel.setFontScale(fontScale);
        unstableRatingValueLabel.setFontScale(fontScale);
        missResultLabel.setFontScale(fontScale);
        badResultLabel.setFontScale(fontScale);
        goodResultLabel.setFontScale(fontScale);
        greatResultLabel.setFontScale(fontScale);
        perfectResultLabel.setFontScale(fontScale);
        comboResultLabel.setFontScale(fontScale);

        approachRateLabel.setFontScale(fontScale);
        accuracyLabel.setFontScale(fontScale);
        accuracyRangeLabel.setFontScale(fontScale);
        normalizedAccuracyLabel.setFontScale(fontScale);
        unstableRatingLabel.setFontScale(fontScale);
        missLabel.setFontScale(fontScale);
        badLabel.setFontScale(fontScale);
        perfectLabel.setFontScale(fontScale);
        goodLabel.setFontScale(fontScale);
        greatLabel.setFontScale(fontScale);
        comboLabel.setFontScale(fontScale);

        Label songResultTitle = new Label(Assets.selectedBeatmap.metadata.songName.replaceAll("\\\\n", " "), Assets.menuSkin, "song_result_song_title");
        songResultTitle.setFontScale(fontScale);


        table.add(songResultTitle).colspan(3).row();
        table.add(titleLabel).colspan(3).padBottom(stage.getHeight() * 0.04f).row();
        table.add(comboLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f).padBottom(20);
        table.add(comboResultLabel).fillX().padBottom(20).row();

        table.add(approachRateLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(approachRateConfigLabel).fillX().row();
        table.add(normalizedAccuracyLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(normalizedAccuracyResultLabel).fillX().row();
        table.add(accuracyLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(accuracyResultLabel).fillX().row();
        table.add(accuracyRangeLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(accuracyRangeResultLabel).fillX().row();
        table.add(unstableRatingLabel).padBottom(20).fillX();
        table.add().width(stage.getWidth() * 0.2f).padBottom(20);
        table.add(unstableRatingValueLabel).padBottom(20).fillX().row();

        table.add(perfectLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(perfectResultLabel).fillX().row();
        table.add(greatLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(greatResultLabel).fillX().row();
        table.add(goodLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(goodResultLabel).fillX().row();
        table.add(badLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(badResultLabel).fillX().row();
        table.add(missLabel).fillX();
        table.add().width(stage.getWidth() * 0.2f);
        table.add(missResultLabel).fillX().row();

        TextButton retryButton = new TextButton("Retry", Assets.menuSkin, "item1");
        TextButton continueButton = new TextButton("Continue", Assets.menuSkin, "item1");

        retryButton.getLabel().setFontScale(fontScale);
        continueButton.getLabel().setFontScale(fontScale);

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SongScreen());
            }
        });
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SongSelectionScreen());
            }
        });

        table.add(retryButton).width(stage.getWidth() * 0.3f).height(stage.getHeight() * 0.1f);
        table.add().fillX();
        table.add(continueButton).width(stage.getWidth() * 0.3f).height(stage.getHeight() * 0.1f).row();
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        stage.dispose();
    }
}
