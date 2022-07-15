package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Checklist extends AppCompatActivity {
    private TextView questionTV, questionNumberTV;
    private Button option1btn,option2Btn,option3Btn,option4Btn,option5Btn;
    private ArrayList<ChecklistModel> checklistModelArrayList;
    Random random;
    int currentScore = 0, questionAttempted = 1, currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_checklist );
        questionTV = findViewById ( R.id.idTVQuestion );
        questionNumberTV = findViewById ( R.id.idTVQuestionAttemped );
        option1btn = findViewById ( R.id.idbtnoption1 );
        option2Btn = findViewById ( R.id.idbtnoption2 );
        option3Btn = findViewById ( R.id.idbtnoption3 );
        option4Btn = findViewById ( R.id.idbtnoption4 );
        option5Btn = findViewById ( R.id.idbtnoption5 );
        checklistModelArrayList = new ArrayList<> ();
        random = new Random ();
        getCheckQuestion (checklistModelArrayList);
        currentPos = random.nextInt(checklistModelArrayList.size ());
        setDatatoviews(currentPos);

        option1btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(checklistModelArrayList.get(currentPos).getAnswer ().trim().toLowerCase ().equals ( option1btn.getText ().toString ().trim ().toLowerCase () )){
                    currentScore++;
                }
                    questionAttempted++;
                    currentPos = random.nextInt (checklistModelArrayList.size ());
                    setDatatoviews ( currentPos );
            }
        } );
        option2Btn.setOnClickListener ( new View.OnClickListener () {
          @Override
           public void onClick(View view) {
              if(checklistModelArrayList.get(currentPos).getAnswer ().trim().toLowerCase ().equals ( option2Btn.getText ().toString ().trim ().toLowerCase () )){
                  currentScore++;
              }
              questionAttempted++;
              currentPos = random.nextInt (checklistModelArrayList.size ());
              setDatatoviews ( currentPos );

             }
        } );

        option3Btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(checklistModelArrayList.get(currentPos).getAnswer ().trim().toLowerCase ().equals ( option3Btn.getText ().toString ().trim ().toLowerCase () )){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt (checklistModelArrayList.size ());
                setDatatoviews ( currentPos );

            }
        } );

        option4Btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(checklistModelArrayList.get(currentPos).getAnswer ().trim().toLowerCase ().equals ( option4Btn.getText ().toString ().trim ().toLowerCase () )){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt (checklistModelArrayList.size ());
                setDatatoviews ( currentPos );


            }
        } );

        option5Btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(checklistModelArrayList.get(currentPos).getAnswer ().trim().toLowerCase ().equals ( option5Btn.getText ().toString ().trim ().toLowerCase () )){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt (checklistModelArrayList.size ());
                setDatatoviews ( currentPos );

            }
        } );

    }

    private void setDatatoviews(int currentPos){
        questionNumberTV.setText("Question Attempted : "+questionAttempted + "/10");

        questionTV.setText ( checklistModelArrayList.get ( currentPos ).getQuestion () );
        option1btn.setText ( checklistModelArrayList.get ( currentPos ).getOption1 () );
        option2Btn.setText ( checklistModelArrayList.get ( currentPos ).getOption2 () );
        option3Btn.setText ( checklistModelArrayList.get ( currentPos ).getOption3 () );
        option4Btn.setText ( checklistModelArrayList.get ( currentPos ).getOption4 () );
        option5Btn.setText ( checklistModelArrayList.get ( currentPos ).getOption5 () );
    }

    private void getCheckQuestion(ArrayList<ChecklistModel>checklistModelArrayList) {
        checklistModelArrayList.add ( new ChecklistModel ( "External Walls and Wall Finishes", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "External Doors", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "External Windows ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "External Floors and Finishes ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "External Ceiling and Ceiling Finishes", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Roofs ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Internal Walls and Finishes ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Internal Doors ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Internal Floors and Finishes ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Internal Ceiling and Ceiling Finishes ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Handwash Basin ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Carpets ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Tiles: Floors ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Tiles: Wall ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Toilets ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Gutters ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Down Pipes  ", "C1", "C1", "C3", "C4", "C5", "" ) );
        checklistModelArrayList.add ( new ChecklistModel ( "Paint: Exterior  ", "C1", "C1", "C3", "C4", "C5", "" ) );


    }
}