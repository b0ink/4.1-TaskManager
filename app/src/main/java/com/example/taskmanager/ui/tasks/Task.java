package com.example.taskmanager.ui.tasks;

public class Task{

    public static final int HIGH_PRIORITY = 3;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 1;

    private String title;
    private String description;
    private String dueDate;

    private int priorityIcon;

    public int priority;

//    public enum priority{
//        HIGH_PRIORITY,
//        MEDIUM_PRIORITY,
//        LOW_PRIORITY
//    }

    public Task(String title, String description, String dueDate, int priorityIcon, int priority){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityIcon = priorityIcon;
        this.priority = priority;
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
        return this.dueDate;

    }

    public int getPriorityIcon() {
        return this.priorityIcon;
    }
}
