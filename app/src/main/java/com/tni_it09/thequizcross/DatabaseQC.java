package com.tni_it09.thequizcross;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bokee_000 on 2/10/2559.
 */
public class DatabaseQC extends SQLiteOpenHelper{

    private static final String DB_NAME = "DB_QuizCross";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "QA_tb";
    public static final String COL_title = "title";
    public static final String COL_question = "question";
    public static final String COL_answer = "answer";
    public static final String COL_imagePath ="image";
    public static final String COL_state="state";    public static final String TABLE_NAME2 = "Title_tb";
    public static final String COL_TITLE = "title";
    public static final String COL_imgT = "imgt";
    public DatabaseQC(Context context){
        super (context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME2+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_TITLE+" TEXT NOT NULL, "+COL_imgT+" TEXT);");
        //db.execSQL("INSERT INTO "+TABLE_NAME2+" ("+COL_TITLE+", "+COL_imgT+") VALUES ('TTT','/storage/emulated/0/TheQuizCross/TITLE/Pre.jpg');");
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_title + " TEXT NOT NULL, " + COL_question + " TEXT, " + COL_answer + " TEXT, " + COL_imagePath + " TEXT, " + COL_state  + " TEXT);");
        //db.execSQL("INSERT INTO " + TABLE_NAME + " ("+COL_title + ", " + COL_question + ", "+ COL_answer + ", " + COL_imagePath + ", " + COL_state + ") VALUES ('ENL'" + ", 'Hello', 'สวัสดี', '/storage/emulated/0/TheQuizCross/PIC/Pre.jpg','0');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
