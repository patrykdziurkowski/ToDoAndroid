package com.example.todoandroid.ui.tasks;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.domain.Task;
import com.example.todoandroid.databinding.FrameTaskBinding;

import java.util.List;
import java.util.stream.Collectors;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {
    private List<Task> tasks;
    private List<Attachment> attachments;
    private OnClickListener deleteClickListener;
    private OnClickListener completeClickListener;
    private OnClickListener descriptionClickListener;
    private OnClickListener titleClickListener;
    private OnClickListener importanceClickListener;
    private OnClickListener dateClickListener;
    private OnClickListener attachmentAddClickListener;
    private OnClickListener attachmentRemoveClickListener;

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
                deleteClickListener,
                completeClickListener,
                descriptionClickListener,
                titleClickListener,
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
                .filter((a) -> currentTask.getId().equals(a.getTaskId()))
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

    public void setDeleteClickListener(OnClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public void setCompleteClickListener(OnClickListener completeClickListener) {
        this.completeClickListener = completeClickListener;
    }

    public void setDescriptionClickListener(OnClickListener descriptionClickListener) {
        this.descriptionClickListener = descriptionClickListener;
    }

    public void setTitleClickListener(OnClickListener titleClickListener) {
        this.titleClickListener = titleClickListener;
    }

    public void setImportanceClickListener(OnClickListener importanceClickListener) {
        this.importanceClickListener = importanceClickListener;
    }

    public void setDateClickListener(OnClickListener dateClickListener) {
        this.dateClickListener = dateClickListener;
    }

    public void setAttachmentAddClickListener(OnClickListener attachmentAddClickListener) {
        this.attachmentAddClickListener = attachmentAddClickListener;
    }

    public void setAttachmentRemoveClickListener(OnClickListener attachmentRemoveClickListener) {
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
                OnClickListener deleteClickListener,
                OnClickListener completeClickListener,
                OnClickListener descriptionClickListener,
                OnClickListener titleClickListener,
                OnClickListener importanceClickListener,
                OnClickListener dateClickListener,
                OnClickListener attachmentAddClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.taskAddAttachment.setOnClickListener((view) -> {
                if (attachmentAddClickListener == null) return;
                attachmentAddClickListener.onClick(view, getAdapterPosition());
            });

            binding.taskDeadline.setOnLongClickListener((view) -> {
                if (dateClickListener == null) return false;
                dateClickListener.onClick(view, getAdapterPosition());
                return true;
            });

            binding.taskRemove.setOnClickListener((view) -> {
                if (deleteClickListener == null) { return; }
                deleteClickListener.onClick(view, getAdapterPosition());
            });

            binding.taskComplete.setOnClickListener((view) -> {
                if (completeClickListener == null) { return; }
                completeClickListener.onClick(view, getAdapterPosition());
            });

            binding.taskImportant.setOnClickListener((view) -> {
                if (importanceClickListener == null) { return; }
                importanceClickListener.onClick(view, getAdapterPosition());
            });

            binding.taskTitle.setFocusable(false);
            binding.taskTitle.setOnLongClickListener((view) -> {
                binding.taskTitle.setFocusableInTouchMode(true);
                return true;
            });
            binding.taskTitle.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) return;
                binding.taskTitle.setFocusable(false);

                if (titleClickListener == null) return;
                titleClickListener.onClick(view, getAdapterPosition());
            });

            binding.taskDescription.setFocusable(false);
            binding.taskDescription.setOnLongClickListener((view) -> {
                binding.taskDescription.setFocusableInTouchMode(true);
                return true;
            });
            binding.taskDescription.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) return;
                binding.taskDescription.setFocusable(false);

                if (descriptionClickListener == null) return;
                descriptionClickListener.onClick(view, getAdapterPosition());
            });

            binding.taskCollapseAttachments.setOnClickListener((view) -> {
                int visibility = binding.taskAttachmentsContainer.getVisibility();

                if (visibility == View.GONE) {
                    binding.taskAttachmentsContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.taskAttachmentsContainer.setVisibility(View.GONE);
                }
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

    public interface OnClickListener {
        void onClick(View view, int position);
    }
}
