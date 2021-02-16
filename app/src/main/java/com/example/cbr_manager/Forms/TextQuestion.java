package com.example.cbr_manager.Forms;

public class TextQuestion extends Question {

    public TextQuestion(String strQuestion, QuestionType questionType) {
        super(strQuestion, questionType);
    }

    public QuestionType getQuestionType() {
        return super.getQuestionType();
    }

    public String getQuestionString(){
        return super.getStrQuestion();
    }

}
