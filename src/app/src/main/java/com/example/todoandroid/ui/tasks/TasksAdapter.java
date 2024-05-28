package com.example.todoandroid.ui.tasks;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.CreateTaskActivity;
import com.example.todoandroid.R;
import com.example.todoandroid.Task;
import com.example.todoandroid.databinding.FrameTaskBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {
    private List<Task> tasks;
    private HolderClickListener holderDeleteClickListener;
    private HolderClickListener holderCompleteClickListener;
    private HolderClickListener holderEditClickListener;
    private HolderClickListener holderImportantClickListener;
    private HolderClickListener holderDateClickListener;

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
        holder.binding.cardTitle.setText(currentTask.getTitle());
        holder.binding.cardDescription.setText(currentTask.getDescription());
        holder.setCompleted(currentTask.isCompleted());
        holder.setImportant(currentTask.getPriority() == Task.TaskPriority.IMPORTANT);
        holder.binding.dateAddedText.setText(currentTask.getDateAdded().toString());
        if (currentTask.getDeadline() == null) {
            holder.binding.deadlineContainer.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.deadlineContainer.setVisibility(View.VISIBLE);
            holder.binding.cardDeadlineEdit.setText(currentTask.getDeadline().toString());
            holder.binding.cardDeadlineEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            v.getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    holder.binding.cardDeadlineEdit.setText(String.format("%s-%s-%s", year, month, dayOfMonth));
                                    currentTask.setDeadline(new Date(year, month, dayOfMonth));
                                    holderDateClickListener.onClick(currentTask);
                                }
                            },
                            year, month, day);
                    datePickerDialog.show();
                }
            });
        }

        holder.binding.removeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderDeleteClickListener == null) { return; }
                holderDeleteClickListener.onClick(currentTask);
            }
        });
        holder.binding.markCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderCompleteClickListener == null) { return; }
                holderCompleteClickListener.onClick(currentTask);
            }
        });
        holder.binding.markImportantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderImportantClickListener == null) { return; }
                holderImportantClickListener.onClick(currentTask);
            }
        });

        holder.binding.cardTitle.setFocusable(false);
        holder.binding.cardTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.binding.cardTitle.setFocusableInTouchMode(true);
                return true;
            }
        });
        holder.binding.cardTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                holder.binding.cardTitle.setFocusable(false);

                if (holderEditClickListener == null) return;
                currentTask.setTitle(String.valueOf(holder.binding.cardTitle.getText()));
                holderEditClickListener.onClick(currentTask);
            }
        });

        holder.binding.cardDescription.setFocusable(false);
        holder.binding.cardDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.binding.cardDescription.setFocusableInTouchMode(true);
                return true;
            }
        });
        holder.binding.cardDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                holder.binding.cardDescription.setFocusable(false);

                if (holderEditClickListener == null) return;
                currentTask.setDescription(String.valueOf(holder.binding.cardDescription.getText()));
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

    public void setOnDateClickListener(HolderClickListener holderDateClickListener) {
        this.holderDateClickListener = holderDateClickListener;
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
            binding.markImportantButton.setBackgroundColor(color);
        }
    }

    public interface HolderClickListener {
        void onClick(Task task);
    }
}
