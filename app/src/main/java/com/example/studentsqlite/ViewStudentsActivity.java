package com.example.studentsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewStudentsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private StudentAdapter adapter;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        setTitle("Students");

        dbHelper = new DBHelper(this);
        textEmpty = findViewById(R.id.textEmpty);

        RecyclerView recyclerView = findViewById(R.id.recyclerStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StudentAdapter(new StudentAdapter.OnStudentAction() {
            @Override
            public void onEdit(Student student) {
                Intent intent = new Intent(
                        ViewStudentsActivity.this, MainActivity.class
                );
                intent.putExtra("original_id", student.id);
                intent.putExtra("name", student.name);
                intent.putExtra("major", student.major);
                startActivity(intent);
            }

            @Override
            public void onDelete(Student student) {
                new AlertDialog.Builder(ViewStudentsActivity.this)
                        .setTitle("Delete student?")
                        .setMessage(student.name + " (" + student.id + ")")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbHelper.deleteStudent(student.id);
                            refreshStudents();
                        })
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStudents();
    }

    private void refreshStudents() {
        List<Student> students = dbHelper.getAllStudents();
        adapter.setStudents(students);
        textEmpty.setVisibility(
                students.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}