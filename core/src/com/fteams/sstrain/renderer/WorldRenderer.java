package com.fteams.sstrain.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fteams.sstrain.World;
import com.fteams.sstrain.assets.Assets;
import com.fteams.sstrain.config.GlobalConfiguration;
import com.fteams.sstrain.entities.Note;
import com.fteams.sstrain.objects.AccuracyMarker;
import com.fteams.sstrain.objects.AccuracyPopup;
import com.fteams.sstrain.objects.Circle;
import com.fteams.sstrain.objects.TapZone;
import com.fteams.sstrain.util.Accuracy;
import com.fteams.sstrain.util.SongUtils;

public class WorldRenderer {

    private static final float CAMERA_WIDTH = 600f;
    private static final float CAMERA_HEIGHT = 400f;

    private World world;
    private OrthographicCamera cam;

    // textures
    TextureRegion circle;
    TextureRegion circleSim;
    TextureRegion circleSwipeLeft;
    TextureRegion circleSwipeLeftSim;
    TextureRegion circleSwipeRight;
    TextureRegion circleSwipeRightSim;

    TextureRegion tapZoneIdle;
    TextureRegion tapZoneWarn;
    TextureRegion tapZonePressed;

    TextureRegion accBadBackground;
    TextureRegion accGoodBackground;
    TextureRegion accGreatBackground;
    TextureRegion accPerfectBackground;

    TextureRegion holdBG;

    TextureRegion accHitMark;

    TextureRegion missMark;
    TextureRegion badLateMark;
    TextureRegion badSoonMark;
    TextureRegion goodLateMark;
    TextureRegion goodSoonMark;
    TextureRegion greatLateMark;
    TextureRegion greatSoonMark;
    TextureRegion perfectMark;

    BitmapFont font;

    GlyphLayout layout;

    // extra stuff
    private PolygonSpriteBatch spriteBatch;

    private ShapeRenderer renderer;

    private int width;
    private int height;
    private int positionOffsetX;
    private int positionOffsetY;
    // pixels per unit on X
    public float ppuX;
    // pixels per unit on Y
    public float ppuY;

    private float time;

    short[] triangles = {0, 1, 2, 0, 2, 3};

    public void setSize(int w, int h, int offsetX, int offsetY) {
        this.width = w;
        this.height = h;
        this.positionOffsetX = offsetX;
        this.positionOffsetY = offsetY;
        ppuX = (float) width / CAMERA_WIDTH;
        ppuY = (float) height / CAMERA_HEIGHT;
    }

    public WorldRenderer(World world) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(0f, 0f, 0f);
        this.cam.update();
        spriteBatch = new PolygonSpriteBatch();
        renderer = new ShapeRenderer();
        layout = new GlyphLayout();
        loadTextures();
    }

    private void loadTextures() {
        TextureAtlas atlas = Assets.atlas;
        circle = atlas.findRegion("circle");
        circleSim = atlas.findRegion("circle_sim");
        circleSwipeLeft = atlas.findRegion("circle_swipe_left");
        circleSwipeLeftSim = atlas.findRegion("circle_swipe_left_sim");
        circleSwipeRight = atlas.findRegion("circle_swipe_right");
        circleSwipeRightSim = atlas.findRegion("circle_swipe_right_sim");

        tapZoneIdle = atlas.findRegion("tap");
        tapZonePressed = atlas.findRegion("tap_pressed");
        tapZoneWarn = atlas.findRegion("tap_warn");

        accBadBackground = atlas.findRegion("acc_bad");
        accGoodBackground = atlas.findRegion("acc_good");
        accGreatBackground = atlas.findRegion("acc_great");
        accPerfectBackground = atlas.findRegion("acc_perfect");
        accHitMark = atlas.findRegion("acc_mark");

        holdBG = new TextureRegion(Assets.holdBG);

        missMark = atlas.findRegion("miss");
        badLateMark = atlas.findRegion("bad_late");
        badSoonMark = atlas.findRegion("bad_soon");
        goodLateMark = atlas.findRegion("nice_late");
        goodSoonMark = atlas.findRegion("nice_soon");
        greatLateMark = atlas.findRegion("great_late");
        greatSoonMark = atlas.findRegion("great_soon");
        perfectMark = atlas.findRegion("perfect");

        font = Assets.font;
    }

    public void render() {
        spriteBatch.begin();
        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawTapZones();
        drawCircles();
        drawFlatBar();
        drawCombo();
        //drawProgressBar();
        drawAccuracyBar();
        if (!world.started) {
            drawTapToBeginMessage();
        }
        if (!world.paused) {
            drawAccuracy();
        }
        if (world.paused) {
            drawTapToContinue();
        }
        renderer.end();
        spriteBatch.end();
        time += Gdx.graphics.getDeltaTime();
    }

    private void drawFlatBar() {
        float centerX = this.positionOffsetX + width / 2;
        float y = this.positionOffsetY + height - height * 0.72f;
        spriteBatch.draw(accBadBackground, centerX - width / 2f, y, width, height * 0.01f);
    }

    private void drawAccuracyBar() {
        float centerX = this.positionOffsetX + width / 2;
        float y = this.positionOffsetY + height - height * 0.1f;
        float zone = (SongUtils.getSpeedFromConfig(GlobalConfiguration.noteSpeed)) * 0.5f / 2000f;
        // draw the background (bad level)
        spriteBatch.draw(accBadBackground, centerX - width / 6f, y, width / 3f, height * 0.01f);
        // draw the background (good level)
        spriteBatch.draw(accGoodBackground, centerX - 0.8f * width / 6f, y, 0.8f * width / 3f, height * 0.01f);
        // draw the background (great level)
        spriteBatch.draw(accGreatBackground, centerX - 0.70f * width / 6f, y, 0.7f * width / 3f, height * 0.01f);
        // draw the background (perfect level)
        spriteBatch.draw(accPerfectBackground, centerX - 0.4f * width / 6f, y, 0.4f * width / 3f, height * 0.01f);
        // draw each of the 'markers'
        for (AccuracyMarker accMarker : world.getAccuracyMarkers()) {
            if (accMarker.display) {

                spriteBatch.setColor(1, 1, 1, accMarker.getAlpha());
                spriteBatch.draw(accHitMark, centerX + (accMarker.getTime()) * (width / 6f) / zone - accHitMark.getRegionWidth(), y - height * 0.01f, 3f, height * 0.03f);
            }
        }
        spriteBatch.setColor(1, 1, 1, 1);
    }

    private void drawTapToBeginMessage() {
        String tapToBegin = "Tap to begin!";
        float centerX = this.positionOffsetX + width / 2;
        float centerY = this.positionOffsetY + height / 2 + height * 0.15f;
        layout.setText(font, tapToBegin);
        font.draw(spriteBatch, tapToBegin, centerX - layout.width / 2, centerY - layout.height / 2);
    }

    private void drawTapToContinue() {
        String tapToBegin = "Tap to continue!";
        float centerX = this.positionOffsetX + width / 2;
        float centerY = this.positionOffsetY + height / 2 + height * 0.15f;
        layout.setText(font, tapToBegin);
        font.draw(spriteBatch, tapToBegin, centerX - layout.width / 2, centerY - layout.height / 2);

        String backToExit = "Or press back again to skip to the Results screen.";
        centerX = this.positionOffsetX + width / 2;
        centerY = this.positionOffsetY + height / 2 + height * 0.1f;
        layout.setText(font, backToExit);
        font.draw(spriteBatch, backToExit, centerX - layout.width / 2, centerY - layout.height / 2);
    }

    private void drawAccuracy() {
        float scale = height / GlobalConfiguration.BASE_HEIGHT;
        float centerX = this.positionOffsetX + width / 2;
        float centerY = this.positionOffsetY + height / 2 + height * 0.15f;
        for (AccuracyPopup popup : world.getAccuracyPopups()) {
            if (popup.show) {
                TextureRegion region = perfectMark;
                if (popup.accuracy == Accuracy.MISS) {
                    region = missMark;
                }
                if (popup.accuracy == Accuracy.BAD) {
                    region = popup.soon ? badSoonMark : badLateMark;
                }
                if (popup.accuracy == Accuracy.NICE) {
                    region = popup.soon ? goodSoonMark : goodLateMark;
                }
                if (popup.accuracy == Accuracy.GREAT) {
                    region = popup.soon ? greatSoonMark : greatLateMark;
                }
                spriteBatch.setColor(1, 1, 1, popup.getAlpha());
                spriteBatch.draw(region, centerX - scale * region.getRegionWidth() * popup.getSize() / 2, centerY - scale * region.getRegionHeight() * popup.getSize() / 2, scale * region.getRegionWidth() * popup.getSize(), scale * region.getRegionHeight() * popup.getSize());
            }
        }
        spriteBatch.setColor(1, 1, 1, 1);
    }

    private void drawCombo() {
        float centerX = this.positionOffsetX + width / 2;
        float centerY = height / 2;
        if (world.combo != 0) {
            layout.setText(font, "" + world.combo);
            font.draw(spriteBatch, "" + world.combo, centerX - layout.width / 2, centerY - layout.height / 2);
        }
    }

    private void drawTapZones() {
        float centerX = this.positionOffsetX + width / 2;
        float centerY = this.positionOffsetY + height - height * 0.2f;
        float size = height * 0.125f;
        for (TapZone tapZone : world.getTapZones()) {

            TextureRegion region = tapZoneIdle;

            if (tapZone.warn) {
                region = tapZoneWarn;
            }

            if (tapZone.pressed) {
                tapZone.touchTime = time;
            }

            final float x = centerX + tapZone.getPosition().x * ppuX - size / 2;
            final float y = centerY + tapZone.getPosition().y * ppuY - size / 2;
            spriteBatch.draw(region, x, y, size, size);

            float alpha = 1f - MathUtils.clamp((time - tapZone.touchTime) * 5f, 0f, 1f);
            if (alpha > 0) {
                Color c = spriteBatch.getColor();
                spriteBatch.setColor(c.r, c.g, c.b, Interpolation.pow2In.apply(alpha));
                spriteBatch.draw(tapZonePressed, x, y, size, size);
                spriteBatch.setColor(c);
            }
        }
    }

    private void drawCircles() {
        float centerX = this.positionOffsetX + width / 2;
        float centerY = this.positionOffsetY + height - height * 0.2f;
        float size = height * 0.1f;

        for (Circle mark : world.getCircles()) {
            if (!mark.visible)
                continue;

            float alpha = mark.alpha;
            // float alpha2 = mark.alpha2;
            Color c = spriteBatch.getColor();

            if (mark.holding)
                spriteBatch.setColor(1.0f, 1.0f, 0.5f, alpha * 1f * 0.45f * (0.75f + 0.25f * MathUtils.sin(time * 7f + mark.hitTime)));
            else
                spriteBatch.setColor(c.r, c.g, c.b, alpha * alpha * 1f * 0.45f);

            if (mark.nextNote != null) {
                Vector2 org = mark.nextNote.position.cpy();
                org.x *= ppuX;
                org.y *= ppuY;
                org.x += centerX;
                org.y += centerY;

                Vector2 dst = mark.position.cpy();
                dst.x *= ppuX;
                if (mark.holding) {
                    dst.y = -249f;
                }

                dst.y *= ppuY;
                dst.x += centerX;
                dst.y += centerY;
                drawHoldBeam(org, dst, size, size);
            }

            if (mark.visible) {

                spriteBatch.setColor(c.r, c.g, c.b, alpha);
                spriteBatch.draw(selectTextureForCircle(mark.note), centerX - size / 2 + mark.position.x * ppuX, centerY - size / 2 + mark.position.y * ppuY, size, size);

            }
            spriteBatch.setColor(c);
        }

    }

    private void drawHoldBeam(Vector2 from, Vector2 to, float orgSize, float dstSize) {
        Vector2 delta = from.cpy().sub(to);

        float w = Math.max(orgSize, dstSize);
        float h = delta.len();

        float tw = holdBG.getRegionWidth();
        float th = holdBG.getRegionHeight();

        float factorScale = (tw / w) * 0.5f;
        float topFactor = Math.max(dstSize - orgSize, 0f) * factorScale;
        float botFactor = Math.max(orgSize - dstSize, 0f) * factorScale;

        float[] points = {
                topFactor,
                0f,

                botFactor,
                th,

                tw - botFactor,
                th,

                tw - topFactor,
                0f
        };

        PolygonRegion clamped = new PolygonRegion(holdBG, points, triangles);
        spriteBatch.draw(clamped, from.x - w * 0.5f, from.y, w * 0.5f, 0f, w, h, 1f, 1f, delta.angle() + 90);
    }

    private TextureRegion selectTextureForCircle(Note note) {

        if (note.sync.intValue() == SongUtils.NOTE_SYNC_ON) {
            if (note.status.equals(SongUtils.NOTE_NO_SWIPE)) {
                return circleSim;
            } else if (note.status.equals(SongUtils.NOTE_SWIPE_LEFT)) {
                return circleSwipeLeftSim;
            } else if (note.status.equals(SongUtils.NOTE_SWIPE_RIGHT)) {
                return circleSwipeRightSim;
            }
            // TODO: Notes in sync (simultaneous) are linked by a horizontal beam.
        } else if (note.sync.intValue() == SongUtils.NOTE_SYNC_OFF) {
            if (note.status.equals(SongUtils.NOTE_NO_SWIPE)) {
                return circle;
            } else if (note.status.equals(SongUtils.NOTE_SWIPE_LEFT)) {
                return circleSwipeLeft;
            } else if (note.status.equals(SongUtils.NOTE_SWIPE_RIGHT)) {
                return circleSwipeRight;
            }
        }
        return circle;
    }
}
