package com.example.lab_5a_mobile;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import yuku.ambilwarna.AmbilWarnaDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridLayout studentBoard;
    private TextView emptyBoardText;
    private final List<Student> students = new ArrayList<>();
    private Button selectedStudentButton;
    private int defaultButtonColor = Color.rgb(98, 0, 238);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        studentBoard = findViewById(R.id.student_board);
        emptyBoardText = findViewById(R.id.empty_board_text);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_new) {
            showNewStudentDialog();
            return true;
        }
        if (itemId == R.id.action_select_color) {
            showColorPickerDialog();
            return true;
        }
        if (itemId == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNewStudentDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_new_student, null);
        EditText idInput = dialogView.findViewById(R.id.edit_student_id);
        EditText nameInput = dialogView.findViewById(R.id.edit_student_name);
        EditText ageInput = dialogView.findViewById(R.id.edit_student_age);
        Button addButton = dialogView.findViewById(R.id.button_add_student);
        Button clearButton = dialogView.findViewById(R.id.button_clear_student);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.new_student)
                .setView(dialogView)
                .setNegativeButton(R.string.cancel, null)
                .create();

        clearButton.setOnClickListener(view -> {
            idInput.setText("");
            nameInput.setText("");
            ageInput.setText("");
            idInput.requestFocus();
        });

        addButton.setOnClickListener(view -> {
            String id = idInput.getText().toString().trim();
            String name = nameInput.getText().toString().trim();
            String ageText = ageInput.getText().toString().trim();

            if (TextUtils.isEmpty(id)) {
                idInput.setError(getString(R.string.id_required));
                idInput.requestFocus();
                return;
            }
            if (findStudentById(id) != null) {
                idInput.setError(getString(R.string.id_exists));
                idInput.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(name)) {
                nameInput.setError(getString(R.string.name_required));
                nameInput.requestFocus();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException exception) {
                ageInput.setError(getString(R.string.age_required));
                ageInput.requestFocus();
                return;
            }

            if (age < 1 || age > 150) {
                ageInput.setError(getString(R.string.age_range));
                ageInput.requestFocus();
                return;
            }

            Student student = new Student(id, name, age);
            students.add(student);
            addStudentButton(student);
            dialog.dismiss();
        });

        dialog.setOnShowListener(listener -> idInput.requestFocus());
        dialog.show();
    }

    private Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    private void addStudentButton(Student student) {
        Button studentButton = new Button(this);
        studentButton.setText(student.getId());
        studentButton.setAllCaps(false);
        studentButton.setFocusable(true);
        studentButton.setMinWidth(0);
        studentButton.setMinimumWidth(0);
        studentButton.setSingleLine(true);
        studentButton.setTextSize(12);
        studentButton.setBackgroundColor(defaultButtonColor);
        studentButton.setTag(defaultButtonColor);
        updateButtonTextColor(studentButton, defaultButtonColor);
        studentButton.setAlpha(0.85f);

        int margin = getResources().getDimensionPixelSize(R.dimen.student_button_margin);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        int position = studentBoard.getChildCount();
        int row = position / 4;
        int column = position % 4;
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(column, 1, 1f);
        params.rowSpec = GridLayout.spec(row);
        params.setMargins(margin, margin, margin, margin);
        studentButton.setLayoutParams(params);

        studentButton.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                if (selectedStudentButton != null && selectedStudentButton != view) {
                    selectedStudentButton.setAlpha(0.85f);
                }
                selectedStudentButton = (Button) view;
                selectedStudentButton.setAlpha(1.0f);
            }
        });

        studentButton.setOnClickListener(view -> {
            selectedStudentButton = (Button) view;
            selectedStudentButton.requestFocusFromTouch();
            startActivity(StudentDetailActivity.createIntent(this, student));
        });

        studentBoard.addView(studentButton);
        emptyBoardText.setVisibility(View.GONE);
    }

    private void showColorPickerDialog() {
        Button focusedButton = getFocusedStudentButton();
        int initialColor = defaultButtonColor;
        if (focusedButton != null && focusedButton.getTag() instanceof Integer) {
            initialColor = (Integer) focusedButton.getTag();
        }
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(
                this,
                initialColor,
                false,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        if (focusedButton != null) {
                            focusedButton.setBackgroundColor(color);
                            focusedButton.setTag(color);
                            updateButtonTextColor(focusedButton, color);
                        } else {
                            defaultButtonColor = color;
                            Toast.makeText(MainActivity.this,
                                    R.string.default_color_selected,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        Toast.makeText(MainActivity.this,
                                R.string.color_picker_closed,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        colorPickerDialog.show();
    }

    private Button getFocusedStudentButton() {
        View currentFocus = getCurrentFocus();
        if (currentFocus instanceof Button && currentFocus.getParent() == studentBoard) {
            return (Button) currentFocus;
        }
        if (selectedStudentButton != null && selectedStudentButton.getParent() == studentBoard) {
            return selectedStudentButton;
        }
        return null;
    }

    private void updateButtonTextColor(Button button, int backgroundColor) {
        double brightness = 0.299 * Color.red(backgroundColor)
                + 0.587 * Color.green(backgroundColor)
                + 0.114 * Color.blue(backgroundColor);
        button.setTextColor(brightness > 160 ? Color.BLACK : Color.WHITE);
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(getString(R.string.about_message))
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
