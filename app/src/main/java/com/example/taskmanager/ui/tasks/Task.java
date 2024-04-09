package com.example.taskmanager.ui.tasks;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    public static final int HIGH_PRIORITY = 3;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 1;
    public static final int NO_PRIORITY = 0;

    private String title;
    private String description;
//    private String dueDate;

    private int priorityIcon;

    public LocalDate dueDate;
    public String dueDateString;
//    = LocalDate.now(); // Create a date object

    public int priority;

//    public enum priority{
//        HIGH_PRIORITY,
//        MEDIUM_PRIORITY,
//        LOW_PRIORITY
//    }

    public Task(String title, String description, LocalDate dueDate, int priorityIcon, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityIcon = priorityIcon;
        this.priority = priority;
    }

    @SuppressLint("NewApi")
    public Task(String title, String description, String dueDate, int priorityIcon, int priority) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dueDate, formatter);
        this.title = title;
        this.description = description;
        this.dueDate = date;
        this.priorityIcon = priorityIcon;
        this.priority = priority;
        this.dueDateString = dueDate;
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
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            String dateString = dueDate.format(formatter);
            String monthUpper = dueDate.getMonth().toString().toLowerCase();
            String month = monthUpper.substring(0, 1).toUpperCase() + monthUpper.substring(1);

            String dateString = dueDate.getDayOfMonth() + getDayOfMonthSuffix(dueDate.getDayOfMonth()) + " " +
                    month + " " +
                    dueDate.getYear();

            return dateString;

        }
        return "";
    }


    public int getPriorityIcon() {
        return this.priorityIcon;
    }

    private static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
