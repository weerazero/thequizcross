package com.tni_it09.thequizcross;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditQC extends Fragment {
    private boolean checkOn = false;

    DatabaseQC mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor,CursorTitle;
    ListView listview;

    EditText edit_Q;
    Button btnBackeditQC;

    public static final int REQUEST_GALLERY = 1;
    Bitmap bitmap;
    ImageView imageQ;
    String imagePath = "";

    String state = "0",TITLE="";
    EditText editQuestion,editAnswer;
    Spinner data;
    RelativeLayout scr;
    public EditQC() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_edit_qc, container, false);
        editQuestion = (EditText)rootView.findViewById(R.id.edit_QUESTION);
        editAnswer = (EditText)rootView.findViewById(R.id.edit_ANSWER);
        listview = (ListView) rootView.findViewById(R.id.listqc);
        data = (Spinner) rootView.findViewById(R.id.spinnerspinner_data);
        imageQ = (ImageView) rootView.findViewById(R.id.image_ADD);
        scr = (RelativeLayout) rootView.findViewById(R.id.screen_edit_);
        btnBackeditQC = (Button) rootView.findViewById(R.id.button_back_edit);


        scr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeFullScreen();
            }
        });
        imageQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentT = new Intent(Intent.ACTION_GET_CONTENT);
                intentT.setType("image/*");
                startActivityForResult(Intent.createChooser(intentT,"Select Picture"),REQUEST_GALLERY);
            }
        });

        //--------------------DB----------------------
        mHelper = new DatabaseQC(getActivity());
        mDb = mHelper.getWritableDatabase();


        btnBackeditQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenu fragment = new MainMenu();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();//
            }
        });
        Button buttonAdd = (Button)rootView.findViewById(R.id.btn_addDB);
        //ADD BUTTON
        ShowLIST();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String title = TITLE;
                String question = editQuestion.getText().toString();
                String answer = editAnswer.getText().toString();

                if(title.length() != 0 && question.length() != 0 && answer.length() != 0 ) {

                    Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME
                            + " WHERE " +DatabaseQC.COL_question + "='" +question+"'",null);

                    //INSERT into
                    if(mCursor.getCount() == 0) { //DATA IS NULL Cursor.getcount = 0
                        imagePath = saveToInternalStorage(bitmap,question);
                        mDb.execSQL("INSERT INTO " + DatabaseQC.TABLE_NAME + " ("
                                + DatabaseQC.COL_title + ", " + DatabaseQC.COL_question
                                + ", " + DatabaseQC.COL_answer +", "+DatabaseQC.COL_imagePath + ", "+DatabaseQC.COL_state +") VALUES ('" + title
                                + "', '" + question + "', '" + answer + "', '" + imagePath + "', '" +state+ "');");

                        editQuestion.setText("");
                        editAnswer.setText("");
                        imagePath = "";

                        Toast.makeText(getContext(), "เพิ่มข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "คุณมีข้อมูลนี้อยู่แล้ว", Toast.LENGTH_SHORT).show();
                    }
                } else {
                   activeFullScreen();
                    Toast.makeText(getContext(), "กรุณาใส่ข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }
                ShowLIST();
            }
        });
        return rootView;
    }
    public void onResume() {
        ShowLIST();
    }
    public void ShowLIST(){
        checkOn = true;
        super.onResume();

        mHelper = new DatabaseQC(getContext());
        mDb = mHelper.getWritableDatabase();

        //---------------------------------QUERY title
            CursorTitle = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME2 + ";", null);
            final ArrayList<String> arr_list_title = new ArrayList<String>();
            CursorTitle.moveToFirst();
            while (!CursorTitle.isAfterLast()) {
                arr_list_title.add(CursorTitle.getString(CursorTitle.getColumnIndex(DatabaseQC.COL_TITLE)));
                CursorTitle.moveToNext();
            }

            ArrayAdapter<String> adapterDB = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arr_list_title);

            data.setAdapter(adapterDB);

            data.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TITLE = arr_list_title.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

        //--------------------ADD----------------------------
        mCursor = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME, null);

        listview.setAdapter(updateListView());
        //detect event click listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mCursor.moveToPosition(arg2);
                String question = mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_question));
                updateState(question,"0");
                mCursor.requery();
                listview.setAdapter(updateListView());
                Toast.makeText(getContext(),"อัพเดทข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();

            }
        });
        //detect event longCLICK
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mCursor.moveToPosition(arg2);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("ลบข้อมูล");
                builder.setMessage("คุณต้องการลบข้อมูลนี้ใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String title = mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_title));
                        String question = mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_question));
                        String answer = mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_answer));
                        String imageP = mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_imagePath));
                        String stateC = mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_state));
                        mDb.execSQL("DELETE FROM " + DatabaseQC.TABLE_NAME
                                + " WHERE " + DatabaseQC.COL_title + "='" + title + "'"
                                + " AND " + DatabaseQC.COL_question + "='" + question + "'"
                                + " AND " + DatabaseQC.COL_answer + "='" + answer + "'"
                                + " AND " + DatabaseQC.COL_imagePath + "='" + imageP + "'"
                                + " AND " + DatabaseQC.COL_state+ "='" + stateC + "';");

                        mCursor.requery();

                        listview.setAdapter(updateListView());

                        FileDelete(imageP);

                        Toast.makeText(getContext(),"ลบข้อมูลเรียบร้อย"
                                , Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                return true;
            }
        });
    }
    public void onStop() {
        super.onStop();
        mHelper.close();
        mDb.close();
    }

    public ArrayAdapter<String> updateListView() {
        ArrayList<String> arr_list = new ArrayList<String>();
        mCursor.moveToFirst();
        while(!mCursor.isAfterLast()){
            arr_list.add("หัวข้อ : " + mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_title)) + "\nคำถาม : "
                    + mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_question)) + "\n"
                    + "คำตอบ : " + mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_answer))+"  "
                    + mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_imagePath))+" "
                    + mCursor.getString(mCursor.getColumnIndex(DatabaseQC.COL_state)));
            mCursor.moveToNext();
        }

        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(getContext(), R.layout.my_listview, arr_list);
        return adapterDir;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        try {
            if (requestCode == REQUEST_GALLERY /*&& resultCode == RESULT_OK*/) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    imageQ.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage,String Qname){
        File Root = Environment.getExternalStorageDirectory();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Root+"/TheQuizCross/PIC/"+Qname+".jpg");
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Root+"/TheQuizCross/PIC/"+Qname+".jpg";
    }
    public void FileDelete(String path){
        File file = new File(path);
        file.delete();
    }
    private void updateState(String questionUp,String state){
        mDb.execSQL("UPDATE "+ DatabaseQC.TABLE_NAME + " SET "+ DatabaseQC.COL_state
                +" = '"+state+"' WHERE "+DatabaseQC.COL_question+" = '"+questionUp+"';");
    }
    void activeFullScreen(){
        View decorView = this.getView().getRootView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


}
