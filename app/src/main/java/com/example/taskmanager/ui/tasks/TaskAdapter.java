package com.example.taskmanager.ui.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;


import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;

import com.example.taskmanager.DatabaseHelper;
import com.example.taskmanager.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    private List<Task> taskList;
    private DatabaseHelper databaseHelper;


    // CRUD
    public void DeleteTask(Task item) {
        DeleteTask(taskList.indexOf((item)));
    }

    public void DeleteTask(int position) {
        if (position < 0 || position > taskList.size()) {
            throw new IndexOutOfBoundsException("Invalid task specified");
        }
        //TODO: database/localstorage update as well
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void changeTaskPriority(final int position, int priority){
        if (position < 0 || position > taskList.size()) {
            throw new IndexOutOfBoundsException("Invalid task specified");
        }

        if(priority < 0 || priority > 3){
            return;
        }


        Task task = taskList.get(position);
        task.priority = priority; // TODO: add private method to udpate priority;
        databaseHelper.updateData(task);
        notifyItemChanged(position);

    }

    public TaskAdapter(Context context, List<Task> taskList) {
        this.taskList = taskList;
        this.databaseHelper = new DatabaseHelper(context);
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

    public void refreshAdapter() {
        notifyDataSetChanged();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textTaskTitle;
        private TextView textTaskDescription;
        private TextView textDueDate;
        private ImageView imagePriority;

        private TextView priorityPill;
        private ImageView editButton;
        private CheckBox checkbox;


        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTaskTitle = itemView.findViewById(R.id.text_task_title);
            textTaskDescription = itemView.findViewById(R.id.text_task_description);
            textDueDate = itemView.findViewById(R.id.text_due_date);
            imagePriority = itemView.findViewById(R.id.image_priority);
            priorityPill = itemView.findViewById(R.id.priority_pill);
            editButton = itemView.findViewById(R.id.edit_button);
            checkbox = itemView.findViewById(R.id.checkBox);


//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position = getAdapterPosition();
//                    showDeleteDialog(position);
//                    return true;
//                }
//            });

            checkbox.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Task task = taskList.get(position);
                task.isComplete = checkbox.isChecked();
                databaseHelper.updateData(task);
                notifyItemChanged(position);
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                    final String[] taskOptions = {
                            "View task", "Edit task", "Set priority", "Mark complete", "Delete task"
                    };

                    Task task = taskList.get(getAdapterPosition());
                    if(task.isComplete){
                        taskOptions[3] = "Mark incomplete";
                    }

                    builder.setTitle("Task options");
                    builder.setItems(taskOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if ("View task".equals(taskOptions[which])) {
                                //TODO: send them to activity/fragment to view task as fullscreen
                            } else if ("Edit task".equals(taskOptions[which])) {

                                Intent intent = new Intent(itemView.getContext(), TaskViewActivity.class);
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_ACTION, "edit");
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_ID, task.id);
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_TITLE, task.getTitle());
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_DESC, task.getDescription());
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_DUEDATE, task.dueDateString);
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_PRIORITY, task.priority);
                                intent.putExtra(TaskViewActivity.EXTRA_TASK_COMPLETE, task.isComplete);
                                itemView.getContext().startActivity(intent);
                                //TODO: send them back to the add/edit task activity with prefilled details, option to save instead of add
                            } else if ("Mark complete".equals(taskOptions[which])) {
                                task.isComplete = true;
                                databaseHelper.updateData(task);
                                notifyItemChanged(getAdapterPosition());
                            } else if ("Mark incomplete".equals(taskOptions[which])) {
                                task.isComplete = false;
                                notifyItemChanged(getAdapterPosition());
                            } else if ("Set priority".equals(taskOptions[which])) {
                                showChangePriorityDialog(getAdapterPosition());
                            } else if ("Delete task".equals(taskOptions[which])) {
                                showDeleteDialog(getAdapterPosition());
                            }
                        }
                    });
                    builder.show();
                }
            });
        }



        private void showChangePriorityDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            final String[] priorityOptions = {
                    "None", "Low", "Medium", "High"
            };

            builder.setTitle("Set task priority");
            builder.setItems(priorityOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    changeTaskPriority(position, which);
                }
            });
            builder.show();
        }

        void bind(Task task) {
            textTaskTitle.setText(task.getTitle());
            textTaskDescription.setText(task.getDescription());
            textDueDate.setText(task.getDueDate());
            imagePriority.setImageResource(task.getPriorityIcon());
            checkbox.setChecked(task.isComplete);

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


        public void setPriorityPillBackground(int backgroundResId) {
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
