package com.example.todoandroid.ui.tasks;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.Constants;
import com.example.todoandroid.DateOnly;
import com.example.todoandroid.Task;
import com.example.todoandroid.databinding.FrameTaskBinding;

import java.util.Calendar;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {
    private List<Task> tasks;
    private TaskClickListener deleteClickListener;
    private TaskClickListener completeClickListener;
    private TaskClickListener editClickListener;
    private TaskClickListener importanceClickListener;
    private TaskClickListener dateClickListener;

    public TasksAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameTaskBinding holderBinding = FrameTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new TaskHolder(holderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.binding.taskTitle.setText(currentTask.getTitle());
        holder.binding.taskDescription.setText(currentTask.getDescription());
        holder.setCompleted(currentTask.isCompleted());
        holder.setImportant(currentTask.getPriority() == Task.TaskPriority.IMPORTANT);
        holder.binding.taskDateAdded.setText(currentTask.getDateAdded().toString());
        if (currentTask.getDeadline() == null) {
            holder.binding.taskDeadline.setText("");
        } else {
            holder.binding.taskDeadline.setText(currentTask.getDeadline().toString());
        }

        holder.binding.taskDeadline.setOnLongClickListener((view) -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    (v, y, m, d) -> {
                        currentTask.setDeadline(new DateOnly(y, m + Constants.INDEX_OFFSET, d));
                        dateClickListener.onClick(currentTask);
                    },
                    year, month, day);
            datePickerDialog.show();
            return true;
        });

        holder.binding.taskRemove.setOnClickListener((view) -> {
            if (deleteClickListener == null) { return; }
            deleteClickListener.onClick(currentTask);
        });

        holder.binding.taskComplete.setOnClickListener((view) -> {
            if (completeClickListener == null) { return; }
            completeClickListener.onClick(currentTask);
        });

        holder.binding.taskImportant.setOnClickListener((view) -> {
            if (importanceClickListener == null) { return; }
            importanceClickListener.onClick(currentTask);
        });

        holder.binding.taskTitle.setFocusable(false);
        holder.binding.taskTitle.setOnLongClickListener((view) -> {
                holder.binding.taskTitle.setFocusableInTouchMode(true);
                return true;
        });
        holder.binding.taskTitle.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) return;
            holder.binding.taskTitle.setFocusable(false);

            if (editClickListener == null) return;
            currentTask.setTitle(String.valueOf(holder.binding.taskTitle.getText()));
            editClickListener.onClick(currentTask);
        });

        holder.binding.taskDescription.setFocusable(false);
        holder.binding.taskDescription.setOnLongClickListener((view) -> {
            holder.binding.taskDescription.setFocusableInTouchMode(true);
            return true;
        });
        holder.binding.taskDescription.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) return;
            holder.binding.taskDescription.setFocusable(false);

            if (editClickListener == null) return;
            currentTask.setDescription(String.valueOf(holder.binding.taskDescription.getText()));
            editClickListener.onClick(currentTask);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setDeleteClickListener(TaskClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public void setCompleteClickListener(TaskClickListener completeClickListener) {
        this.completeClickListener = completeClickListener;
    }

    public void setEditClickListener(TaskClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setImportanceClickListener(TaskClickListener importanceClickListener) {
        this.importanceClickListener = importanceClickListener;
    }

    public void setDateClickListener(TaskClickListener dateClickListener) {
        this.dateClickListener = dateClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {
        public final FrameTaskBinding binding;

        public TaskHolder(FrameTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setCompleted(boolean completed) {
            int color = (completed) ? Color.GREEN : Color.GRAY;
            binding.taskCard.setBackgroundColor(color);
        }

        public void setImportant(boolean important) {
            int color = (important) ? Color.RED : Color.LTGRAY;
            binding.taskImportant.setBackgroundColor(color);
        }
    }

    public interface TaskClickListener {
        void onClick(Task task);
    }
}
