package com.example.cbr_manager.Forms;

import android.app.DatePickerDialog;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.NewClientActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayFormPage {

    private static float txtSize = 20;


    public static void displayPage(FormPage page, LinearLayout form, android.content.Context context, double latitude, double longitude){
        ArrayList<Question> questions = page.getQuestions();
        Space mSpace;
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
            else if(question.getQuestionType().equals(QuestionType.CHECK_BOX_WITH_COMMENT)){
                mcQ = (MultipleChoiceQuestion) question;
                displayCheckBoxCommentQuestion(mcQ, form, context);
            }
            else if(question.getQuestionType().equals(QuestionType.GPS)){
                txtQ = (TextQuestion) question;
                displayGPSQuestion(txtQ, form, context, latitude, longitude);
            }
            else if(question.getQuestionType().equals(QuestionType.NONE)){
                txtQ = (TextQuestion) question;
                displayNoneTypeQuestion(txtQ.getQuestionString(), form, context);
            }

            //space between questions
            mSpace = new Space(context);
            mSpace.setMinimumHeight(50);
            form.addView(mSpace);
        }
    }

    private static void displayNoneTypeQuestion(String questionString, LinearLayout form, android.content.Context context){
        TextView questionText = new TextView(context);
        questionText.setText(Html.fromHtml(questionString));
        form.addView(questionText);
    }


    private static void displayQuestionHeading(String questionString, LinearLayout form, android.content.Context context){
        TextView questionText = new TextView(context);
        questionText.setText(questionString);
        questionText.setTextSize(txtSize);
        form.addView(questionText);
    }

    private static void displayPlainTextQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        EditText inputText = new EditText(context);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        inputText.setTag(question.getQuestionTag());
        inputText.setTextSize(txtSize);
        form.addView(inputText);
    }

    private static void displayPhoneNumberQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);


        EditText inputText = new EditText(context);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_PHONE);
        inputText.setTag(question.getQuestionTag());
        inputText.setTextSize(txtSize);
        form.addView(inputText);
    }

    private static void displayNumberQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        EditText inputText = new EditText(context);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputText.setTag(question.getQuestionTag());
        inputText.setTextSize(txtSize);
        form.addView(inputText);
    }

    private static void displayDateQuestion(TextQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        TextView selectDate = new TextView(context);
        Calendar calendar = Calendar.getInstance();
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH)+1;
        int dayOfMonth1 = calendar.get(Calendar.DAY_OF_MONTH);
        String date = dayOfMonth1 + "/" + month1 + "/" + year1;
        selectDate.setText(date);
        selectDate.setTextSize(txtSize);
        selectDate.setTag(question.getQuestionTag());

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date = dayOfMonth + "/" + month + "/" + year;
                selectDate.setText(date);
            }
        };

        selectDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);
            dialog.show();
        });
        form.addView(selectDate);
    }

    private static void displayGPSQuestion(TextQuestion question, LinearLayout form, android.content.Context context, double latitude, double longitude){
        displayQuestionHeading(question.getQuestionString(), form, context);

        TextView location = new TextView(context);
        location.setText("Latitude: " + String.valueOf(latitude) + " Longitude: " + String.valueOf(longitude));
        location.setTextSize(txtSize);
        location.setTag(question.getQuestionTag());

        form.addView(location);
    }

    private static void displayRadioQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        String[] answers = question.getAnswers();
        RadioGroup radioAnswers = new RadioGroup(context);
        radioAnswers.setTag(question.getQuestionTag());
        RadioButton rButton;
        for (String answer : answers) {
            rButton = new RadioButton(context);
            rButton.setText(answer);
            rButton.setTextSize(txtSize);
            radioAnswers.addView(rButton);
        }

        form.addView(radioAnswers);
    }

    private static void displayDropDownQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        Spinner spinner = new Spinner(context);
        String[] answers = question.getAnswers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, answers);
        spinner.setAdapter(adapter);
        spinner.setTag(question.getQuestionTag());

        form.addView(spinner);
    }

    private static void displayCheckBoxQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        CheckBox checkBox;
        String[] answers = question.getAnswers();
        for(int i = 0; i < answers.length; i++){
            checkBox = new CheckBox(context);
            checkBox.setText(answers[i]);
            checkBox.setTag(i);
            checkBox.setTextSize(txtSize);
            form.addView(checkBox);
            if(answers[i].equals("Other")){
                EditText input = new EditText(context);
                input.setTextSize(txtSize);
                input.setHint(R.string.explain_newVisitForm);
                input.setVisibility(View.GONE);
                input.setTag("otherExplanation");
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            input.setVisibility(View.VISIBLE);
                        }
                        else{
                            input.setVisibility(View.GONE);
                        }
                    }
                });
                form.addView(input);
            }

        }

    }

    private static void displayCheckBoxCommentQuestion(MultipleChoiceQuestion question, LinearLayout form, android.content.Context context){
        displayQuestionHeading(question.getQuestionString(), form, context);

        ScrollView sv = new ScrollView(context);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setTag(question.getQuestionTag());


        CheckBox checkBox;
        String[] answers = question.getAnswers();
        for(int i = 0; i < answers.length; i++){
            checkBox = new CheckBox(context);
            checkBox.setText(answers[i]);
            checkBox.setTag(i);
            checkBox.setTextSize(txtSize);
            EditText input = new EditText(context);
            input.setTextSize(txtSize);
            input.setHint(R.string.explain_newVisitForm);
            input.setVisibility(View.GONE);
            input.setTag(checkBox.getText().toString());

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        input.setVisibility(View.VISIBLE);
                    }
                    else{
                        input.setVisibility(View.GONE);
                    }
                }
            });

            layout.addView(checkBox);
            layout.addView(input);
        }

        sv.addView(layout);
        form.addView(sv);
    }
}
