package com.example.cbr_manager.Forms;

public abstract class Question {
    final private String strQuestion;
    final private QuestionType questionType;

    public Question(String strQuestion, QuestionType questionType) {
        this.strQuestion = strQuestion;
        this.questionType = questionType;
    }

    public String getStrQuestion() {
        return strQuestion;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }
}
