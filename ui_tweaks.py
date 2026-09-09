import re

# 1. Update activity_main.xml
path_xml = 'app/src/main/res/layout/activity_main.xml'
with open(path_xml, 'r', encoding='utf-8') as f:
    xml_content = f.read()

# Change background to #000000 and add theme for white dots
xml_content = xml_content.replace('android:background="?attr/colorPrimary"', 'android:background="#000000"\n        app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"')

with open(path_xml, 'w', encoding='utf-8') as f:
    f.write(xml_content)


# 2. Update dialog_new_student.xml
path_dialog = 'app/src/main/res/layout/dialog_new_student.xml'
dialog_content = '''<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:paddingStart="24dp"
    android:paddingTop="8dp"
    android:paddingEnd="24dp"
    android:paddingBottom="8dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="ID"
        android:textColor="#333333"
        android:textStyle="bold" />
    <EditText
        android:id="@+id/edit_student_id"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/student_id"
        android:imeOptions="actionNext"
        android:inputType="text"
        android:maxLines="1" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Name"
        android:textColor="#333333"
        android:textStyle="bold" />
    <EditText
        android:id="@+id/edit_student_name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/student_name"
        android:imeOptions="actionNext"
        android:inputType="textPersonName"
        android:maxLines="1" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Age"
        android:textColor="#333333"
        android:textStyle="bold" />
    <NumberPicker
        android:id="@+id/np_student_age"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:gravity="end"
        android:orientation="horizontal">

        <Button
            android:id="@+id/button_clear_student"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/clear" />

        <Button
            android:id="@+id/button_add_student"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:text="@string/add" />
    </LinearLayout>

</LinearLayout>'''

with open(path_dialog, 'w', encoding='utf-8') as f:
    f.write(dialog_content)

# 3. Update MainActivity.java
path_java = 'app/src/main/java/com/example/lab_5a_mobile/MainActivity.java'
with open(path_java, 'r', encoding='utf-8') as f:
    java_content = f.read()

# Add NumberPicker import
if 'import android.widget.NumberPicker;' not in java_content:
    java_content = java_content.replace('import android.widget.TextView;', 'import android.widget.TextView;\nimport android.widget.NumberPicker;')

# Update showNewStudentDialog method
old_dialog_method = '''    private void showNewStudentDialog() {
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
    }'''

new_dialog_method = '''    private void showNewStudentDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_student, null);
        EditText idInput = dialogView.findViewById(R.id.edit_student_id);
        EditText nameInput = dialogView.findViewById(R.id.edit_student_name);
        NumberPicker agePicker = dialogView.findViewById(R.id.np_student_age);
        Button addButton = dialogView.findViewById(R.id.button_add_student);
        Button clearButton = dialogView.findViewById(R.id.button_clear_student);

        agePicker.setMinValue(1);
        agePicker.setMaxValue(150);
        agePicker.setValue(20);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.new_student)
                .setView(dialogView)
                .setNegativeButton(R.string.cancel, null)
                .create();

        clearButton.setOnClickListener(view -> {
            idInput.setText("");
            nameInput.setText("");
            agePicker.setValue(20);
            idInput.requestFocus();
        });

        addButton.setOnClickListener(view -> {
            String id = idInput.getText().toString().trim();
            String name = nameInput.getText().toString().trim();
            int age = agePicker.getValue();

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

            Student student = new Student(id, name, age);
            students.add(student);
            addStudentButton(student);
            dialog.dismiss();
        });

        dialog.setOnShowListener(listener -> idInput.requestFocus());
        dialog.show();
    }'''

# Replace using a more robust way just in case whitespace differs slightly
# Using regex to replace the entire method
pattern = r'private void showNewStudentDialog\(\) \{[\s\S]*?dialog\.show\(\);\n    \}'
java_content = re.sub(pattern, new_dialog_method.strip(), java_content)

with open(path_java, 'w', encoding='utf-8') as f:
    f.write(java_content)

print("UI tweaks complete")