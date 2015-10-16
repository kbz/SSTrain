package com.fteams.sstrain.entities;


import com.fteams.sstrain.config.GlobalConfiguration;
import com.fteams.sstrain.util.Accuracy;
import com.fteams.sstrain.util.SongUtils;

public class Results {
    public static Integer score;
    public static Integer combo;
    public static float accuracy;
    public static int miss;
    public static int bads;
    public static int goods;
    public static int greats;
    public static int perfects;
    public static float maxAccuracy;
    public static float minAccuracy;
    public static float normalizedAccuracy;
    public static float unstableRating;

    public static void clear() {
        score = 0;
        combo = 0;
        accuracy = 0;
        miss = 0;
        bads = 0;
        goods = 0;
        greats = 0;
        perfects = 0;
        maxAccuracy = 0;
        minAccuracy = 0;
        normalizedAccuracy = 0;
        unstableRating = 0;
    }


    public static float getAccuracyMultiplierForAccuracy(Accuracy accuracy) {
        if (accuracy == Accuracy.PERFECT) {
            return 1.0f;
        }
        if (accuracy == Accuracy.GREAT) {
            return 0.75f;
        }
        if (accuracy == Accuracy.NICE) {
            return 0.50f;
        }
        if (accuracy == Accuracy.BAD) {
            return 0.25f;
        }
        return 0f;
    }

    public static Accuracy getAccuracyFor(float timing) {
        // Perfect
        float zone = SongUtils.getSpeedFromConfig(GlobalConfiguration.noteSpeed) / 1000f;
        if (Math.abs(timing) < zone * 0.1f) {
            return Accuracy.PERFECT;
        }
        if (Math.abs(timing) < zone * 0.175f) {
            return Accuracy.GREAT;
        }
        if (Math.abs(timing) < zone * 0.20f) {
            return Accuracy.NICE;
        }
        if (Math.abs(timing) < zone * 0.25f) {
            return Accuracy.BAD;
        }
        return Accuracy.MISS;
    }

    // holds and swipes have bigger windows
    public static Accuracy getAccuracyForSwipesAndHolds(float timing) {
        // Perfect
//        float zone = SongUtils.getSpeedFromConfig(GlobalConfiguration.noteSpeed) / 1000f;
//        if (Math.abs(timing) < zone * 0.25f) {
//            return Accuracy.PERFECT;
//        }
//        if (Math.abs(timing) < zone * 0.3f) {
//            return Accuracy.GREAT;
//        }
//        if (Math.abs(timing) < zone * 0.35f) {
//            return Accuracy.NICE;
//        }
//        if (Math.abs(timing) < zone * 0.5f) {
//            return Accuracy.BAD;
//        }
        return getAccuracyFor(timing * SWIPE_HOLD_MULTIPLIER);
    }
    public final static float SWIPE_HOLD_MULTIPLIER = 0.5f;
}
