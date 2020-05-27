package com.example.mangaxdroid.activity.useractivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.mangaxdroid.R;


public class UserFaqActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView question1, question2, question3, question4, question5, question6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_faq);

        toolbar = (Toolbar) findViewById(R.id.toolBarFaq);
        question1 = (TextView) findViewById(R.id.question1);
        question2 = (TextView) findViewById(R.id.question2);
        question3 = (TextView) findViewById(R.id.question3);
        question4 = (TextView) findViewById(R.id.question4);
        question5 = (TextView) findViewById(R.id.question5);
        question6 = (TextView) findViewById(R.id.question6);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiceDialogBox(question1, R.string.question1);
            }
        });
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiceDialogBox(question2, R.string.question2);
            }
        });
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiceDialogBox(question3, R.string.question3);
            }
        });
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiceDialogBox(question4, R.string.question4);
            }
        });
        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiceDialogBox(question5, R.string.question5);
            }
        });
        question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiceDialogBox(question6, R.string.question6);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNiceDialogBox(TextView id, int msg) {
        if (id == question3) {
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
            myBuilder.setIcon(R.drawable.mangaxdroid)
                    .setTitle(id.getText())
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .setNegativeButton("Xem thêm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichOne) {
                            showNiceDialogBox(question4, R.string.question4);
                        }})
                    .show();
        }
        else {
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
            myBuilder.setIcon(R.drawable.mangaxdroid)
                    .setTitle(id.getText())
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
