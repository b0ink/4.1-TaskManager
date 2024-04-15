package com.example.taskmanager.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taskmanager.DatabaseHelper;
import com.example.taskmanager.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.databinding.FragmentHomeBinding;
import com.example.taskmanager.ui.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ProgressBar pbCompletedTasks;
    private ProgressBar pbOverdueTasks;

    private TextView tvCompletedTasks;
    private TextView tvOverdueTasks;

    private DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        pbCompletedTasks = root.findViewById(R.id.completed_tasks_progress);
        pbOverdueTasks = root.findViewById(R.id.overdue_tasks_progress);

        tvCompletedTasks = root.findViewById(R.id.completed_pct);
        tvOverdueTasks = root.findViewById(R.id.overdue_pct);
        dbHelper = new DatabaseHelper(root.getContext());

        ArrayList<Task> tasks = dbHelper.getTasks();
        int totalTasks = tasks.size();
        int completedTasks = 0;
        int overdueTasks = 0;

        for(Task task : tasks){
            if(task.isComplete){
                completedTasks++;
            } else if(task.isOverdue()){
                overdueTasks++;
            }
        }

//        System.out.println("total, complete, overdue");
//        System.out.println(totalTasks);
//        System.out.println(completedTasks);
//        System.out.println(overdueTasks);

        int completedPct = (int)((double)completedTasks/(double)totalTasks*100.0);
        int overduePct = (int)((double)overdueTasks/(double)totalTasks*100.0);

        setProgressBars(tvCompletedTasks, pbCompletedTasks, completedPct);
        setProgressBars(tvOverdueTasks, pbOverdueTasks, overduePct);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private final Handler handler = new Handler();

    public void setProgressBars(TextView text, ProgressBar pb, int pct) {

        int actualResults = pct;

        pb.setProgress(0);
        pb.setMax(100);

        text.setText("0%");

        final int[] pStatus = {0};

        //        https://stackoverflow.com/questions/21333866/how-to-create-a-circular-progressbar-in-android-which-rotates-on-it
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double msDelay = 1500.0 / (double) actualResults;

            while (pStatus[0] < actualResults) {
                pStatus[0] += 1;
                handler.post(() -> {
                    pb.setProgress(pStatus[0]);
                    text.setText(pStatus[0] + "%");
                });
                try {
                    Thread.sleep((int) msDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}