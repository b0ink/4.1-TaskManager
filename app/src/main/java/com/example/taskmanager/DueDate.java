package com.example.taskmanager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DueDate {

    private LocalDate date;

    @SuppressLint("NewApi")
    public DueDate(String dueDate) {
        setDate(dueDate);
    }

    public DueDate(LocalDate dueDate) {
        this.date = dueDate;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(DatePicker datePicker) {
        String dueDateString = "";
        if (datePicker.getDayOfMonth() < 10) {
            dueDateString += "0";
        }
        dueDateString += datePicker.getDayOfMonth() + "/";
        if ((datePicker.getMonth() + 1) < 10) {
            dueDateString += "0";
        }
        dueDateString += (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
        setDate(dueDateString);
    }

    @SuppressLint("NewApi")
    public void setDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.date = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            System.out.println("Error parsing date as string");
            System.out.println(e);
            System.out.println(date);
            this.date = LocalDate.now();
        }
    }




    @NonNull
    public String toPrettyString() {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String monthUpper = date.getMonth().toString().toLowerCase();
            String month = monthUpper.substring(0, 1).toUpperCase() + monthUpper.substring(1);
            String dateString = date.getDayOfMonth() + getDayOfMonthSuffix(date.getDayOfMonth()) + " " +
                    month + " " +
                    date.getYear();

            return dateString;
        }
        return "N/A";
    }

    @NonNull
    public String toString() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return this.date.format(formatter);
            } catch (Exception e) {
                System.out.println("Error return dueDate.toString()");
            }
        }
        return "N/A";
    }


    /*
        Number of days until due date (from today)
        negative values mean amount of days past due date
        positive values mean days until due date
     */
    @SuppressLint("NewApi")
    public int daysUntilDue() {
        LocalDate today = LocalDate.now();;
        int days = (int) ChronoUnit.DAYS.between(this.date, today);
        if (this.date.isBefore(today)) {
            return Math.abs(days) * -1;
        } else {
            return Math.abs(days);
        }
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
