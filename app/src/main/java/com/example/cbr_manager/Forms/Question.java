package com.example.cbr_manager.Forms;

public abstract class Question {
    final private String strQuestion;

    public Question(String strQuestion) {
        this.strQuestion = strQuestion;
    }

    public String getStrQuestion() {
        return strQuestion;
    }
}
