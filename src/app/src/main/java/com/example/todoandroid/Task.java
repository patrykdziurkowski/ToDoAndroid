package com.example.todoandroid;

import java.util.Date;

public class Task {
    private String title;
    private String description;
    private boolean isCompleted;
    private Date dateAdded;
    private Date deadline;
    private TaskPriority priority;
    public enum TaskPriority { IMPORTANT, NORMAL }

    public Task(
            String title,
            String description,
            Date deadline,
            Date dateAdded
    ) {
        this.title = title;
        this.description = description;
        isCompleted = false;
        this.dateAdded = dateAdded;
        this.deadline = deadline;
        priority = TaskPriority.NORMAL;
    }

    public Task(
            String title,
            String description,
            Date dateAdded
    ) {
        this(title, description, null, dateAdded);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDeadline() {
        return deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }
}
