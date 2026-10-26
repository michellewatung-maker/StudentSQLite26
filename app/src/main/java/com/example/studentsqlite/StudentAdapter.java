package com.example.studentsqlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter
        extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {

    public interface OnStudentAction {
        void onEdit(Student student);
        void onDelete(Student student);
    }

    private final List<Student> students = new ArrayList<>();
    private final OnStudentAction listener;

    public StudentAdapter(OnStudentAction listener) {
        this.listener = listener;
    }

    public void setStudents(List<Student> newStudents) {
        students.clear();
        students.addAll(newStudents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull StudentHolder holder, int position) {
        Student student = students.get(position);

        holder.textId.setText("Student ID: " + student.id);
        holder.textName.setText("Name: " + student.name);
        holder.textMajor.setText("Major: " + student.major);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(student));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(student));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentHolder extends RecyclerView.ViewHolder {
        TextView textId, textName, textMajor;
        Button btnEdit, btnDelete;

        StudentHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            textName = itemView.findViewById(R.id.textName);
            textMajor = itemView.findViewById(R.id.textMajor);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}