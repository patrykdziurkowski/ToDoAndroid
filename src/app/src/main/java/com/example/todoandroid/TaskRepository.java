package com.example.todoandroid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

public class TaskRepository {
    private final ArrayList<Task> tasks = new ArrayList<>();

    @Inject
    public TaskRepository() {
        tasks.add(new Task(
                UUID.randomUUID(),
                "Go hiking on Saturday",
                "I need to have fun and go hiking on this sunny day!",
                new Date(2020, 8, 24),
                new Date(2020, 8, 27)
        ));
        tasks.add(new Task(
                UUID.randomUUID(),
                "Clean the dishes",
                "",
                new Date(2020, 7, 6)
        ));
        tasks.add(new Task(
                UUID.randomUUID(),
                "Fix the car",
                "Car broke down and I need to fix it",
                new Date(2020, 8, 19),
                new Date(2020, 8, 30)
        ));
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Optional<Task> getTaskById(UUID id) {
        return tasks.stream()
                .filter((t) -> t.getId() == id)
                .findFirst();
    }

    public void save(Task task) {
        Optional<Task> taskToChange = tasks.stream()
                .filter((t) -> { return t.getId() == task.getId(); })
                .findFirst();

        if (!taskToChange.isPresent()) {
            tasks.add(task);
        }

        Task taskToUpdate = tasks.stream()
                .filter((t) -> { return t.getId() == task.getId(); })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Element previously added not found in repository"));
        taskToUpdate
                .setTitle(task.getTitle())
                .setDescription(task.getDescription())
                .setCompleted(task.isCompleted())
                .setDeadline(task.getDeadline())
                .setPriority(task.getPriority());
        if (task.isMarkedForDeletion()) {
            tasks.remove(taskToUpdate);
        }
    }
}
