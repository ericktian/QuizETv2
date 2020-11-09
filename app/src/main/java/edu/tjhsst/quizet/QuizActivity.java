package edu.tjhsst.quizet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

//score counter, hint, enter name?, retry?

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mHintButton;
    private TextView mQuestionTextView;
    private TextView mHighScore;
    private int mCurrentIndex = 0;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_text1,false),
            new Question(R.string.question_text2,true),
            new Question(R.string.question_text3, true),
            new Question(R.string.question_text4, true),
            new Question(R.string.question_text5, false),
            new Question(R.string.question_text6, false),
            new Question(R.string.question_text7, false),
            new Question(R.string.question_text8, true),
            new Question(R.string.question_text9, true)

    };
    //MY NEW INTS
    private double numCorrect = 0.0;
    private boolean answeredAlready = false;
    private boolean alreadylost = false;
    private int highScore;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private boolean isFirst = true;



    private void updateQuestion(){
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void checkAnswer(boolean userPressed){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(userPressed == answerIsTrue) {
            messageResId = R.string.correct_toast;
            if(!answeredAlready){
                numCorrect++;
                answeredAlready = true;}
        }
        else
            messageResId = R.string.incorrect_toast;
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mHighScore = (TextView)findViewById(R.id.high_score);


        mQuestionTextView = (TextView)findViewById(R.id.question_text);
        updateQuestion();

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex<mQuestionBank.length){
                checkAnswer(true);}
                mCurrentIndex++;
                answeredAlready = false;
                alreadylost = false;

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("message");
                if(isFirst){
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int value = dataSnapshot.getValue(int.class);
                            highScore = value;
                            isFirst = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
                //
                if(!isFirst && numCorrect/mQuestionBank.length * 100 > highScore){
                    highScore = (int)(numCorrect/mQuestionBank.length * 100);
                    myRef.setValue(highScore);}
                //
                if(mCurrentIndex<mQuestionBank.length)
                    updateQuestion();
                else{
                    String ans = "Congrats! You scored " + (int)(numCorrect/mQuestionBank.length * 100.0) + "%";
                    if((int)(numCorrect/mQuestionBank.length * 100) == highScore) ans += ", the high score!";
                    else ans += "! The high score was " + highScore + "%";
                    mQuestionTextView.setText(ans);
                }
            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex<mQuestionBank.length){
                checkAnswer(false);}
                mCurrentIndex++;
                answeredAlready = false;
                alreadylost = false;

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("message");
                if(isFirst){
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int value = dataSnapshot.getValue(int.class);
                            highScore = value;
                            isFirst = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
                //
                if(!isFirst && numCorrect/mQuestionBank.length * 100 > highScore){
                    highScore = (int)(numCorrect/mQuestionBank.length * 100);
                    myRef.setValue(highScore);}
                //
                if(mCurrentIndex<mQuestionBank.length)
                    updateQuestion();
                else{
                    String ans = "Congrats! You scored " + (int)(numCorrect/mQuestionBank.length * 100.0) + "%";
                    if((int)(numCorrect/mQuestionBank.length * 100) == highScore) ans += ", the high score!";
                    else ans += "! The high score was " + highScore + "%";
                    mQuestionTextView.setText(ans);
                }
            }
        });


        mHintButton = (Button)findViewById(R.id.hint_button);
        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex<mQuestionBank.length){
                    if(!alreadylost){
                        numCorrect -= .5;
                        alreadylost = true;
                    }
                    Intent i = new Intent(QuizActivity.this, HintActivity.class);
                    boolean answer = mQuestionBank[mCurrentIndex].isAnswerTrue();
                    i.putExtra("label", answer);
                    startActivity(i);}
            }
        });
        updateQuestion();
    }
}
