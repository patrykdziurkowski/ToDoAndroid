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

        return new TaskHolder(
                holderBinding,
                tasks,
                deleteClickListener,
                completeClickListener,
                editClickListener,
                importanceClickListener,
                dateClickListener);
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

        public TaskHolder(
                FrameTaskBinding binding,
                List<Task> tasks,
                TaskClickListener deleteClickListener,
                TaskClickListener completeClickListener,
                TaskClickListener editClickListener,
                TaskClickListener importanceClickListener,
                TaskClickListener dateClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.taskDeadline.setOnLongClickListener((view) -> {
                Task task = tasks.get(getAdapterPosition());
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        (v, y, m, d) -> {
                            task.setDeadline(new DateOnly(y, m + Constants.INDEX_OFFSET, d));
                            dateClickListener.onClick(task);
                        },
                        year, month, day);
                datePickerDialog.show();
                return true;
            });

            binding.taskRemove.setOnClickListener((view) -> {
                if (deleteClickListener == null) { return; }
                Task task = tasks.get(getAdapterPosition());
                deleteClickListener.onClick(task);
            });

            binding.taskComplete.setOnClickListener((view) -> {
                if (completeClickListener == null) { return; }
                Task task = tasks.get(getAdapterPosition());
                completeClickListener.onClick(task);
            });

            binding.taskImportant.setOnClickListener((view) -> {
                if (importanceClickListener == null) { return; }
                Task task = tasks.get(getAdapterPosition());
                importanceClickListener.onClick(task);
            });

            binding.taskTitle.setFocusable(false);
            binding.taskTitle.setOnLongClickListener((view) -> {
                binding.taskTitle.setFocusableInTouchMode(true);
                return true;
            });
            binding.taskTitle.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) return;
                binding.taskTitle.setFocusable(false);

                if (editClickListener == null) return;
                Task task = tasks.get(getAdapterPosition());
                task.setTitle(String.valueOf(binding.taskTitle.getText()));
                editClickListener.onClick(task);
            });

            binding.taskDescription.setFocusable(false);
            binding.taskDescription.setOnLongClickListener((view) -> {
                binding.taskDescription.setFocusableInTouchMode(true);
                return true;
            });
            binding.taskDescription.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) return;
                binding.taskDescription.setFocusable(false);

                if (editClickListener == null) return;
                Task task = tasks.get(getAdapterPosition());
                task.setDescription(String.valueOf(binding.taskDescription.getText()));
                editClickListener.onClick(task);
            });
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
