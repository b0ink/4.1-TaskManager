package com.example.taskmanager.ui.tasks;

public class Task{

    private String title;
    private String description;
    private String dueDate;

    private int priorityIcon;

    public Task(String title, String description, String dueDate, int priorityIcon){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityIcon = priorityIcon;
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
