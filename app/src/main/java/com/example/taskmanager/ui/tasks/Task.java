package com.example.taskmanager.ui.tasks;

import android.annotation.SuppressLint;

import com.example.taskmanager.DueDate;

import java.time.LocalDate;

public class Task {

    public static final int HIGH_PRIORITY = 3;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 1;
    public static final int NO_PRIORITY = 0;

    public int id;

    private String title;
    private String description;

    public DueDate dueDate;

    public Boolean isComplete;

    public int priority;


    public Task(String title, String description, LocalDate dueDate, int priority, Boolean isComplete) {
        this.title = title;
        this.description = description;
        this.dueDate = new DueDate(dueDate);
        this.priority = priority;
        this.isComplete = isComplete;
    }

    @SuppressLint("NewApi")
    public Task(String title, String description, String dueDate, int priority, Boolean isComplete) {
        this.title = title;
        this.description = description;
        this.dueDate = new DueDate(dueDate);
        this.priority = priority;
        this.isComplete = isComplete;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDueDate() {
        return this.dueDate.toString();
    }

    public boolean isOverdue() {
        System.out.println("Days until due:");
        System.out.println(dueDate.daysUntilDue());
        return dueDate.daysUntilDue() < 0;
    }
}
