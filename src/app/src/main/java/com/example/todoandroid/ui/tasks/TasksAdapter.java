package com.example.todoandroid.ui.tasks;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.R;
import com.example.todoandroid.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private List<Task> tasks;
    private HolderClickListener holderDeleteClickListener;
    private HolderClickListener holderCompleteClickListener;
    private HolderClickListener holderEditClickListener;

    public TasksAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.ViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.getTitle().setText(currentTask.getTitle());
        holder.getDescription().setText(currentTask.getDescription());
        holder.setCompleted(currentTask.isCompleted());

        holder.itemView.findViewById(R.id.remove_task_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderDeleteClickListener != null) {
                    holderDeleteClickListener.onClick(currentTask);
                }
            }
        });
        holder.itemView.findViewById(R.id.mark_completed_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderCompleteClickListener != null) {
                    holderCompleteClickListener.onClick(currentTask);
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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final EditText title;
        private final EditText description;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.card_title);
            description = view.findViewById(R.id.card_description);
        }

        public EditText getTitle() {
            return title;
        }

        public EditText getDescription() {
            return description;
        }

        public ViewHolder setCompleted(boolean completed) {
            int color = (completed) ? Color.GREEN : Color.GRAY;
            this.itemView.findViewById(R.id.task_card).setBackgroundColor(color);
            return this;
        }
    }

    public interface HolderClickListener {
        void onClick(Task task);
    }
}
