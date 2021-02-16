package com.example.cbr_manager.Forms;


public class MultipleChoiceQuestion extends Question {
    private final String[] answers;

    public MultipleChoiceQuestion(String strQuestion, QuestionType questionType, String[] answers) {
        super(strQuestion, questionType);
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
}
