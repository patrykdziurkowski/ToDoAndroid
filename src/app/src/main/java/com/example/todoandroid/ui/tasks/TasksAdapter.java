package com.example.todoandroid.ui.tasks;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.getTitle().setText(tasks.get(position).getTitle());
        holder.getDescription().setText(tasks.get(position).getDescription());
        boolean completionStatus = tasks.get(position).isCompleted();
        holder.setCompleted(completionStatus);

        holder.itemView.findViewById(R.id.remove_task_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderDeleteClickListener != null) {
                    holderDeleteClickListener.onClick(position, tasks.get(position));
                }
            }
        });
        holder.itemView.findViewById(R.id.mark_completed_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderCompleteClickListener != null) {
                    holderCompleteClickListener.onClick(position, tasks.get(position));
                }
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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.card_title);
            description = (TextView) view.findViewById(R.id.card_description);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

        public ViewHolder setCompleted(boolean completed) {
            int color = (completed) ? Color.GREEN : Color.GRAY;
            this.itemView.findViewById(R.id.task_card).setBackgroundColor(color);
            return this;
        }
    }

    public interface HolderClickListener {
        void onClick(int position, Task task);
    }
}
