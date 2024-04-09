package com.example.taskmanager.ui.tasks;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.DatabaseHelper;
import com.example.taskmanager.R;
import com.example.taskmanager.databinding.FragmentTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private RecyclerView recyclerViewTasks;

    private Button btnAddTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TaskViewModel galleryViewModel =
                new ViewModelProvider(this).get(TaskViewModel.class);

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


//        recyclerViewTasks = findViewById(R.id.recycler_view_tasks);
        recyclerViewTasks = root.findViewById(R.id.recycler_view_tasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: on change of fragment (switching between dashboard/tasks/etc), data set is lost
        // -> dataset must be dynamically retrieved everytime here
        // -> don't bother with a local storage/cache if using sqlite
        // -> look into SyncAdapters?

        List<Task> tasks = new ArrayList<Task>();

        DatabaseHelper dbHelper = new DatabaseHelper(root.getContext());
        Cursor cursor = dbHelper.getData();

        // Check if the cursor is not null and move it to the first row
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("Title"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("Description"));
                @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex("DueDate"));
                @SuppressLint("Range") int priority = cursor.getInt(cursor.getColumnIndex("Priority"));

                Task newtask = new Task(title, description, dueDate,0,  priority);
                newtask.id = id;
                // Do something with the retrieved data (e.g., display it, process it)
//                Log.d("Data", "ID: " + id + ", Name: " + name);
                tasks.add((newtask));
            } while (cursor.moveToNext()); // Move to the next row if available
        }

        // Close the cursor after use to release resources
        if (cursor != null) {
            cursor.close();
        }

//        tasks.add(new Task("do homework", "finish this", "21/05/2025", 0, 1));
//        tasks.add(new Task("clean house", "finish asdf", "05/11/2025", 0, 2));
//        tasks.add(new Task("do nothing", "finish 21123123", "05/09/2024", 0, 3));

        TaskAdapter adapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(adapter);

        btnAddTask = root.findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener((View view) -> {
            int position = tasks.size();
            tasks.add(new Task("NEW TASK!", "finish 21123123", "01/07/2049", 0, 2));
            adapter.notifyItemInserted(position);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}