package com.oatmealprogs.delima.Shared;

public class HomeworkTask {
    private String taskID;
    private String description;
    private String datetime;

    public HomeworkTask(String taskID, String description, String datetime) {
        this.taskID = taskID;
        this.description = description;
        this.datetime = datetime;
    }

    public HomeworkTask() {
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
