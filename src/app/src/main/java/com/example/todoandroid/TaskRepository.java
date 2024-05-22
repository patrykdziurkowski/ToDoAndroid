package com.example.todoandroid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TaskRepository {
    @Inject
    public TaskRepository() {}

    public List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task(
                "Go hiking on Saturday",
                "I need to have fun and go hiking on this sunny day!",
                new Date(2020, 8, 24),
                new Date(2020, 8, 27)
        ));
        tasks.add(new Task(
                "Clean the dishes",
                "",
                new Date(2020, 7, 6)
        ));
        tasks.add(new Task(
                "Fix the car",
                "Car broke down and I need to fix it",
                new Date(2020, 8, 19),
                new Date(2020, 8, 30)
        ));
        return tasks;
    }
}
