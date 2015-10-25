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

            // we create a copy which is modified based on the live options - speed / a-b repeat
            Note copy = copy(notesInfo);
            if (GlobalConfiguration.playbackMode != null && GlobalConfiguration.playbackMode.equals(SongUtils.GAME_MODE_ABREPEAT)) {
                if (GlobalConfiguration.aTime != null) {
                    if (copy.timing < GlobalConfiguration.aTime) {
                        continue;
                    }
                }
                if (GlobalConfiguration.bTime != null) {
                    if (copy.timing > GlobalConfiguration.bTime + 2f) {
                        continue;
                    }
                }
            }
            if (GlobalConfiguration.playbackRate != null) {
                copy.timing = copy.timing / GlobalConfiguration.playbackRate;

            }

            x = (copy.endPos - 3) * radius * 4;
            Circle mark = new Circle(x, 0, copy, noteSpeed, delay);
            circles.add(mark);
        }
//        System.out.println("Loaded: " + circles.size + " notes");

        linkCircles(circles);
        linkSyncCircles(circles);

        circles.sort();

        int zoneId = 1;
        tapZones = new Array<>();

        for (int i = 0; i < 11; i++) {
            if (i % 2 == 0)
                continue;

            x = (i * 2 - 10) * radius;

            TapZone zone = new TapZone(x, -249.0f, zoneId++);
            tapZones.add(zone);
        }
        tapZones.sort();
        this.accuracyMarkers = new Array<>();
        this.accuracyPopups = new Array<>();
        paused = false;
    }

    private Note copy(Note notesInfo) {
        Note copy = new Note();
        copy.status = notesInfo.status;
        copy.timing = notesInfo.timing;
        copy.id = notesInfo.id;
        copy.nextNoteId = notesInfo.nextNoteId;
        copy.prevNoteId = notesInfo.prevNoteId;
        copy.endPos = notesInfo.endPos;
        copy.startPos = notesInfo.startPos;
        copy.groupId = notesInfo.groupId;
        copy.sync = notesInfo.sync;
        copy.type = notesInfo.type;
        return copy;
    }

    private void linkCircles(Array<Circle> circles) {
        for (int i = 0; i < circles.size; i++) {
            Circle current = circles.get(i);
            if (current.note.nextNoteId == 0)
                continue;

            Circle next = findNext(circles, current.note.nextNoteId);

            // next can be null in A-B repeat mode since not all the circles are loaded.
            if (next != null) {
                current.setNextNote(next);
                current.nextNote.setPreviousNote(current);
            }
            // 2 side relation
        }
    }

    private Circle findNext(Array<Circle> circles, Long nextNoteId) {
        for (Circle circle : circles) {
            if (circle.note.id.equals(nextNoteId))
                return circle;
        }
        return null;
    }

    private void linkSyncCircles(Array<Circle> circles) {
        for (int i = 0; i < circles.size; i++) {
            Circle mark = circles.get(i);
            if (mark.note.sync != 0) {
                Circle next = findNextSync(circles, mark.note.timing, mark.note.id);
                if (next != null) {
                    mark.nextSyncNote = next;
                }
            }
        }

    }

    private Circle findNextSync(Array<Circle> circles, Double timing, Long id) {
        for (int i = 0; i < circles.size; i++) {
            Circle circle = circles.get(i);
            if (circle.note.timing.equals(timing) && circle.note.id > id) {
                return circle;
            }
        }
        return null;
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
