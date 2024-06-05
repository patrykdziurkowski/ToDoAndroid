package com.example.todoandroid.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoandroid.FileManager;
import com.example.todoandroid.TaskSortingStrategyFactory;
import com.example.todoandroid.databinding.FragmentSettingsBinding;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.ui.BaseActivityResult;
import com.example.todoandroid.viewmodel.TasksViewModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private TasksViewModel viewModel;
    private FileManager fileManager;
    private final BaseActivityResult<Intent, ActivityResult> activityLauncher = BaseActivityResult.registerActivityForResult(this);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        fileManager = new FileManager(requireContext().getContentResolver());

        binding.settingsSortDateAdded.setOnClickListener((view) -> {
            viewModel.setTaskSortingMode(TaskSortingStrategyFactory.SortingMode.DateAdded);
            viewModel.sortTasks();
        });
        binding.settingsSortDeadline.setOnClickListener((view) -> {
            viewModel.setTaskSortingMode(TaskSortingStrategyFactory.SortingMode.Deadline);
            viewModel.sortTasks();
        });
        binding.settingsSortImportance.setOnClickListener((view) -> {
            viewModel.setTaskSortingMode(TaskSortingStrategyFactory.SortingMode.Importance);
            viewModel.sortTasks();
        });

        binding.settingsFileExport.setOnClickListener((view) -> {
            openCreateFileDialog();
        });
        binding.settingsFileImport.setOnClickListener((view) -> {
            openOpenFileDialog();
        });

        return binding.getRoot();
    }

    private void openCreateFileDialog() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "todo.txt");

        activityLauncher.launch(intent, this::exportTasks);
    }

    private void exportTasks(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK) return;
        if (result.getData() == null) return;

        Uri uri = result.getData().getData();
        if (uri == null) return;

        boolean isSavingSuccessful = fileManager.saveTasksToFile(viewModel.getTasks().getValue(), uri);
        if (isSavingSuccessful) {
            Toast.makeText(getContext(), "Saved the tasks to a file.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Unable to save the file.", Toast.LENGTH_LONG).show();
        }
    }

    private void openOpenFileDialog() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        activityLauncher.launch(intent, this::importTasks);
    }

    private void importTasks(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK) return;
        if (result.getData() == null) return;

        Uri uri = result.getData().getData();
        if (uri == null) return;

        Optional<List<Task>> loadResult = fileManager.loadTasksFromFile(uri);
        if (loadResult.isPresent()) {
            List<Task> tasks = loadResult.get();
            viewModel.importTasks(tasks);
            Toast.makeText(getContext(), "Loaded the tasks from a file.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Unable to read the given file.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}