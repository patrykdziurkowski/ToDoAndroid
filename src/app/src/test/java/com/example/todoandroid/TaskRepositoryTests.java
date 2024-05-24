package com.example.todoandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskRepositoryTests {
    @Test
    public void save_addsNewTask_whenItsUuidNotInRepository() {
        TaskRepository taskRepository = new TaskRepository();
        int numberOfTasks = taskRepository.getTasks().size();
        Task newTask = new Task(
                UUID.randomUUID(),
                "My new task",
                "",
                new Date(2020, 6, 24));

        taskRepository.save(newTask);

        int updatedNumberOfTasks = taskRepository.getTasks().size();
        assertEquals(numberOfTasks + 1, updatedNumberOfTasks);
    }

    @Test
    public void save_doesntAddNewTask_whenItsUuidAlreadyInRepository() {
        TaskRepository taskRepository = new TaskRepository();
        int numberOfTasks = taskRepository.getTasks().size();
        Task newTask = new Task(
                UUID.randomUUID(),
                "My new task",
                "",
                new Date(2020, 6, 24));

        taskRepository.save(newTask);
        taskRepository.save(newTask);

        int updatedNumberOfTasks = taskRepository.getTasks().size();
        assertEquals(numberOfTasks + 1, updatedNumberOfTasks);
    }

    @Test
    public void save_changesTaskDetails_whenItsUuidAlreadyInRepository() {
        TaskRepository taskRepository = new TaskRepository();
        UUID taskUuid = UUID.randomUUID();
        Task oldTask = new Task(
                taskUuid,
                "Old task name",
                "Old description",
                new Date(2020, 6, 24));
        Task newTask = new Task(
                taskUuid,
                "New task name",
                "New description",
                new Date(2020, 6, 24));
        newTask.setCompleted(true);
        newTask.setPriority(Task.TaskPriority.IMPORTANT);
        newTask.setDeadline(new Date(2023, 7, 15));

        taskRepository.save(oldTask);
        taskRepository.save(newTask);

        Task task = taskRepository.getTasks().stream()
                .filter((t) -> {
                    return t.getId() == taskUuid;
                })
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No task found with id " + taskUuid));
        assertEquals("New task name", task.getTitle());
        assertEquals("New description", task.getDescription());
        assertTrue(task.isCompleted());
        assertEquals(Task.TaskPriority.IMPORTANT, task.getPriority());
        assertEquals(new Date(2023, 7, 15), task.getDeadline());
    }

    @Test
    public void save_removesATask_thatsMarkedForDeletion() {
        TaskRepository taskRepository = new TaskRepository();
        UUID taskId = UUID.randomUUID();
        Task task = new Task(
                taskId,
                "My new task",
                "",
                new Date(2020, 6, 24));
        task.setMarkedForDeletion(true);

        taskRepository.save(task);

        assertTrue(taskRepository.getTasks().stream()
                .noneMatch((t) -> t.getId() == taskId));
    }
}
