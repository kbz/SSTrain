package com.fteams.sstrain.entities;

public class Note {
    public Long id;
    public Double timing;
    // for types, refer to SongUtils
    public Long type;
    // origin point [1-5]
    public Long startPos;
    // tap zone [1-5]
    public Long endPos;
    public Long status;
    // indicates if there's another note with the same timestamp and tags them as >simultaneous<
    public Long sync;
    // used in linked slides to draw beams between notes
    public Long groupId;
    // for easy access in groups and holds
    public Long nextNoteId;
    public Long prevNoteId;
}
