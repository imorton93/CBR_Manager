package com.example.cbr_manager.Forms;

public abstract class Question {
    final private String questionTag;
    final private String strQuestion;
    final private QuestionType questionType;
    final private Boolean required;


    public Question(String questionTag, String strQuestion, QuestionType questionType, Boolean required) {
        this.questionTag = questionTag;
        this.strQuestion = strQuestion;
        this.questionType = questionType;
        this.required = required;
    }

    public String getStrQuestion() {
        return strQuestion;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public String getQuestionTag() {
        return questionTag;
    }

    public Boolean getRequired() {
        return required;
    }
}
