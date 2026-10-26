package com.example.studentsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editName, editId;
    private Spinner spinnerMajor;
    private DBHelper dbHelper;
    private String originalId; // Non-null when editing an existing student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editId = findViewById(R.id.editId);
        spinnerMajor = findViewById(R.id.spinnerMajor);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnView = findViewById(R.id.btnView);
        dbHelper = new DBHelper(this);

        originalId = getIntent().getStringExtra("original_id");

        if (originalId != null) {
            setTitle("Edit Student");
            btnSave.setText("Save Changes");
            btnView.setVisibility(View.GONE);

            editId.setText(originalId);
            editName.setText(getIntent().getStringExtra("name"));

            String currentMajor = getIntent().getStringExtra("major");
            String[] majors = getResources().getStringArray(R.array.majors);
            for (int i = 0; i < majors.length; i++) {
                if (majors[i].equals(currentMajor)) {
                    spinnerMajor.setSelection(i);
                    break;
                }
            }
        }

        btnSave.setOnClickListener(v -> saveStudent());

        btnView.setOnClickListener(v ->
                startActivity(new Intent(this, ViewStudentsActivity.class))
        );
    }

    private void saveStudent() {
        String name = editName.getText().toString().trim();
        String id = editId.getText().toString().trim();
        String major = spinnerMajor.getSelectedItem().toString();

        if (name.isEmpty()) {
            editName.setError("Enter a student name");
            editName.requestFocus();
            return;
        }

        if (id.isEmpty()) {
            editId.setError("Enter a student ID");
            editId.requestFocus();
            return;
        }

        Student student = new Student(id, name, major);
        boolean success;

        if (originalId == null) {
            success = dbHelper.addStudent(student);
        } else {
            success = dbHelper.updateStudent(originalId, student);
        }

        if (!success) {
            editId.setError("Student ID already exists");
            editId.requestFocus();
            return;
        }

        Toast.makeText(this, "Student saved", Toast.LENGTH_SHORT).show();

        if (originalId != null) {
            finish(); // Return to the student list
        } else {
            editName.setText("");
            editId.setText("");
            spinnerMajor.setSelection(0);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}