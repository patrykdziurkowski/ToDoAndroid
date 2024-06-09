package com.example.todoandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.repository.AttachmentRepository;
import com.example.todoandroid.repository.TaskRepository;
import com.example.todoandroid.viewmodel.TasksViewModel;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModelTests {
    @Test
    public void addTask_withNoDeadline_addsTask() {
        TaskRepository taskRepository = mock(TaskRepository.class);
        AttachmentRepository attachmentRepository = mock(AttachmentRepository.class);
        TasksViewModel tasksViewModel = new TasksViewModel(
                taskRepository,
                attachmentRepository,
                new TaskSortingStrategyFactory()
        );
        when(taskRepository.getTasks()).thenReturn(new ArrayList<>());
        when(attachmentRepository.getAttachments()).thenReturn(new ArrayList<>());

        tasksViewModel.addTask(
                "Title",
                "Description",
                new DateOnly(2022, 6, 23));

        assertEquals(1, tasksViewModel.getTasks().getValue().size());
    }
}
