package com.fteams.sstrain.entities;

import com.fteams.sstrain.util.SongUtils;

public class Metadata extends BaseMetadata{
    public String difficultyName;
    public Long difficulty;

    public int compareTo(Metadata metadata) {
        if (songName.equals(metadata.songName))
        {
            if (difficulty.equals(metadata.difficulty))
            {
                return difficultyName.compareTo(metadata.difficultyName);
            }
            else return SongUtils.compare(difficulty, metadata.difficulty);
        }
        return songName.compareTo(metadata.songName);
    }
}
