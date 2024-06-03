package com.example.todoandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskSortingTests {
    @Test
    public void compareTo_sortsByDeadline_whenGivenDeadlineStrategy() {
        TaskSortingStrategyFactory factory = new TaskSortingStrategyFactory();
        Comparator<Task> sortingStrategy = factory.getSortingStrategy(TaskSortingStrategyFactory.SortingMode.Deadline);
        List<Task> tasks = getTasks();

        List<Task> sortedTasks = tasks.stream()
                .sorted(sortingStrategy)
                .collect(Collectors.toList());

        Task t1 = sortedTasks.get(0);
        Task t2 = sortedTasks.get(1);
        Task t3 = sortedTasks.get(2);
        assertTrue(t1.getDeadline().isPresent());
        assertTrue(t2.getDeadline().isPresent());
        assertFalse(t3.getDeadline().isPresent());
        assertTrue(t1.getDeadline().get().before(t2.getDeadline().get()));
    }

    @Test
    public void compareTo_sortsByDateAdded_whenGivenDateAddedStrategy() {
        TaskSortingStrategyFactory factory = new TaskSortingStrategyFactory();
        Comparator<Task> sortingStrategy = factory.getSortingStrategy(TaskSortingStrategyFactory.SortingMode.DateAdded);
        List<Task> tasks = getTasks();

        List<Task> sortedTasks = tasks.stream()
                .sorted(sortingStrategy)
                .collect(Collectors.toList());

        Task t1 = sortedTasks.get(0);
        Task t2 = sortedTasks.get(1);
        Task t3 = sortedTasks.get(2);
        assertTrue(t1.getDateAdded().before(t2.getDateAdded()));
        assertTrue(t2.getDateAdded().before(t3.getDateAdded()));
    }

    @Test
    public void compareTo_sortsByImportance_whenGivenImportanceStrategy() {
        TaskSortingStrategyFactory factory = new TaskSortingStrategyFactory();
        Comparator<Task> sortingStrategy = factory.getSortingStrategy(TaskSortingStrategyFactory.SortingMode.Importance);
        List<Task> tasks = getTasks();

        List<Task> sortedTasks = tasks.stream()
                .sorted(sortingStrategy)
                .collect(Collectors.toList());

        Task t1 = sortedTasks.get(0);
        Task t2 = sortedTasks.get(1);
        Task t3 = sortedTasks.get(2);
        assertEquals(Task.TaskPriority.IMPORTANT, t1.getPriority());
        assertEquals(Task.TaskPriority.NORMAL, t2.getPriority());
        assertEquals(Task.TaskPriority.NORMAL, t3.getPriority());
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(
                UUID.randomUUID(),
                "Go hiking on Saturday",
                "I need to have fun and go hiking on this sunny day!",
                new DateOnly(2020, 8, 24),
                new DateOnly(2020, 8, 27)
        ));
        tasks.add(new Task(
                UUID.randomUUID(),
                "Clean the dishes",
                "",
                new DateOnly(2020, 7, 6)
        ));
        tasks.add(new Task(
                UUID.randomUUID(),
                "Fix the car",
                "Car broke down and I need to fix it",
                new DateOnly(2020, 8, 19),
                new DateOnly()
        ));
        tasks.get(1).setPriority(Task.TaskPriority.IMPORTANT);
        return tasks;
    }
}
