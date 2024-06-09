package com.example.todoandroid.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Task {
    @PrimaryKey
    @NonNull
    private final UUID id;
    private String title;
    private String description;
    @TypeConverters(DateOnly.class)
    private final DateOnly dateAdded;
    @TypeConverters(DateOnly.class)
    private Optional<DateOnly> deadline;
    private boolean isCompleted = false;
    private TaskPriority priority = TaskPriority.NORMAL;

    public Task(
            UUID id,
            String title,
            String description,
            DateOnly dateAdded,
            DateOnly deadline
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateAdded = dateAdded;
        this.deadline = Optional.ofNullable(deadline);
    }

    public Task(
            UUID id,
            String title,
            String description,
            DateOnly dateAdded
    ) {
        this(id, title, description, dateAdded, null);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Optional<DateOnly> deadline) {
        this.deadline = deadline;
    }

    public void setDeadline(DateOnly deadline) {
        this.deadline = Optional.ofNullable(deadline);
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public UUID getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public DateOnly getDateAdded() {
        return dateAdded;
    }

    public Optional<DateOnly> getDeadline() {
        return deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }



    public enum TaskPriority { IMPORTANT, NORMAL }
}
