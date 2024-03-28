package com.example.taskmanager.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_item, parent, false);
        return new TaskViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);

        // Set background color based on priority
        switch (task.priority) {
            case Task.HIGH_PRIORITY:
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.high_priority_color));
                break;
            case Task.MEDIUM_PRIORITY:
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.medium_priority_color));
                break;
            case Task.LOW_PRIORITY:
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.low_priority_color));
                break;
            default:
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textTaskTitle;
        private TextView textTaskDescription;
        private TextView textDueDate;
        private ImageView imagePriority;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTaskTitle = itemView.findViewById(R.id.text_task_title);
            textTaskDescription = itemView.findViewById(R.id.text_task_description);
            textDueDate = itemView.findViewById(R.id.text_due_date);
            imagePriority = itemView.findViewById(R.id.image_priority);
        }

        void bind(Task task) {
            textTaskTitle.setText(task.getTitle());
            textTaskDescription.setText(task.getDescription());
            textDueDate.setText(task.getDueDate());
            imagePriority.setImageResource(task.getPriorityIcon());
        }
    }
}
