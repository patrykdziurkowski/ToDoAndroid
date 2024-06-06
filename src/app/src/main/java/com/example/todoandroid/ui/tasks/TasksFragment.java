package com.example.todoandroid.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.ui.BaseActivityResult;
import com.example.todoandroid.ui.CreateTaskActivity;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.databinding.FragmentTasksBinding;
import com.example.todoandroid.viewmodel.TasksViewModel;

import java.util.Optional;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TasksFragment extends Fragment {
    private FragmentTasksBinding binding;
    private TasksViewModel viewModel;
    private final BaseActivityResult<Intent, ActivityResult> activityLauncher = BaseActivityResult.registerActivityForResult(this);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = FragmentTasksBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupTaskCreationActivityLauncher();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        TasksAdapter adapter = new TasksAdapter(
                viewModel.getTasks().getValue(),
                viewModel.getAttachments().getValue());
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getTasks().observe(getViewLifecycleOwner(), adapter::setTasks);
        viewModel.getAttachments().observe(getViewLifecycleOwner(), adapter::setAttachments);

        adapter.setDeleteClickListener((task) -> {
            viewModel.removeTask(task.getId());
        });
        adapter.setCompleteClickListener((task) -> {
            viewModel.toggleTaskCompletion(task.getId());
        });
        adapter.setEditClickListener((task) -> {
            viewModel.save(task);
        });
        adapter.setImportanceClickListener((task) -> {
            Task.TaskPriority priority = (task.getPriority() == Task.TaskPriority.NORMAL) ?
                    Task.TaskPriority.IMPORTANT :
                    Task.TaskPriority.NORMAL;
            task.setPriority(priority);
            viewModel.save(task);
        });
        adapter.setDateClickListener((task) -> {
            viewModel.save(task);
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri == null) return;
                    viewModel.addAttachment(uri);
                });
        adapter.setAttachmentAddClickListener((task) -> {
            viewModel.rememberTaskToAttachTo(task.getId());
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void setupTaskCreationActivityLauncher() {
        binding.tasksAdd.setOnClickListener((view) -> {
            Intent createTaskIntent = new Intent(getActivity(), CreateTaskActivity.class);
            activityLauncher.launch(createTaskIntent, this::createTaskResult);
        });
    }

    private void createTaskResult(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK) {
            return;
        }

        Intent intent = result.getData();
        if (intent == null) return;
        Bundle data = intent.getExtras();
        if (data == null) return;

        Optional<DateOnly> parseResult = DateOnly.parse(data.getString("deadline"));
        if (!parseResult.isPresent()) {
            viewModel.addTask(
                    data.getString("title"),
                    data.getString("description"),
                    new DateOnly());
            return;
        }

        viewModel.addTask(
                data.getString("title"),
                data.getString("description"),
                parseResult.get(),
                new DateOnly());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}