package com.example.todoandroid.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoandroid.Task;
import com.example.todoandroid.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TasksViewModel extends ViewModel {
    private final TaskRepository taskRepository;
    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    @Inject
    public TasksViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        tasks.setValue(taskRepository.getTasks());
    }

    public void removeTask(UUID id) {
        Optional<Task> taskResult = taskRepository.getTaskById(id);
        if (!taskResult.isPresent()) return;

        Task task = taskResult.get();
        task.setMarkedForDeletion(true);
        taskRepository.save(task);
        tasks.setValue(taskRepository.getTasks());
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }
}