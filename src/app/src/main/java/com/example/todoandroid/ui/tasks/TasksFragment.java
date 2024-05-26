package com.example.todoandroid.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.CreateTaskActivity;
import com.example.todoandroid.Task;
import com.example.todoandroid.databinding.FragmentTasksBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TasksFragment extends Fragment {
    private FragmentTasksBinding binding;
    private TasksViewModel viewModel;
    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != Activity.RESULT_OK) { return; }

                    Intent intent = result.getData();
                    if (intent == null) return;
                    Bundle data = intent.getExtras();
                    if (data == null) return;

                    try {
                        viewModel.addTask(
                                data.getString("title"),
                                data.getString("description"),
                                new SimpleDateFormat("yyyy-MM-dd").parse(data.getString("deadline")),
                                new Date()
                        );
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        binding = FragmentTasksBinding.inflate(inflater, container, false);

        TasksAdapter adapter = new TasksAdapter(viewModel.getTasks().getValue());
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createTaskIntent = new Intent(getActivity(), CreateTaskActivity.class);
                someActivityResultLauncher.launch(createTaskIntent);
            }
        });
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });
        adapter.setOnClickListener(new TasksAdapter.OnClickListener() {
            public void onClick(int position, Task task) {
                viewModel.removeTask(task.getId());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}