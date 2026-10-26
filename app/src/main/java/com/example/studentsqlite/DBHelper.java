package com.example.studentsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "students.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "students";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE + " (" +
                        "student_id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "major TEXT NOT NULL" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException(
                "Add a database migration before changing DB_VERSION"
        );
    }

    public boolean addStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put("student_id", student.id);
        values.put("name", student.name);
        values.put("major", student.major);

        long result = getWritableDatabase().insertWithOnConflict(
                TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE
        );
        return result != -1;
    }

    public boolean updateStudent(String originalId, Student student) {
        ContentValues values = new ContentValues();
        values.put("student_id", student.id);
        values.put("name", student.name);
        values.put("major", student.major);

        int rows = getWritableDatabase().updateWithOnConflict(
                TABLE,
                values,
                "student_id = ?",
                new String[]{originalId},
                SQLiteDatabase.CONFLICT_IGNORE
        );
        return rows > 0;
    }

    public boolean deleteStudent(String id) {
        int rows = getWritableDatabase().delete(
                TABLE, "student_id = ?", new String[]{id}
        );
        return rows > 0;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(
                TABLE,
                new String[]{"student_id", "name", "major"},
                null, null, null, null,
                "name COLLATE NOCASE ASC"
        );

        try {
            while (cursor.moveToNext()) {
                students.add(new Student(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            }
        } finally {
            cursor.close();
        }

        return students;
    }
}