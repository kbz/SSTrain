package com.fteams.sstrain.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fteams.sstrain.assets.Assets;
import com.fteams.sstrain.config.GlobalConfiguration;
import com.fteams.sstrain.entities.Note;
import com.fteams.sstrain.entities.Results;
import com.fteams.sstrain.util.Accuracy;
import com.fteams.sstrain.util.SongUtils;

public class Circle implements Comparable<Circle> {

    public Note note;

    public Vector2 origin = new Vector2();
    public Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();

    public boolean hold;
    public Long destination;
    Double speed;
    public Circle nextNote;
    public Circle previousNote;

    float spawnTime;
    float despawnTime;
    float startWaitTime;
    float endWaitTime;

    float size;

    public float hitTime;
    float previousTime;
    long previousSystemTime;

    public boolean visible;
    public boolean holding;
    public boolean waiting;
    public boolean soundPlayed;
    public boolean miss;

    public float alpha = 1f;

    public Accuracy accuracy;
    public boolean processed;

    // holds consist of 2 notes out of which the first one must have type 2
    // the second one will either be a type 2 or a type 1 with some effect
    // notes with type 1 can be linked to other notes IF the effect != 0 and the groupID is set.

    public Circle(float x, float y, Note note, Double noteSpeed, float delay) {

        float timing = (float) (delay + note.timing * 1f + GlobalConfiguration.offset * 1f / 1000f);

        this.origin.x = x;
        this.origin.y = y;
        this.position.x = x;
        this.position.y = y;
        this.note = note;
        this.hold = (note.type & SongUtils.NOTE_TYPE_HOLD) != 0;
        // position goes 1-5
        this.destination = note.endPos;
        this.speed = noteSpeed;
        this.spawnTime = (float) (timing - speed);
        this.startWaitTime = (float) (timing - (hold || !note.status.equals(SongUtils.NOTE_NO_SWIPE) ? 2f : 1f) * SongUtils.overallDiffBad[GlobalConfiguration.overallDifficulty] / 1000f);
        this.endWaitTime = (float) (timing + (hold || !note.status.equals(SongUtils.NOTE_NO_SWIPE) ? 2f : 1f) * SongUtils.overallDiffBad[GlobalConfiguration.overallDifficulty] / 1000f);
        this.despawnTime = timing * 1.0f;
        this.size = 1f;
        this.previousSystemTime = 0L;

        hitTime = -9f;
        previousTime = 0f;

        initializeVelocity();
        initializeStates();
    }

    private void initializeStates() {
        visible = false;
        holding = false;
        soundPlayed = false;
        miss = false;
    }

    public void setPreviousNote(Circle previousNote) {
        this.previousNote = previousNote;
        if (previousNote != null) {
            if (previousNote.hold && previousNote.previousNote == null) {
                this.startWaitTime = (float) (despawnTime - 2f * SongUtils.overallDiffBad[GlobalConfiguration.overallDifficulty] / 1000f);
                this.endWaitTime = (float) (despawnTime + 2f * SongUtils.overallDiffBad[GlobalConfiguration.overallDifficulty] / 1000f);

            }
        }

    }

    public void setNextNote(Circle nextNote) {
        this.nextNote = nextNote;
    }

    private void initializeVelocity() {
        // unless the arc movement is implemented,
        // the notes will simply fall from the top towards the tap zones
        velocity.x = 0;
        velocity.y = (float) (-249 / speed);
    }

    public void update(float time) {

        if (miss || (accuracy != null && !holding)) {
            if (visible) {
                visible = false;
            }
            return;
        }

        if (spawnTime <= time && despawnTime > time && !visible) {
            visible = true;
        }

        if (spawnTime >= time && visible)
            visible = false;

        if (visible && despawnTime <= time) {
            if (GlobalConfiguration.playHintSounds && !soundPlayed) {
                // hint sounds play at 50% of the volume
                if (note.status.equals(SongUtils.NOTE_NO_SWIPE)) {
                    Assets.perfectTapSound.play(GlobalConfiguration.feedbackVolume / 200f);
                } else {
                    Assets.perfectSwipeSound.play(GlobalConfiguration.feedbackVolume / 200f);
                }
                soundPlayed = true;
            }

            if (holding) {
                alpha = 1f;
            } else {
                alpha = MathUtils.clamp((endWaitTime - time) / (endWaitTime - despawnTime), 0f, 1f);
                if (alpha == 0f)
                    visible = false;
            }
        }

        if (visible) {
            // TODO: implement parabolic movement of the notes towards the player and use the origin spot instead of spawning from the same lane (more SS-like)
            float scl = time - spawnTime;
            if (holding) {
                position.set(origin.cpy().x, origin.cpy().y - 249);
            } else
                position.set(origin.cpy().add(velocity.cpy().scl(scl)));
        }
        if (startWaitTime <= time && endWaitTime > time && !waiting && accuracy == null) {
            waiting = true;
        }

        processMiss(time);
        previousTime = time;
        previousSystemTime = System.currentTimeMillis();
    }


    private void processMiss(float time) {
        // miss if we miss the first note
        if (nextNote != null && hold && !holding && endWaitTime <= time && accuracy == null && !miss) {
            waiting = false;
            miss = true;
            accuracy = Accuracy.MISS;
            nextNote.miss = true;
            nextNote.accuracy = Accuracy.MISS;
            nextNote.processed = true;
            nextNote.waiting = false;
//            System.out.println("MISS-001: didn't hit the note (" + note.id + ")");
        } else if (nextNote == null && endWaitTime <= time && !miss && accuracy == null) {
            waiting = false;
            miss = true;
            accuracy = Accuracy.MISS;
//            System.out.println("MISS-002: didn't hit the note (" + note.id + ")");
        } else if (nextNote != null && !hold && endWaitTime <= time && accuracy == null && !miss) {
            waiting = false;
            miss = true;
            accuracy = Accuracy.MISS;
//            System.out.println("MISS-003: didn't hit the note (" + note.id + ")");
        }
        if (hold && !miss) {
            // miss if we hold for too long
            if (nextNote != null && nextNote.endWaitTime <= time && nextNote.accuracy == null) {
                miss = true;
                holding = false;
                waiting = false;
//                System.out.println("MISS-004: held for too long (" + note.id + ")");
                accuracy = Accuracy.MISS;
            }
        }
    }

    public Accuracy hit() {
//        System.out.println("H>" + note.id);

        // HIT DOESN'T COUNT FOR HOLD RELEASE!
        if (previousNote != null && previousNote.hold)
            return Accuracy.NONE;

        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalConfiguration.inputOffset / 1000f;

        Accuracy accuracy = hold ? Results.getAccuracyForSwipesAndHolds(hit) : Results.getAccuracyFor(hit);
        // If the note was tapped too early, we ignore the tap
        if (despawnTime > previousTime && accuracy == Accuracy.MISS) {
            return Accuracy.NONE;
        }
        hitTime = hit;
        waiting = false;
        if (hold) {
            hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
            holding = true;
        } else {
            visible = false;
        }
        this.accuracy = accuracy;
        return accuracy;
    }

    public Accuracy release() {
//        System.out.println("R>" + note.id);
        // if a non-hold is released it it counts as a miss.
        // some songs have notes with type 1 and status != 0 for release so we check if the previous note was a hold
        if (previousNote != null && previousNote.hold && !note.status.equals(SongUtils.NOTE_NO_SWIPE)) {
            accuracy = Accuracy.MISS;
            miss = true;
            visible = false;
            previousNote.release();
            waiting = false;
            // only type 2 can gain from a release.
            // type 1 with status calls release on swipe
            return accuracy;
        }
        // RELEASE DOESN'T COUNT FOR HOLD START
        if (holding) {
            holding = false;
            visible = false;
        }
        if (nextNote != null)
            return Accuracy.NONE;

        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalConfiguration.inputOffset / 1000f;
        accuracy = Results.getAccuracyForSwipesAndHolds(hit);
        previousNote.release();
        waiting = false;
        // miss if we release before we start waiting
        if (accuracy == Accuracy.MISS) {
            waiting = false;
            visible = false;
            miss = true;
            processed = true;
//            System.out.println("MISS-005: released hold too early (" + note.id + ")");
        } else {
            hitTime = hit;
            hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
        }
        return accuracy;
    }

    public Accuracy swipeLeft() {
        // some songs have notes with type 2 and status != 0
        if (note.status.equals(SongUtils.NOTE_NO_SWIPE) || note.status.equals(SongUtils.NOTE_SWIPE_RIGHT)) {
            return Accuracy.NONE;
        }
        if (previousNote != null && previousNote.hold) {
            previousNote.release();
        }

        if (previousNote != null && previousNote.previousNote != null) {
            if (previousNote.previousNote.note.status.equals(note.status) && !previousNote.isDone()) {
                return Accuracy.NONE;
            }
        }
        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalConfiguration.inputOffset / 1000f;
        Accuracy accuracy = Results.getAccuracyForSwipesAndHolds(hit);
        // If the note was tapped too early, we ignore the tap
        if (despawnTime > previousTime && accuracy == Accuracy.MISS) {
            return Accuracy.NONE;
        }
        hitTime = hit;
        hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
        waiting = false;
        this.accuracy = accuracy;
        visible = false;
        return accuracy;
    }

    public Accuracy swipeRight() {
        // some songs have notes with type 2 and status != 0
        // legne on pro, for instance.
        if (note.status.equals(SongUtils.NOTE_NO_SWIPE) || note.status.equals(SongUtils.NOTE_SWIPE_LEFT)) {
            return Accuracy.NONE;
        }
        if (previousNote != null && previousNote.hold) {
            previousNote.release();
        }

        if (previousNote != null && previousNote.previousNote != null) {
            if (previousNote.previousNote.note.status.equals(note.status) && !previousNote.isDone()) {
                return Accuracy.NONE;
            }
        }

        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalConfiguration.inputOffset / 1000f;
        Accuracy accuracy = Results.getAccuracyForSwipesAndHolds(hit);
        // If the note was tapped too early, we ignore the tap
        if (despawnTime > previousTime && accuracy == Accuracy.MISS) {
            return Accuracy.NONE;
        }
        hitTime = hit;
        hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
        waiting = false;
        this.accuracy = accuracy;
        visible = false;
        return accuracy;
    }

    public boolean isDone() {
        return miss || (accuracy != null && !holding);
    }

    @Override
    public int compareTo(Circle o) {
        if (o == null)
            return 1;
        // if the notes have the same timing, sort them by destination
        if (0 == Double.compare(note.timing, o.note.timing)) {
            return Long.compare(destination, o.destination);
        }

        return Double.compare(note.timing, o.note.timing);
    }
}

