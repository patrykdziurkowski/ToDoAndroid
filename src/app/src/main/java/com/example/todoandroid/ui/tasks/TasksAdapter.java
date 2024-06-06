package com.example.todoandroid.ui.tasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.Constants;
import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.domain.DateOnly;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.databinding.FrameTaskBinding;
import com.example.todoandroid.ui.BaseActivityResult;
import com.example.todoandroid.ui.MainActivity;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {
    private List<Task> tasks;
    private List<Attachment> attachments;
    private TaskClickListener deleteClickListener;
    private TaskClickListener completeClickListener;
    private TaskClickListener editClickListener;
    private TaskClickListener importanceClickListener;
    private TaskClickListener dateClickListener;
    private TaskClickListener attachmentAddClickListener;
    private AttachmentsAdapter.AttachmentClickListener attachmentRemoveClickListener;

    public TasksAdapter(
            List<Task> tasks,
            List<Attachment> attachments) {
        this.tasks = tasks;
        this.attachments = attachments;
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
                dateClickListener,
                attachmentAddClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.binding.taskTitle.setText(currentTask.getTitle());
        holder.binding.taskDescription.setText(currentTask.getDescription());
        holder.setCompleted(currentTask.isCompleted());
        holder.setImportant(currentTask.getPriority() == Task.TaskPriority.IMPORTANT);
        holder.binding.taskDateAdded.setText(currentTask.getDateAdded().toString());
        if (currentTask.getDeadline().isPresent()) {
            holder.binding.taskDeadline.setText(currentTask.getDeadline().get().toString());
        } else {
            holder.binding.taskDeadline.setText("");
        }

        List<Attachment> taskAttachments = attachments.stream()
                .filter((a) -> a.getTaskId() == currentTask.getId())
                .collect(Collectors.toList());
        AttachmentsAdapter adapter = new AttachmentsAdapter(taskAttachments);
        RecyclerView recyclerView = holder.binding.taskAttachments;
        recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setAttachmentRemoveClickListener(attachmentRemoveClickListener);
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

    public void setAttachmentAddClickListener(TaskClickListener attachmentAddClickListener) {
        this.attachmentAddClickListener = attachmentAddClickListener;
    }

    public void setAttachmentRemoveClickListener(AttachmentsAdapter.AttachmentClickListener attachmentRemoveClickListener) {
        this.attachmentRemoveClickListener = attachmentRemoveClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
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
                TaskClickListener dateClickListener,
                TaskClickListener attachmentAddClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.taskAddAttachment.setOnClickListener((view) -> {
                if (attachmentAddClickListener == null) return;
                Task task = tasks.get(getAdapterPosition());
                attachmentAddClickListener.onClick(task);
            });

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
