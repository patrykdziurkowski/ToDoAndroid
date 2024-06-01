package com.example.todoandroid.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.BaseActivityResult;
import com.example.todoandroid.Constants;
import com.example.todoandroid.CreateTaskActivity;
import com.example.todoandroid.DateOnly;
import com.example.todoandroid.Task;
import com.example.todoandroid.databinding.FragmentTasksBinding;
import com.example.todoandroid.ui.TasksViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        TasksAdapter adapter = new TasksAdapter(viewModel.getTasks().getValue());
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getTasks().observe(getViewLifecycleOwner(), adapter::setTasks);

        adapter.setOnDeleteClickListener((task) -> {
            viewModel.removeTask(task.getId());
        });
        adapter.setOnCompleteClickListener((task) -> {
            viewModel.toggleTaskCompletion(task.getId());
        });
        adapter.setOnEditClickListener((task) -> {
            viewModel.save(task);
        });
        adapter.setOnImportantClickListener((task) -> {
            Task.TaskPriority priority = (task.getPriority() == Task.TaskPriority.NORMAL) ?
                    Task.TaskPriority.IMPORTANT :
                    Task.TaskPriority.NORMAL;
            task.setPriority(priority);
            viewModel.save(task);
        });
        adapter.setOnDateClickListener((task) -> {
            viewModel.save(task);
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