package com.example.cbr_manager.Forms;

public abstract class Question {
    final private int questionId;
    final private String strQuestion;
    final private QuestionType questionType;


    public Question(int questionId, String strQuestion, QuestionType questionType) {
        this.questionId = questionId;
        this.strQuestion = strQuestion;
        this.questionType = questionType;
    }

    public String getStrQuestion() {
        return strQuestion;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public int getQuestionId() {
        return questionId;
    }
}
