package com.tni_it09.thequizcross;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

/**
 * Created by bokee_000 on 3/10/2559.
 */
public class ManageDB {

    private Context context;
    private DatabaseQC mHelper;
    private SQLiteDatabase mDb;

    private Cursor CursorA1,CursorA2,CursorA3,CursorS,CursorT,CursorState,CursorQ,CursorD,CursorE;
    private Cursor CursorTitle,CursorTitleP;
    private Cursor CorsorMem;



    public ManageDB(Context context){
        this.context = context;

        mHelper = new DatabaseQC(context);
        mDb = mHelper.getWritableDatabase();
    }


    public ArrayList queryTitle(){
        CursorTitle = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME2+";", null);
        final ArrayList<String> arr_list_title = new ArrayList<String>();
        CursorTitle.moveToFirst();
        while(!CursorTitle.isAfterLast()){
            arr_list_title.add(CursorTitle.getString(CursorTitle.getColumnIndex(DatabaseQC.COL_TITLE)));
            CursorTitle.moveToNext();
        }
        return arr_list_title;
    }

    public ArrayList queryTitlePath(){
        CursorTitleP = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME2+";", null);
        final ArrayList<String> arr_list_titleP = new ArrayList<String>();
        CursorTitleP.moveToFirst();
        while(!CursorTitleP.isAfterLast()){
            arr_list_titleP.add(CursorTitleP.getString(CursorTitleP.getColumnIndex(DatabaseQC.COL_imgT)));
            CursorTitleP.moveToNext();
        }
        return arr_list_titleP;
    }

    public ArrayList queryTitle_QA_tb(){
        CursorT = mDb.rawQuery("SELECT DISTINCT "+DatabaseQC.COL_title+" FROM " + DatabaseQC.TABLE_NAME+";", null);
        final ArrayList<String> arr_list_T = new ArrayList<String>();
        CursorT.moveToFirst();
        while(!CursorT.isAfterLast()){
            arr_list_T.add(CursorT.getString(CursorT.getColumnIndex(DatabaseQC.COL_title)));
            CursorT.moveToNext();
        }
        return  arr_list_T;
    }

    //query ค่าที่มี title ตามนี้
    public ArrayList queryTitle(String title){
        CursorState = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME+" WHERE "+DatabaseQC.COL_title+" = '"+title+"';", null);
        final ArrayList<String> arr_list_State = new ArrayList<String>();
        CursorState.moveToFirst();
        while(!CursorState.isAfterLast()) {
            arr_list_State.add(""+CursorState.getString(CursorState.getColumnIndex(DatabaseQC.COL_state)));
            CursorState.moveToNext();
        }
        return arr_list_State;
    }

    //query เลือกเป็นคำตอบ
    public ArrayList query_RND_STATE(String title){
        CursorS = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME+" WHERE "+DatabaseQC.COL_state+" = '0' AND " +
                DatabaseQC.COL_title+" = '"+title+"' ORDER BY RANDOM() LIMIT 1;", null);
        final ArrayList<String> arr_list_R = new ArrayList<String>();
        CursorS.moveToFirst();
        while(!CursorS.isAfterLast()) {
            arr_list_R.add(""+CursorS.getString(CursorS.getColumnIndex(DatabaseQC.COL_title)) + "->"
                    + CursorS.getString(CursorS.getColumnIndex(DatabaseQC.COL_question)) + "->"
                    + CursorS.getString(CursorS.getColumnIndex(DatabaseQC.COL_answer)) + "->"
                    + CursorS.getString(CursorS.getColumnIndex(DatabaseQC.COL_imagePath)) + "->"
                    + CursorS.getString(CursorS.getColumnIndex(DatabaseQC.COL_state)));
            CursorS.moveToNext();
        }
        return arr_list_R;
    }


    public void updateState(String questionUp,String state){
        mDb.execSQL("UPDATE "+ DatabaseQC.TABLE_NAME + " SET "+ DatabaseQC.COL_state
                +" = '"+state+"' WHERE "+DatabaseQC.COL_question+" = '"+questionUp+"';");
    }

    //-------------------RANDOM 3 ANSWER
    public ArrayList queryRND_A1(String title,String Question){
        CursorA1 = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME +" WHERE "+DatabaseQC.COL_question+" != '"+Question+"'" +
                " AND "+DatabaseQC.COL_title+" = '"+title+"' ORDER BY RANDOM() LIMIT 1;", null);
        final ArrayList<String> arr_list_A1 = new ArrayList<String>();
        CursorA1.moveToFirst();
        while(!CursorA1.isAfterLast()){
            arr_list_A1.add(""+CursorA1.getString(CursorA1.getColumnIndex(DatabaseQC.COL_answer)));
            CursorA1.moveToNext();
        }
        return arr_list_A1;
    }

    public ArrayList queryRND_A2(String title,String Question,String ansA1){
        CursorA2 = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME +" WHERE "+DatabaseQC.COL_question+" != '"+Question+"'" +
                " AND "+DatabaseQC.COL_title+" = '"+title+"' AND "+DatabaseQC.COL_answer+" != '"+ansA1+"' ORDER BY RANDOM() LIMIT 1;", null);
        final ArrayList<String> arr_list_A2 = new ArrayList<String>();
        CursorA2.moveToFirst();
        while(!CursorA2.isAfterLast()){
            arr_list_A2.add(""+CursorA2.getString(CursorA2.getColumnIndex(DatabaseQC.COL_answer)));
            CursorA2.moveToNext();
        }
        return arr_list_A2;

    }

    public ArrayList queryRND_A3(String title,String Question,String ansA1,String ansA2){
        CursorA3 = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME +" WHERE "+DatabaseQC.COL_question+" != '"+Question+"'" +
                " AND "+DatabaseQC.COL_title+" = '"+title+"' AND "+DatabaseQC.COL_answer+" != '"+ansA1+
                "' AND "+DatabaseQC.COL_answer+" != '"+ansA2+"' ORDER BY RANDOM() LIMIT 1;", null);
        final ArrayList<String> arr_list_A3 = new ArrayList<String>();
        CursorA3.moveToFirst();
        while(!CursorA3.isAfterLast()){
            arr_list_A3.add(""+CursorA3.getString(CursorA3.getColumnIndex(DatabaseQC.COL_answer)));
            CursorA3.moveToNext();
        }
        return arr_list_A3;
    }

    public int getNumState(String title){
        int i=0;
        CursorS = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME+" WHERE "+DatabaseQC.COL_state+" = '0' AND " +
                DatabaseQC.COL_title+" = '"+title+"';", null);
        CursorS.moveToFirst();
        while(!CursorS.isAfterLast()) {
            i++;
            CursorS.moveToNext();
        }
        return i;
    }
    public int getNumTitle(String title){
        int i=0;
        CursorD = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME+" WHERE "+DatabaseQC.COL_title+" = '"+title+"';", null);
        CursorD.moveToFirst();
        while(!CursorD.isAfterLast()) {
            i++;
            CursorD.moveToNext();
        }
        return i;
    }
    public ArrayList getIndexTitle(){
        int i=-1;
        ArrayList<Integer> index = new ArrayList<Integer>();
        CursorE = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME2+";", null);
        CursorE.moveToFirst();
        while(!CursorE.isAfterLast()) {
            i++;
            index.add(i);
            CursorE.moveToNext();
        }
        return index;
    }

    public int getPoint(){
        int i=0;
        CursorQ = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME+" WHERE "+DatabaseQC.COL_state+" = '1';", null);
        CursorQ.moveToFirst();
        while(!CursorQ.isAfterLast()) {
            i++;
            CursorQ.moveToNext();
        }
        return i;
    }
    public void checkTABLE(){

    }

}
