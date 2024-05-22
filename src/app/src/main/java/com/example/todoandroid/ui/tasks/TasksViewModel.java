package com.example.todoandroid.ui.tasks;

import androidx.lifecycle.ViewModel;

import com.example.todoandroid.Task;
import com.example.todoandroid.TaskRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TasksViewModel extends ViewModel {
    public TaskRepository taskRepository;
    @Inject
    public TasksViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        List<Task> tasks = this.taskRepository.getTasks();
    }

}