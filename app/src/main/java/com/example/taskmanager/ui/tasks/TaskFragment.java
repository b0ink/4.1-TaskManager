package com.example.taskmanager.ui.tasks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.DatabaseHelper;
import com.example.taskmanager.R;
import com.example.taskmanager.databinding.FragmentTaskBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private RecyclerView recyclerViewTasks;

    private Button btnAddTask;
    private DatabaseHelper dbHelper;

    private Spinner spnSortBy;
    private ArrayAdapter<String> sortByOptions;

    List<Task> tasks = new ArrayList<Task>();
    TaskAdapter adapter;

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
        spnSortBy = root.findViewById(R.id.spinner_sortby);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: on change of fragment (switching between dashboard/tasks/etc), data set is lost
        // -> dataset must be dynamically retrieved everytime here
        // -> don't bother with a local storage/cache if using sqlite
        // -> look into SyncAdapters?

//        List<Task> tasks = new ArrayList<Task>();
        dbHelper = new DatabaseHelper(root.getContext());
        adapter = new TaskAdapter(root.getContext(), tasks);

        getData();

        sortByOptions = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item);
        sortByOptions.add("Sort by: Due date ▲");
        sortByOptions.add("Sort by: Due date ▼");
        sortByOptions.add("Sort by: Priority ▲");
        sortByOptions.add("Sort by: Priority ▼");

        spnSortBy.setAdapter(sortByOptions);
        AdapterView.OnItemSelectedListener spinnerSortByChanged = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    SortTasksByDueDate("newest");
                } else if (position == 1) {
                    SortTasksByDueDate("oldest");
                }else if (position == 2){
                    SortTasksByPriority("lowest");
                }else if (position == 3){
                    SortTasksByPriority("highest");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        };

        spnSortBy.setOnItemSelectedListener(spinnerSortByChanged);


//        tasks.add(new Task("do homework", "finish this", "21/05/2025", 0, 1));
//        tasks.add(new Task("clean house", "finish asdf", "05/11/2025", 0, 2));
//        tasks.add(new Task("do nothing", "finish 21123123", "05/09/2024", 0, 3));

        recyclerViewTasks.setAdapter(adapter);

        btnAddTask = root.findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener((View view) -> {
//            int position = tasks.size();
//            tasks.add(new Task("NEW TASK!", "finish 21123123", "01/07/2049", 0, 2));
//            adapter.notifyItemInserted(position);

            Intent intent = new Intent(root.getContext(), TaskViewActivity.class);
            intent.putExtra(TaskViewActivity.EXTRA_TASK_ACTION, "new");
            root.getContext().startActivity(intent);
        });
        return root;
    }


    public void SortTasksByDueDate(String type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            tasks.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    if (Objects.equals(type, "newest")) {
                        return o1.dueDate.getDate().compareTo(o2.dueDate.getDate());
                    } else if (Objects.equals(type, "oldest")) {
                        return o2.dueDate.getDate().compareTo(o1.dueDate.getDate());
                    }
                    return 0;
                }
            });
        }
        SortTasksByCompletion();
    }

    public void SortTasksByPriority(String type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            tasks.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    if (Objects.equals(type, "lowest")) {
                        return o1.priority - o2.priority;
                    } else if (Objects.equals(type, "highest")) {
                        return o2.priority - o1.priority;
                    }
                    return 0;
                }
            });
        }

        SortTasksByCompletion();
    }

    public void SortTasksByCompletion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            tasks.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    int o1Complete = o1.isComplete ? 1 : 0;
                    int o2Complete = o2.isComplete ? 1 : 0;

                    return o1Complete - o2Complete;
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        tasks.clear();
        tasks.addAll(dbHelper.getTasks());
        SortTasksByDueDate("newest");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();

    }
}