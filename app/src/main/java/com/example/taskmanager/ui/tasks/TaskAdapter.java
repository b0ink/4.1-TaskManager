package com.example.taskmanager.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;

import com.example.taskmanager.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    private List<Task> taskList;


    // CRUD
    public void DeleteTask(Task item){
        DeleteTask(taskList.indexOf((item)));
    }

    public void DeleteTask(int position){
        if(position < 0 || position > taskList.size()){
            throw new IndexOutOfBoundsException("Invalid task specified");
        }
        taskList.remove(position);
        notifyItemRemoved(position);
    }

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
                holder.setPriorityPillBackground(R.drawable.priority_pill_background_high);
                break;
            case Task.MEDIUM_PRIORITY:
                holder.setPriorityPillBackground(R.drawable.priority_pill_background_medium);
                break;
            case Task.LOW_PRIORITY:
                holder.setPriorityPillBackground(R.drawable.priority_pill_background_low);
                break;
            default:
                // Hide priority pill if there is no priority set
                holder.hidePriorityPill();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textTaskTitle;
        private TextView textTaskDescription;
        private TextView textDueDate;
        private ImageView imagePriority;

        private TextView priorityPill;


        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTaskTitle = itemView.findViewById(R.id.text_task_title);
            textTaskDescription = itemView.findViewById(R.id.text_task_description);
            textDueDate = itemView.findViewById(R.id.text_due_date);
            imagePriority = itemView.findViewById(R.id.image_priority);
            priorityPill = itemView.findViewById(R.id.priority_pill);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    showDeleteDialog(position);
                    return true;
                }
            });
        }


        void bind(Task task) {
            textTaskTitle.setText(task.getTitle());
            textTaskDescription.setText(task.getDescription());
            textDueDate.setText(task.getDueDate());
            imagePriority.setImageResource(task.getPriorityIcon());

            if (task.priority != Task.NO_PRIORITY) {
                priorityPill.setVisibility(View.VISIBLE);
                switch (task.priority) {
                    case Task.HIGH_PRIORITY:
                        priorityPill.setText("High");
                        break;
                    case Task.MEDIUM_PRIORITY:
                        priorityPill.setText("Medium");
                        break;
                    case Task.LOW_PRIORITY:
                        priorityPill.setText("Low");
                        break;
                }
            } else {
                hidePriorityPill();
            }
        }


        public void setPriorityPillBackground( int backgroundResId) {
            priorityPill.setBackgroundResource(backgroundResId);
            priorityPill.setVisibility(View.VISIBLE);
        }

        public void hidePriorityPill() {
            priorityPill.setVisibility(View.GONE);
        }

        private void showDeleteDialog(final int position) {
            // consider BottomSheetDialog for multiple options
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            taskList.remove(position);
//                            notifyItemRemoved(position);
                            DeleteTask(position);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


}
