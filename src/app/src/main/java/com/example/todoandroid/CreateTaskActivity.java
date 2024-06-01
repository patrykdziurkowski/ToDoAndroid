package com.example.todoandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todoandroid.databinding.ActivityCreateTaskBinding;
import com.example.todoandroid.databinding.FragmentTasksBinding;

import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity {
    private ActivityCreateTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateTaskBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.createTaskDeadline.setOnClickListener((view) -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateTaskActivity.this,
                    (v, y, m, d) -> {
                        binding.createTaskDeadline.setText(String.format("%s-%s-%s", y  - Constants.DATE_YEAR_OFFSET, m, d));
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        binding.createTaskSubmit.setOnClickListener((view) -> {
            Intent intent = new Intent();
            Bundle data = new Bundle();
            data.putString("title", String.valueOf(binding.createTaskTitle.getText()));
            data.putString("description", String.valueOf(binding.createTaskDescription.getText()));
            data.putString("deadline", String.valueOf(binding.createTaskDeadline.getText()));
            intent.putExtras(data);
            setResult(RESULT_OK, intent);
            finish();
        });

        binding.createTaskCancel.setOnClickListener((view) -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}