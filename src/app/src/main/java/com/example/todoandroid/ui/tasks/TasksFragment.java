package com.example.todoandroid.ui.tasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.Constants;
import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.ui.BaseActivityResult;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.databinding.FragmentTasksBinding;
import com.example.todoandroid.viewmodel.TasksViewModel;

import java.util.Calendar;
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

        adapter.setDeleteClickListener((view, position) -> {
            Task task = viewModel.getTasks().getValue().get(position);
            viewModel.removeTask(task.getId());
        });
        adapter.setCompleteClickListener((view, position) -> {
            Task task = viewModel.getTasks().getValue().get(position);
            viewModel.toggleTaskCompletion(task.getId());
        });
        adapter.setDescriptionClickListener((view, position) -> {
            EditText editText = (EditText) view;
            Task task = viewModel.getTasks().getValue().get(position);
            task.setDescription(String.valueOf(editText.getText()));
            viewModel.save(task);
        });
        adapter.setTitleClickListener((view, position) -> {
            EditText editText = (EditText) view;
            Task task = viewModel.getTasks().getValue().get(position);
            task.setTitle(String.valueOf(editText.getText()));
            viewModel.save(task);
        });
        adapter.setImportanceClickListener((view, position) -> {
            Task task = viewModel.getTasks().getValue().get(position);
            Task.TaskPriority priority = (task.getPriority() == Task.TaskPriority.NORMAL) ?
                    Task.TaskPriority.IMPORTANT :
                    Task.TaskPriority.NORMAL;
            task.setPriority(priority);
            viewModel.save(task);
        });
        adapter.setDateClickListener((view, position) -> {
            Task task = viewModel.getTasks().getValue().get(position);
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    (v, y, m, d) -> {
                        task.setDeadline(new DateOnly(y, m + Constants.INDEX_OFFSET, d));
                        viewModel.save(task);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri == null) return;

                    int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    getContext().getContentResolver().takePersistableUriPermission(uri, flags);
                    viewModel.addAttachment(uri);
                });
        adapter.setAttachmentAddClickListener((view, position) -> {
            Task task = viewModel.getTasks().getValue().get(position);
            viewModel.rememberTaskToAttachTo(task.getId());
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        adapter.setAttachmentRemoveClickListener((view, position) -> {
            Attachment attachment = viewModel.getAttachments().getValue().get(position);
            viewModel.removeAttachment(attachment.getId());

            int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            getContext().getContentResolver()
                    .releasePersistableUriPermission(
                            attachment.getUri(),
                            flags);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}