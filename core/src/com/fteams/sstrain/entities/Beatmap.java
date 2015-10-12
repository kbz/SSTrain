package com.fteams.sstrain.entities;

import java.util.List;

public class Beatmap implements Comparable<Beatmap>{
    public Metadata metadata;
    public List<Note> notes;

    @Override
    public int compareTo(Beatmap o) {
        if (metadata == null)
            return 1;
        if (o.metadata == null)
            return 2;
        return metadata.compareTo(o.metadata);
    }

    public String toString()
    {
        return metadata.songName.replaceAll("\\\\n", " ") + (metadata.difficultyName == null ? "[" + metadata.difficulty + "*]" : "["+metadata.difficultyName+"]");
    }
}
