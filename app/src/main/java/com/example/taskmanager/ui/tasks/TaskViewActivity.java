package com.example.taskmanager.ui.tasks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanager.DatabaseHelper;
import com.example.taskmanager.R;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class TaskViewActivity extends AppCompatActivity {


    public static final String EXTRA_TASK_ACTION = "task_action";
    public static final String EXTRA_TASK_TITLE = "task_title";
    public static final String EXTRA_TASK_ID = "task_id";
    public static final String EXTRA_TASK_DESC = "task_description";
    public static final String EXTRA_TASK_DUEDATE = "task_duedate";
    public static final String EXTRA_TASK_PRIORITY = "task_priority";
    public static final String EXTRA_TASK_COMPLETE = "task_complete";



    private ImageButton closeButton;
    private TextView titleTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePicker datePicker;
    private Spinner prioritySpinner;
    private Button addButton;


    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);


        closeButton = findViewById(R.id.button_close);
        titleTextView = findViewById(R.id.text_add_new_task_title);
        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);
        datePicker = findViewById(R.id.date_picker);
        prioritySpinner = findViewById(R.id.spinner_priority);
        addButton = findViewById(R.id.button_add_task);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        priorityAdapter.add("Priority: None");
        priorityAdapter.add("Priority: Low");
        priorityAdapter.add("Priority: Medium");
        priorityAdapter.add("Priority: High");
        prioritySpinner.setAdapter(priorityAdapter);

        task = null;

        Intent resultsIntent = getIntent();
        if (resultsIntent != null) {
            String action = resultsIntent.getStringExtra(TaskViewActivity.EXTRA_TASK_ACTION);
            if (action.equals("edit")) {
                String taskTitle = resultsIntent.getStringExtra(TaskViewActivity.EXTRA_TASK_TITLE);
                String taskDesc = resultsIntent.getStringExtra(TaskViewActivity.EXTRA_TASK_DESC);
                String taskDate = resultsIntent.getStringExtra(TaskViewActivity.EXTRA_TASK_DUEDATE);
                int taskId = resultsIntent.getIntExtra(TaskViewActivity.EXTRA_TASK_ID, 0);
                int taskPriority = resultsIntent.getIntExtra(TaskViewActivity.EXTRA_TASK_PRIORITY, 0);
                Boolean taskIsComplete = resultsIntent.getBooleanExtra(TaskViewActivity.EXTRA_TASK_COMPLETE, false);


                task = new Task(taskTitle, taskDesc, taskDate, taskPriority, taskIsComplete);
                task.id = taskId;
                titleTextView.setText("Edit task");
                addButton.setText("Save task");
                titleEditText.setText(taskTitle);
                descriptionEditText.setText(taskDesc);
                prioritySpinner.setSelection(taskPriority);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int year = task.dueDate.getDate().getYear();
                    int month = task.dueDate.getDate().getMonthValue() - 1; // Months in DatePicker are 0-indexed
                    int day = task.dueDate.getDate().getDayOfMonth();

                    System.out.println(task.dueDate.toPrettyString());
                    System.out.println(task.dueDate.toString());

                    datePicker.updateDate(year, month, day);
                }

            }
        }


        addButton.setOnClickListener((View view) -> {
            Boolean isNewTask = false;
            if(task == null){
                task = new Task("", "", "01/01/1970", 0, false);
                isNewTask = true;
            }

            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            if(title.isEmpty()){
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if(description.isEmpty()){
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (task != null) {
                task.setTitle(title);
                task.setDescription(description);
                task.dueDate.setDate(datePicker);
                task.priority = prioritySpinner.getSelectedItemPosition();
            }

            if(!isNewTask){
                dbHelper.updateData(task);
            }else{
                dbHelper.addData(task);
            }

            finish();

        });
    }
}