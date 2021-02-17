package com.example.cbr_manager.Forms;

import android.app.DatePickerDialog;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cbr_manager.UI.NewClientActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayFormPage {

    public static void displayPage(FormPage page, LinearLayout form, android.content.Context context){
        ArrayList<Question> questions = page.getQuestions();
        for(Question question : questions){
            TextQuestion txtQ;
            MultipleChoiceQuestion mcQ;
            if(question.getQuestionType().equals(QuestionType.PLAIN_TEXT)){
                txtQ = (TextQuestion) question;
                displayPlainTextQuestion(txtQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.PHONE_NUMBER)){
                txtQ = (TextQuestion) question;
                displayPhoneNumberQuestion(txtQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.NUMBER)){
                txtQ = (TextQuestion) question;
                displayNumberQuestion(txtQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.DATE)){
                txtQ = (TextQuestion) question;
                displayDateQuestion(txtQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.CHECK_BOX)){
                mcQ = (MultipleChoiceQuestion) question;
                displayCheckBoxQuestion(mcQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.DROP_DOWN)){
                mcQ = (MultipleChoiceQuestion) question;
                displayDropDownQuestion(mcQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.RADIO)){
                mcQ = (MultipleChoiceQuestion) question;
                displayRadioQuestion(mcQ, form, context);
            }
        }
    }


    private static void displayQuestionHeading(String questionString, LinearLayout form, android.content.Context context){
        TextView questionText = new TextView(context);
        questionText.setText(questionString);
        form.addView(questionText);
    }

    private static void displayPlainTextQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        EditText inputText = new EditText(context);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        form.addView(inputText);
    }

    private static void displayPhoneNumberQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);


        EditText inputText = new EditText(context);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_PHONE);
        form.addView(inputText);
    }

    private static void displayNumberQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        EditText inputText = new EditText(context);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        form.addView(inputText);
    }

    private static void displayDateQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        TextView selectDate = new TextView(context);
        selectDate.setText("Select Date");

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                selectDate.setText(date);
            }
        };

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);
                dialog.show();
            }
        });
        form.addView(selectDate);
    }

    private static void displayRadioQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        String[] answers = question.getAnswers();
        RadioGroup radioAnswers = new RadioGroup(context);
        RadioButton rButton;
        for (String answer : answers) {
            rButton = new RadioButton(context);
            rButton.setText(answer);
            radioAnswers.addView(rButton);
        }

        form.addView(radioAnswers);
    }

    private static void displayDropDownQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        Spinner spinner = new Spinner(context);
        String[] answers = question.getAnswers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, answers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        form.addView(spinner);
    }

    private static void displayCheckBoxQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        CheckBox checkBox;
        String[] answers = question.getAnswers();
        for (String answer : answers) {
            checkBox = new CheckBox(context);
            checkBox.setText(answer);
            form.addView(checkBox);
        }
    }
}
