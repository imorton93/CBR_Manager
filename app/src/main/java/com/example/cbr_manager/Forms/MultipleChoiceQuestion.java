package com.example.cbr_manager.Forms;


public class MultipleChoiceQuestion extends Question {
    private final String[] answers;

    public MultipleChoiceQuestion(int questionId, String strQuestion, QuestionType questionType, String[] answers) {
        super(questionId,strQuestion, questionType);
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

    public int getQuestionId() { return super.getQuestionId();}
}
