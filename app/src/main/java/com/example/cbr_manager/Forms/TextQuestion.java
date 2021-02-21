package com.example.cbr_manager.Forms;

public class TextQuestion extends Question {

    public TextQuestion(String questionTag, String strQuestion, QuestionType questionType, Boolean required) {
        super(questionTag, strQuestion, questionType, required);
    }

    public QuestionType getQuestionType() {
        return super.getQuestionType();
    }

    public String getQuestionString(){
        return super.getStrQuestion();
    }

    public String getQuestionTag() { return super.getQuestionTag();}

    public Boolean getRequired(){
        return super.getRequired();
    }

}
