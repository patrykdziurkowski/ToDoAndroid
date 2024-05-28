package com.example.todoandroid.ui.tasks;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.R;
import com.example.todoandroid.Task;
import com.example.todoandroid.databinding.FrameTaskBinding;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {
    private List<Task> tasks;
    private HolderClickListener holderDeleteClickListener;
    private HolderClickListener holderCompleteClickListener;
    private HolderClickListener holderEditClickListener;
    private HolderClickListener holderImportantClickListener;

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
        holder.getTitle().setText(currentTask.getTitle());
        holder.getDescription().setText(currentTask.getDescription());
        holder.setCompleted(currentTask.isCompleted());
        holder.setImportant(currentTask.getPriority() == Task.TaskPriority.IMPORTANT);

        holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderDeleteClickListener != null) {
                    holderDeleteClickListener.onClick(currentTask);
                }
            }
        });
        holder.getCompleteToggle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderCompleteClickListener != null) {
                    holderCompleteClickListener.onClick(currentTask);
                }
            }
        });
        holder.getImportantToggle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderImportantClickListener != null) {
                    holderImportantClickListener.onClick(currentTask);
                }
            }
        });

        holder.getTitle().setFocusable(false);
        holder.getTitle().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.getTitle().setFocusableInTouchMode(true);
                return true;
            }
        });
        holder.getTitle().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                holder.getTitle().setFocusable(false);

                if (holderEditClickListener == null) return;
                currentTask.setTitle(String.valueOf(holder.getTitle().getText()));
                holderEditClickListener.onClick(currentTask);
            }
        });

        holder.getDescription().setFocusable(false);
        holder.getDescription().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.getDescription().setFocusableInTouchMode(true);
                return true;
            }
        });
        holder.getDescription().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                holder.getDescription().setFocusable(false);

                if (holderEditClickListener == null) return;
                currentTask.setDescription(String.valueOf(holder.getDescription().getText()));
                holderEditClickListener.onClick(currentTask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setOnDeleteClickListener(HolderClickListener holderDeleteClickListener) {
        this.holderDeleteClickListener = holderDeleteClickListener;
    }

    public void setOnCompleteClickListener(HolderClickListener holderCompleteClickListener) {
        this.holderCompleteClickListener = holderCompleteClickListener;
    }

    public void setOnEditClickListener(HolderClickListener holderEditClickListener) {
        this.holderEditClickListener = holderEditClickListener;
    }

    public void setOnImportantClickListener(HolderClickListener holderImportantClickListener) {
        this.holderImportantClickListener = holderImportantClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {
        private final FrameTaskBinding binding;

        public TaskHolder(FrameTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public EditText getTitle() {
            return binding.cardTitle;
        }

        public EditText getDescription() {
            return binding.cardDescription;
        }

        public Button getImportantToggle() {
            return binding.markImportantButton;
        }

        public Button getCompleteToggle() {
            return binding.markCompletedButton;
        }

        public Button getDeleteButton() {
            return binding.removeTaskButton;
        }

        public void setCompleted(boolean completed) {
            int color = (completed) ? Color.GREEN : Color.GRAY;
            binding.taskCard.setBackgroundColor(color);
        }

        public void setImportant(boolean important) {
            int color = (important) ? Color.RED : Color.LTGRAY;
            binding.markImportantButton.setBackgroundColor(color);
        }
    }

    public interface HolderClickListener {
        void onClick(Task task);
    }
}
