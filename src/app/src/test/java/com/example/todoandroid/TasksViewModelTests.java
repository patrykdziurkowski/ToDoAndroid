package com.example.todoandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.repository.AttachmentRepository;
import com.example.todoandroid.repository.TaskRepository;
import com.example.todoandroid.viewmodel.TasksViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(RobolectricTestRunner.class)
public class TasksViewModelTests {
    private TaskRepository taskRepository;
    private AttachmentRepository attachmentRepository;
    private TasksViewModel tasksViewModel;

    @Before
    public void setup() {
        taskRepository = mock(TaskRepository.class);
        attachmentRepository = mock(AttachmentRepository.class);
        tasksViewModel = new TasksViewModel(
                taskRepository,
                attachmentRepository,
                new TaskSortingStrategyFactory()
        );
    }

    @Test
    public void addTask_withNoDeadline_addsTask() {
        when(taskRepository.getTasks()).thenReturn(new ArrayList<>());
        when(attachmentRepository.getAttachments()).thenReturn(new ArrayList<>());

        tasksViewModel.addTask(
                "Title",
                "Description",
                new DateOnly(2022, 6, 23));

        assertEquals(1, tasksViewModel.getTasks().getValue().size());
    }

    @Test
    public void addTask_withDeadline_addsTask() {
        when(taskRepository.getTasks()).thenReturn(new ArrayList<>());
        when(attachmentRepository.getAttachments()).thenReturn(new ArrayList<>());

        tasksViewModel.addTask(
                "Title",
                "Description",
                new DateOnly(2022, 8, 25),
                new DateOnly(2022, 6, 23));

        assertEquals(1, tasksViewModel.getTasks().getValue().size());
    }

    @Test
    public void removeTask_givenExistingUUID_removesItsTask() {
        tasksViewModel.addTask(
                "Title",
                "Description",
                new DateOnly(2023, 6, 23));
        Task taskToDelete = tasksViewModel.getTasks().getValue().get(0);
        UUID id = taskToDelete.getId();
        when(taskRepository.getTaskById(any(UUID.class)))
                .thenReturn(Optional.of(taskToDelete));

        tasksViewModel.removeTask(id);

        assertEquals(0, tasksViewModel.getTasks().getValue().size());
    }

    @Test
    public void removeTask_givenNonExistantUUID_doesntRemoveAnyTask() {
        tasksViewModel.addTask(
                "Title",
                "Description",
                new DateOnly(2023, 6, 23));
        UUID id = UUID.randomUUID();
        when(taskRepository.getTaskById(any(UUID.class)))
                .thenReturn(Optional.empty());

        tasksViewModel.removeTask(id);

        assertEquals(1, tasksViewModel.getTasks().getValue().size());
    }

    @Test
    public void toggleTaskCompletion_togglesIsCompletedProperty() {
        tasksViewModel.addTask(
                "Title",
                "Description",
                new DateOnly(2023, 6, 23));
        Task task = tasksViewModel.getTasks().getValue().get(0);
        when(taskRepository.getTaskById(any(UUID.class)))
                .thenReturn(Optional.of(task));

        boolean isCompleted1 = task.isCompleted();
        tasksViewModel.toggleTaskCompletion(task.getId());
        boolean isCompleted2 = task.isCompleted();
        tasksViewModel.toggleTaskCompletion(task.getId());
        boolean isCompleted3 = task.isCompleted();

        assertNotEquals(isCompleted1, isCompleted2);
        assertNotEquals(isCompleted2, isCompleted3);
    }
    
}
