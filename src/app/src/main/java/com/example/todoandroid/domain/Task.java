package com.example.todoandroid.domain;

import java.util.Optional;
import java.util.UUID;

public class Task {
    private final UUID id;
    private String title;
    private String description;
    private final DateOnly dateAdded;
    private Optional<DateOnly> deadline;
    private boolean isCompleted = false;
    private boolean isMarkedForDeletion = false;
    private TaskPriority priority = TaskPriority.NORMAL;

    public enum TaskPriority { IMPORTANT, NORMAL }

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

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public Task setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public Task setDeadline(DateOnly deadline) {
        this.deadline = Optional.ofNullable(deadline);
        return this;
    }

    public Task setDeadline(Optional<DateOnly> deadline) {
        this.deadline = deadline;
        return this;
    }

    public Task setMarkedForDeletion(boolean markedForDeletion) {
        isMarkedForDeletion = markedForDeletion;
        return this;
    }

    public Task setPriority(TaskPriority priority) {
        this.priority = priority;
        return this;
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

    public boolean isMarkedForDeletion() { return isMarkedForDeletion; }

    public Optional<DateOnly> getDeadline() {
        return deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }
}
