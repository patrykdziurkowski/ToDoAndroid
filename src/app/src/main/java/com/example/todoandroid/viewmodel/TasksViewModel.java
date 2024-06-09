package com.example.todoandroid.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.repository.AttachmentRepository;
import com.example.todoandroid.repository.TaskRepository;
import com.example.todoandroid.TaskSortingStrategyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TasksViewModel extends ViewModel {
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final TaskSortingStrategyFactory sortingStrategyFactory;
    private UUID taskToAttachTo;

    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private final MutableLiveData<List<Attachment>> attachments = new MutableLiveData<>();
    private TaskSortingStrategyFactory.SortingMode taskSortingMode = TaskSortingStrategyFactory.SortingMode.Deadline;

    @Inject
    public TasksViewModel(
            TaskRepository taskRepository,
            AttachmentRepository attachmentRepository,
            TaskSortingStrategyFactory sortingStrategyFactory) {
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.sortingStrategyFactory = sortingStrategyFactory;

        tasks.setValue(taskRepository.getTasks());
        attachments.setValue(attachmentRepository.getAttachments());
        sortTasks();
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

    public void importTasks(List<Task> importedTasks) {
        for (Task task : importedTasks) {
            taskRepository.save(task);
        }
    }

    public void removeTask(UUID id) {
        Optional<Task> taskResult = taskRepository.getTaskById(id);
        if (!taskResult.isPresent()) return;

        Task task = taskResult.get();
        task.setMarkedForDeletion(true);
        taskRepository.save(task);
        tasks.setValue(taskRepository.getTasks());

        for (Attachment attachment : attachments.getValue()) {
            if (attachment.getTaskId() != id) continue;
            attachment.setMarkedForDeletion(true);
            attachmentRepository.save(attachment);
        }
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
        t.sort(sortingStrategyFactory.getSortingStrategy(taskSortingMode));
        tasks.setValue(t);
    }

    public void save(Task task) {
        taskRepository.save(task);
        tasks.setValue(taskRepository.getTasks());
        sortTasks();
    }

    public void rememberTaskToAttachTo(UUID taskId) {
        taskToAttachTo = taskId;
    }

    public void addAttachment(Uri uri) {
        Attachment attachment = new Attachment(
                UUID.randomUUID(),
                taskToAttachTo,
                uri
        );
        List<Attachment> currentAttachments = attachments.getValue();
        currentAttachments.add(attachment);
        attachments.setValue(currentAttachments);
        attachmentRepository.save(attachment);
    }

    public void removeAttachment(UUID attachmentId) {
        List<Attachment> currentAttachments = attachments.getValue();
        Attachment attachment = currentAttachments.stream()
                .filter(a -> a.getId() == attachmentId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        currentAttachments.remove(attachment);
        attachments.setValue(currentAttachments);

        attachment.setMarkedForDeletion(true);
        attachmentRepository.save(attachment);
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<List<Attachment>> getAttachments() {
        return attachments;
    }

    public TaskSortingStrategyFactory.SortingMode getTaskSortingMode() {
        return taskSortingMode;
    }

    public void setTaskSortingMode(TaskSortingStrategyFactory.SortingMode taskSortingMode) {
        this.taskSortingMode = taskSortingMode;
    }
}