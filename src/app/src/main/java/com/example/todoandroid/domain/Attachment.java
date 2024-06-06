package com.example.todoandroid.domain;

import android.net.Uri;

import java.util.UUID;

public class Attachment {
    private final UUID id;
    private final UUID taskId;
    private final Uri uri;
    private boolean isMarkedForDeletion = false;

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

    public boolean isMarkedForDeletion() {
        return isMarkedForDeletion;
    }

    public Attachment setMarkedForDeletion(boolean markedForDeletion) {
        isMarkedForDeletion = markedForDeletion;
        return this;
    }
}
