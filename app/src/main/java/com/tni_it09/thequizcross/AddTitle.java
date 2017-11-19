package com.tni_it09.thequizcross;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTitle extends Fragment {

    private DatabaseQC mHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor,cursor1;
    private String imagePath = "",TITLE="";

    private Button btn_add_title,btn_clear,btn_delete,btn_addT_back;
    private ImageView img_view_title;
    private EditText edit_title;
    private RelativeLayout scn;

    private static final int REQUEST_GALLERY = 1;
    private Bitmap bitmap;

    Spinner data;

    public AddTitle() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_title, container, false);
        scn = (RelativeLayout) root.findViewById(R.id.sereen_TITLE);
        btn_add_title = (Button) root.findViewById(R.id.button_ADD_TITLE);
        btn_delete = (Button) root.findViewById(R.id.button_delete_title);
        btn_clear = (Button) root.findViewById(R.id.button_clear_pic);
        btn_addT_back =(Button) root.findViewById(R.id.button_back_editT) ;
        img_view_title = (ImageView) root.findViewById(R.id.imageView_TITLE);
        edit_title = (EditText) root .findViewById(R.id.edit_TITLE);
        data = (Spinner) root.findViewById(R.id.spinner_title);
        mHelper = new DatabaseQC(getContext());
        mDb = mHelper.getWritableDatabase();

        event();
        return root;
    }

    private void event(){
        btn_addT_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenu fragment = new MainMenu();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();//
            }
        });
        scn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeFullScreen();
            }
        });
        img_view_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentT = new Intent(Intent.ACTION_GET_CONTENT);
                intentT.setType("image/*");
                startActivityForResult(Intent.createChooser(intentT,"Select Picture"),REQUEST_GALLERY);
            }
        });


        btn_add_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_title.getText().toString();

                if(title.length() != 0) {

                    Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME2
                            + " WHERE " +DatabaseQC.COL_TITLE + "='" +title+"'",null);

                    //INSERT into
                    if(mCursor.getCount() == 0) { //DATA IS NULL Cursor.getcount = 0
                        imagePath = saveToInternalStorage(bitmap,title);
                        mDb.execSQL("INSERT INTO " + DatabaseQC.TABLE_NAME2 + " ("
                                + DatabaseQC.COL_TITLE + ", " + DatabaseQC.COL_imgT +") VALUES ('" + title
                                + "', '" + imagePath + "');");
                        edit_title.setText("");
                        imagePath = "";
                        Toast.makeText(getContext(), "เพิ่มข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "คุณมีข้อมูลนี้อยู่แล้ว", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    activeFullScreen();
                    Toast.makeText(getContext(), "กรุณาใส่ข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }
                event();
                activeFullScreen();
            }
        });

        cursor = mDb.rawQuery("SELECT * FROM " + DatabaseQC.TABLE_NAME2 + ";", null);
        final ArrayList<String> arr_list_title = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arr_list_title.add(cursor.getString(cursor.getColumnIndex(DatabaseQC.COL_TITLE)));
            cursor.moveToNext();
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

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   if(!TITLE.equals("")||TITLE== null) {
                       cursor1 = mDb.rawQuery("SELECT " + DatabaseQC.COL_imgT + " FROM " + DatabaseQC.TABLE_NAME2 + " WHERE " + DatabaseQC.COL_TITLE + " = '" + TITLE + "';", null);
                       final ArrayList<String> arr_list_P = new ArrayList<String>();
                       cursor1.moveToFirst();
                       while (!cursor1.isAfterLast()) {
                           arr_list_P.add(cursor1.getString(cursor1.getColumnIndex(DatabaseQC.COL_imgT)));
                           cursor1.moveToNext();
                       }

                       mDb.execSQL("DELETE FROM " + DatabaseQC.TABLE_NAME2 + " WHERE " + DatabaseQC.COL_TITLE + " = '" + TITLE + "';");
                       try {
                           FileDelete(arr_list_P.get(0).toString());
                       } catch (Exception e) {
                       }
                       Toast.makeText(getContext(), "Title is deleted", Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(getContext(), "Title is null", Toast.LENGTH_SHORT).show();
                   }
                   event();
                   activeFullScreen();
               }catch (Exception e){}

            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap  =null;
                img_view_title.setBackground(getResources().getDrawable(R.drawable.shape_button_gray));
                event();
                activeFullScreen();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_GALLERY) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    img_view_title.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String saveToInternalStorage(Bitmap bitmapImage,String Pname){
        File Root = Environment.getExternalStorageDirectory();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Root+"/TheQuizCross/TITLE/"+Pname+".jpg");
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
        return Root+"/TheQuizCross/TITLE/"+Pname+".jpg";
    }

    public void FileDelete(String path){
        File file = new File(path);
        file.delete();
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
