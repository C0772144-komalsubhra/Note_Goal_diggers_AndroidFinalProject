package com.example.note_goal_diggers_android.models;

public class Subject {

    private Integer subjectId;
    private String subjectName;

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }

    public Subject(Integer subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
