package com.example.cbr_manager.Forms;

public class TextQuestion extends Question {
    final private TextType textType;

    public TextQuestion(String strQuestion, TextType textType) {
        super(strQuestion);
        this.textType = textType;
    }

    public TextType getTextType() {
        return textType;
    }

    public String getQuestionString(){
        return super.getStrQuestion();
    }

}
