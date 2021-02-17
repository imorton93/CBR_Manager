package com.example.cbr_manager.Forms;


public class MultipleChoiceQuestion extends Question {
    private final String[] answers;

    public MultipleChoiceQuestion(String questionTag, String strQuestion, QuestionType questionType, String[] answers, Boolean required) {
        super(questionTag,strQuestion, questionType, required);
        this.answers = answers;
    }

    public QuestionType getQuestionType() {
        return super.getQuestionType();
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getQuestionString(){
        return super.getStrQuestion();
    }

    public String getQuestionTag() { return super.getQuestionTag();}

    public Boolean getRequired() {
        return super.getRequired();
    }
}
