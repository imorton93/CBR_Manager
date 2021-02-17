package com.example.cbr_manager.Forms;

public class TextQuestion extends Question {

    public TextQuestion(int questionId, String strQuestion, QuestionType questionType) {
        super(questionId, strQuestion, questionType);
    }

    public QuestionType getQuestionType() {
        return super.getQuestionType();
    }

    public String getQuestionString(){
        return super.getStrQuestion();
    }

    public int getQuestionId() { return super.getQuestionId();}

}
