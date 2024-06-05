package com.example.todoandroid;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class FileManager {
    private final ContentResolver contentResolver;

    public FileManager(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public boolean saveTasksToFile(List<Task> tasks, Uri uri) {
        try (ParcelFileDescriptor descriptor = contentResolver.openFileDescriptor(uri, "w");
             FileOutputStream fileOutputStream = new FileOutputStream(descriptor.getFileDescriptor())) {
            for (Task task : tasks) {
                fileOutputStream.write(formatTaskAsString(task).getBytes());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Optional<List<Task>> loadTasksFromFile(Uri uri) {
        try (InputStream inputStream = contentResolver.openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            List<Task> tasks = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = readTaskFromString(line);
                tasks.add(task);
            }
            return Optional.of(tasks);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private Task readTaskFromString(String line) {
        String[] fields = line.split("\";\"");
        Task task = new Task(
                UUID.randomUUID(),
                fields[1],
                fields[2],
                DateOnly.parse(fields[4]).get(),
                (fields[5].equals("Never")) ? null : DateOnly.parse(fields[5]).get()
        );
        task.setCompleted(fields[0].equals("true"));
        task.setPriority((fields[3].equals("IMPORTANT")) ? Task.TaskPriority.IMPORTANT : Task.TaskPriority.NORMAL);
        return task;
    }

    private String formatTaskAsString(Task task) {
        return (String.join("\";\"",
                        task.isCompleted() ? "true" : "false",
                        task.getTitle(),
                        task.getDescription(),
                        task.getPriority() == Task.TaskPriority.IMPORTANT ? "IMPORTANT" : "NORMAL",
                        task.getDateAdded().toString(),
                        (task.getDeadline().isPresent()) ? task.getDeadline().get().toString() : "Never")
                + "\n");
    }
}
