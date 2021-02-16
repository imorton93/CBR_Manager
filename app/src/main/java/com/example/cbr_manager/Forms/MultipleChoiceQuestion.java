package com.example.cbr_manager.Forms;


public class MultipleChoiceQuestion extends Question {
    private final MCType questionType;
    private final String[] answers;

    public MultipleChoiceQuestion(String strQuestion, MCType questionType, String[] answers) {
        super(strQuestion);
        this.questionType = questionType;
        this.answers = answers;
    }

    public MCType getQuestionType() {
        return questionType;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getQuestionString(){
        return super.getStrQuestion();
    }
}
