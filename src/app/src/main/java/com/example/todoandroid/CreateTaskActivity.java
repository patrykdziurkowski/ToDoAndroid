package com.example.todoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText deadlineEditText;
    private Button submitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleEditText = findViewById(R.id.title_editText);
        descriptionEditText = findViewById(R.id.description_editText);
        deadlineEditText = findViewById(R.id.deadline_editDate);
        submitButton = findViewById(R.id.finish_adding_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle data = new Bundle();
                data.putString("title", String.valueOf(titleEditText.getText()));
                data.putString("description", String.valueOf(descriptionEditText.getText()));
                data.putString("deadline", String.valueOf(deadlineEditText.getText()));
                intent.putExtras(data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}