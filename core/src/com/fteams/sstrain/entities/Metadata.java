package com.fteams.sstrain.entities;

public class Metadata extends BaseMetadata{
    public String difficultyName;
    public Long difficulty;

    public int compareTo(Metadata metadata) {
        if (difficulty.equals(metadata.difficulty))
        {
            return difficultyName.compareTo(metadata.difficultyName);
        }
        return Long.compare(difficulty, metadata.difficulty);
    }
}
