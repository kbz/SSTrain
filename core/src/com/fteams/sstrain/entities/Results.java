package com.fteams.sstrain.entities;


import com.fteams.sstrain.util.Accuracy;

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

    // fixed time windows for registering taps - speed depen
    public static Accuracy getAccuracyFor(float timing) {
        // Perfect
        // instead of windows deferred from the note speed, they seem to use a constant window for all the difficulties.

        if (Math.abs(timing) < 0.080f) {
            return Accuracy.PERFECT;
        }
        if (Math.abs(timing) <  0.110f) {
            return Accuracy.GREAT;
        }
        if (Math.abs(timing) <  0.180f) {
            return Accuracy.NICE;
        }
        if (Math.abs(timing) <  0.200f) {
            return Accuracy.BAD;
        }
        return Accuracy.MISS;
    }
}
