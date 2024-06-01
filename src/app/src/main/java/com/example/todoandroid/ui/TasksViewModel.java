package com.example.todoandroid.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoandroid.Constants;
import com.example.todoandroid.DateOnly;
import com.example.todoandroid.Task;
import com.example.todoandroid.TaskRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TasksViewModel extends ViewModel {
    private final TaskRepository taskRepository;

    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private TaskSortingMode taskSortingMode = TaskSortingMode.Deadline;

    @Inject
    public TasksViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        tasks.setValue(taskRepository.getTasks());
    }

    public void addTask(
            String title,
            String description,
            DateOnly deadline,
            DateOnly dateAdded
    ) {
        Task task = new Task(
                UUID.randomUUID(),
                title,
                description,
                deadline,
                dateAdded
        );
        List<Task> newTasks = tasks.getValue();
        newTasks.add(task);
        taskRepository.save(task);
        tasks.setValue(newTasks);
        sortTasks();
    }

    public void addTask(
            String title,
            String description,
            DateOnly dateAdded
    ) {
        Task task = new Task(
                UUID.randomUUID(),
                title,
                description,
                dateAdded
        );
        List<Task> newTasks = tasks.getValue();
        newTasks.add(task);
        taskRepository.save(task);
        tasks.setValue(newTasks);
        sortTasks();
    }

    public void removeTask(UUID id) {
        Optional<Task> taskResult = taskRepository.getTaskById(id);
        if (!taskResult.isPresent()) return;

        Task task = taskResult.get();
        task.setMarkedForDeletion(true);
        taskRepository.save(task);
        tasks.setValue(taskRepository.getTasks());
    }

    public void toggleTaskCompletion(UUID id) {
        Optional<Task> taskResult = taskRepository.getTaskById(id);
        if (!taskResult.isPresent()) return;

        Task task = taskResult.get();
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        tasks.setValue(taskRepository.getTasks());
        sortTasks();
    }

    public void sortTasks() {
        List<Task> t = tasks.getValue();
        t.sort((t1, t2) -> {
            if (taskSortingMode == TaskSortingMode.DateAdded) {
                return t1.getDateAdded().compareTo(t2.getDateAdded());
            } else if (taskSortingMode == TaskSortingMode.Deadline) {
                if (t1.getDeadline() == null) {
                    return Constants.FIRST_ARGUMENT_IS_LESSER;
                } else if (t2.getDeadline() == null) {
                    return Constants.FIRST_ARGUMENT_IS_GREATER;
                }
                return t1.getDeadline().compareTo(t2.getDeadline());
            } else {
                if (t1.getPriority() == Task.TaskPriority.NORMAL
                        && t2.getPriority() == Task.TaskPriority.IMPORTANT) {
                    return Constants.FIRST_ARGUMENT_IS_LESSER;
                } else if (t1.getPriority() == Task.TaskPriority.IMPORTANT
                        && t2.getPriority() == Task.TaskPriority.NORMAL) {
                    return Constants.FIRST_ARGUMENT_IS_GREATER;
                } else {
                    return Constants.ARGUMENTS_ARE_EQUAL;
                }
            }
        });
        if (taskSortingMode == TaskSortingMode.Importance) {
            Collections.reverse(t);
        }
        tasks.setValue(t);
    }

    public void save(Task task) {
        taskRepository.save(task);
        tasks.setValue(taskRepository.getTasks());
        sortTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public TaskSortingMode getTaskSortingMode() {
        return taskSortingMode;
    }

    public void setTaskSortingMode(TaskSortingMode taskSortingMode) {
        this.taskSortingMode = taskSortingMode;
    }

    public enum TaskSortingMode {
        DateAdded,
        Deadline,
        Importance
    }
}