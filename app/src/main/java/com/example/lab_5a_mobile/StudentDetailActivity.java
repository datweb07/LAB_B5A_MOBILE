package com.example.lab_5a_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "student_id";
    private static final String EXTRA_NAME = "student_name";
    private static final String EXTRA_AGE = "student_age";

    public static Intent createIntent(Context context, Student student) {
        Intent intent = new Intent(context, StudentDetailActivity.class);
        intent.putExtra(EXTRA_ID, student.getId());
        intent.putExtra(EXTRA_NAME, student.getName());
        intent.putExtra(EXTRA_AGE, student.getAge());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        setSupportActionBar(findViewById(R.id.detail_toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.student_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView idText = findViewById(R.id.text_detail_id);
        TextView nameText = findViewById(R.id.text_detail_name);
        TextView ageText = findViewById(R.id.text_detail_age);

        idText.setText(getString(R.string.detail_id, getIntent().getStringExtra(EXTRA_ID)));
        nameText.setText(getString(R.string.detail_name, getIntent().getStringExtra(EXTRA_NAME)));
        ageText.setText(getString(R.string.detail_age, getIntent().getIntExtra(EXTRA_AGE, 0)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
