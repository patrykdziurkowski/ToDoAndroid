package com.example.todoandroid.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoandroid.databinding.FragmentSettingsBinding;
import com.example.todoandroid.ui.TasksViewModel;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private TasksViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        binding.settingsSortDateAdded.setOnClickListener((view) -> {
            viewModel.setTaskSortingMode(TasksViewModel.TaskSortingMode.DateAdded);
            viewModel.sortTasks();
        });
        binding.settingsSortDeadline.setOnClickListener((view) -> {
            viewModel.setTaskSortingMode(TasksViewModel.TaskSortingMode.Deadline);
            viewModel.sortTasks();
        });
        binding.settingsSortImportance.setOnClickListener((view) -> {
            viewModel.setTaskSortingMode(TasksViewModel.TaskSortingMode.Importance);
            viewModel.sortTasks();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}