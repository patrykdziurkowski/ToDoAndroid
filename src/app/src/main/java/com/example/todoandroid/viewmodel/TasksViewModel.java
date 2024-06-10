package com.example.todoandroid.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoandroid.TaskSortingStrategyFactory;
import com.example.todoandroid.dataaccess.AttachmentDao;
import com.example.todoandroid.dataaccess.TaskDao;
import com.example.todoandroid.dataaccess.TasksDatabase;
import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TasksViewModel extends ViewModel {
    private final TaskDao taskDao;
    private final AttachmentDao attachmentDao;
    private final TaskSortingStrategyFactory sortingStrategyFactory;

    private UUID taskToAttachTo;
    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private final MutableLiveData<List<Attachment>> attachments = new MutableLiveData<>();
    private TaskSortingStrategyFactory.SortingMode taskSortingMode = TaskSortingStrategyFactory.SortingMode.Deadline;

    @Inject
    public TasksViewModel(
            TaskSortingStrategyFactory sortingStrategyFactory,
            TasksDatabase database) {
        this.sortingStrategyFactory = sortingStrategyFactory;
        this.taskDao = database.taskDao();
        this.attachmentDao = database.attachmentDao();

        tasks.setValue(taskDao.getTasks());
        attachments.setValue(attachmentDao.getAttachments());
        sortTasks();
    }

    public void addTask(
            String title,
            String description,
            DateOnly dateAdded,
            DateOnly deadline) {
        Task task = new Task(
                UUID.randomUUID(),
                title,
                description,
                dateAdded,
                deadline
        );
        taskDao.insert(task);
        tasks.setValue(taskDao.getTasks());
        sortTasks();
    }

    public void addTask(
            String title,
            String description,
            DateOnly dateAdded) {
        addTask(title, description, dateAdded, null);
    }

    public void importTasks(List<Task> importedTasks) {
        for (Task task : importedTasks) {
            taskDao.insert(task);
        }
        tasks.setValue(taskDao.getTasks());
        sortTasks();
    }

    public void removeTask(UUID id) {
        Task task = taskDao.getTaskById(id);
        if (task == null) return;

        taskDao.delete(task);
        tasks.setValue(taskDao.getTasks());
        sortTasks();

        for (Attachment attachment : attachments.getValue()) {
            if (attachment.getTaskId() != id) continue;
            attachmentDao.delete(attachment);
        }
    }

    public void toggleTaskCompletion(UUID id) {
        Task task = taskDao.getTaskById(id);
        if (task == null) return;

        task.setCompleted(!task.isCompleted());
        taskDao.update(task);
        tasks.setValue(taskDao.getTasks());
        sortTasks();
    }

    public void sortTasks() {
        List<Task> t = tasks.getValue();
        t.sort(sortingStrategyFactory.getSortingStrategy(taskSortingMode));
        tasks.setValue(t);
    }

    public void save(Task task) {
        taskDao.update(task);
        tasks.setValue(taskDao.getTasks());
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
        attachmentDao.insert(attachment);
        attachments.setValue(attachmentDao.getAttachments());
    }

    public void removeAttachment(UUID attachmentId) {
        List<Attachment> currentAttachments = attachments.getValue();
        Attachment attachment = currentAttachments.stream()
                .filter(a -> a.getId() == attachmentId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        currentAttachments.remove(attachment);
        attachments.setValue(currentAttachments);

        attachmentDao.delete(attachment);
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