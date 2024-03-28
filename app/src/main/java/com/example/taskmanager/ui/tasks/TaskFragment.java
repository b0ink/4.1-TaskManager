package com.example.taskmanager.ui.tasks;

import android.os.Bundle;
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
        tasks.add(new Task("do homework", "finish this", "today!", 0));
        tasks.add(new Task("clean house", "finish asdf", "yesterday!", 0));
        tasks.add(new Task("do nothing", "finish 21123123", "tomorrow!", 0));

        TaskAdapter adapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(adapter);

        btnAddTask = root.findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener((View view)->{
            tasks.add(new Task("NEW TASK!", "finish 21123123", "tomorrow!", 0));
            adapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}