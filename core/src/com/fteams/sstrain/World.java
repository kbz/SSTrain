package com.fteams.sstrain;

import com.badlogic.gdx.utils.Array;
import com.fteams.sstrain.assets.Assets;
import com.fteams.sstrain.config.GlobalConfiguration;
import com.fteams.sstrain.entities.Note;
import com.fteams.sstrain.objects.AccuracyMarker;
import com.fteams.sstrain.objects.AccuracyPopup;
import com.fteams.sstrain.objects.Circle;
import com.fteams.sstrain.objects.TapZone;
import com.fteams.sstrain.util.SongUtils;

public class World {
    int width;
    int height;

    public int combo;
    public boolean started;
    public boolean paused;
    public int offsetX;
    public int offsetY;

    private Array<AccuracyMarker> accuracyMarkers;
    private Array<AccuracyPopup> accuracyPopups;

    Array<TapZone> tapZones = new Array<>();
    Array<Circle> circles = new Array<>();

    public float delay;

    public World() {
        createWorld();
    }

    private void createWorld() {
        float x = 0f;
        float y = 0f;

        float h = 400;
        float w = 600;

        float radius = h * 0.065f;

        Double noteSpeed = SongUtils.getSpeedFromConfig(GlobalConfiguration.noteSpeed) / 1000.0;

        delay = Assets.selectedBeatmap.metadata.leadIn != null ? Assets.selectedBeatmap.metadata.leadIn : 0f;

        if (delay < noteSpeed) {
            delay += noteSpeed;
        }

        for (Note notesInfo : Assets.selectedBeatmap.notes) {

            x = (notesInfo.endPos - 3) * radius * 4;
            Circle mark = new Circle(x, 0, notesInfo, noteSpeed, delay);
            circles.add(mark);
        }
//        System.out.println("Loaded: " + circles.size + " notes");

        linkCircles(circles);

        int zoneId = 1;
        tapZones = new Array<>();

        for (int i = 0; i < 11; i++) {
            if (i % 2 == 0)
                continue;

            x = (i * 2 - 10) * radius;

            TapZone zone = new TapZone(x, -249.0f, zoneId++);
            tapZones.add(zone);
        }
        this.accuracyMarkers = new Array<>();
        this.accuracyPopups = new Array<>();
        paused = false;
    }

    private void linkCircles(Array<Circle> circles) {
        for (int i = 0; i < circles.size; i++) {
            Circle current = circles.get(i);
            if (current.note.nextNoteId == 0)
                continue;

            current.nextNote = circles.get(current.note.nextNoteId.intValue() - 1);
            // 2 side relation
            current.nextNote.previousNote = current;
        }
    }

    public Array<TapZone> getTapZones() {
        return tapZones;
    }

    public Array<Circle> getCircles() {
        return circles;
    }

    public Array<AccuracyMarker> getAccuracyMarkers() {
        return accuracyMarkers;
    }

    public Array<AccuracyPopup> getAccuracyPopups() {
        return accuracyPopups;
    }

    public void setSize(int width, int height, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
