package com.example.cbr_manager.Forms;

import java.util.ArrayList;

public class FormPage {
    private ArrayList<Question> questions = new ArrayList<>();

    public FormPage() {
    }

    public void addToPage(Question question){
        questions.add(question);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
