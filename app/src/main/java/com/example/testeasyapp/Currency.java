package com.example.testeasyapp;

public class Currency {
    private int id;
    private String title;
    private String description;
    private double amount;
    private long started, finished;
    private String targeted_date;

    public Currency() {

    }

    public Currency(int id, String title, String description, double amount, long started, long finished, String targeted_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.started = started;
        this.finished = finished;
        this.targeted_date = targeted_date;
    }

    public Currency(String title, String description, double amount, long started, long finished, String targeted_date) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.started = started;
        this.finished = finished;
        this.targeted_date = targeted_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public String getTargeted_date() {
        return targeted_date;
    }

    public void setTargeted_date(String targeted_date) {
        this.targeted_date = targeted_date;
    }


}
