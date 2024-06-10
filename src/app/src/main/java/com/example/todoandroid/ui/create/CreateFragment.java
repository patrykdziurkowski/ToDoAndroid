package com.example.todoandroid.ui.create;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoandroid.Constants;
import com.example.todoandroid.databinding.FragmentCreateBinding;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.viewmodel.TasksViewModel;

import java.util.Calendar;
import java.util.Optional;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;
    private TasksViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        binding = FragmentCreateBinding.inflate(inflater, container, false);

        binding.createTaskDeadline.setOnClickListener((view) -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (v, y, m, d) -> {
                        binding.createTaskDeadline.setText(String.format("%s-%s-%s", y, m + Constants.INDEX_OFFSET, d));
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        binding.createTaskSubmit.setOnClickListener((view) -> {
            String title = String.valueOf(
                    binding.createTaskTitle.getText());
            String description = String.valueOf(
                    binding.createTaskDescription.getText());
            Optional<DateOnly> deadline = DateOnly.parse(String.valueOf(
                    binding.createTaskDeadline.getText()));

            if (deadline.isPresent()) {
                viewModel.addTask(
                        title,
                        description,
                        new DateOnly(),
                        deadline.get());
            } else {
                viewModel.addTask(
                        title,
                        description,
                        new DateOnly());
            }

            binding.createTaskTitle.setText("");
            binding.createTaskDescription.setText("");
            binding.createTaskDeadline.setText("");
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}