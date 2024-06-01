package com.example.todoandroid;

import java.util.Date;
import java.util.UUID;

public class Task {
    private final UUID id;
    private String title;
    private String description;
    private boolean isCompleted;
    private final DateOnly dateAdded;
    private DateOnly deadline;
    private boolean isMarkedForDeletion;
    private TaskPriority priority;
    public enum TaskPriority { IMPORTANT, NORMAL }

    public Task(
            UUID id,
            String title,
            String description,
            DateOnly deadline,
            DateOnly dateAdded
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        isCompleted = false;
        this.dateAdded = dateAdded;
        this.deadline = deadline;
        priority = TaskPriority.NORMAL;
    }

    public Task(
            UUID id,
            String title,
            String description,
            DateOnly dateAdded
    ) {
        this(id, title, description, null, dateAdded);
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

    public DateOnly getDeadline() {
        return deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }
}
