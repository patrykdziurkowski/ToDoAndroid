package com.example.todoandroid.domain;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.UUID;

@Entity
public class Attachment {
    @PrimaryKey
    @NonNull
    private final UUID id;
    private final UUID taskId;
    @TypeConverters(UriConverter.class)
    private final Uri uri;

    public Attachment(
            UUID id,
            UUID taskId,
            Uri uri
    ) {
        this.id = id;
        this.taskId = taskId;
        this.uri = uri;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public Uri getUri() {
        return uri;
    }
}
