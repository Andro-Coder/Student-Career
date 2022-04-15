package com.example.studentcareerapp.Domains;

import java.io.Serializable;

public class FacultyFeedbackDomain implements Serializable {

    String email,semester,Feedback;

    public FacultyFeedbackDomain() {
    }

    public FacultyFeedbackDomain(String email, String semester, String feedback) {
        this.email = email;
        this.semester = semester;
        Feedback = feedback;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }
}
