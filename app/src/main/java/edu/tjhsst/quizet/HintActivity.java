package edu.tjhsst.quizet;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {

    private boolean correct_Answer;
    private TextView mAnswerTrue;
    private Button mShowAnswer;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        correct_Answer = getIntent().getBooleanExtra("label",false);
        mAnswerTrue = (Button)findViewById(R.id.answer_button);
        mAnswerTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
            }
        });
        mShowAnswer = (Button)findViewById(R.id.CHEAT_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct_Answer == true) {
                    mAnswerTrue.setText(R.string.true_button);
                //    mAnswerTrue.setBackgroundColor(Color.parseColor("00cc00"));
                }
                else {
                    mAnswerTrue.setText(R.string.false_button);
                //    mAnswerTrue.setBackgroundColor(Color.parseColor("ff0000"));
                }
                if(exit)
                    finish();
                mShowAnswer.setText("Press to go back");
                exit = true;
            }
        });
    }
}
