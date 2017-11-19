package com.tni_it09.thequizcross;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;

import android.widget.Toast;

import java.io.File;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateFolder();
       activeFullScreen();

        //fragment MAIN MENU
        MainMenu fragment = new MainMenu();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();

    }
    //life cycle Activity
    @Override
    protected void onPause() {
        activeFullScreen();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        activeFullScreen();
        super.onRestart();
    }
    void activeFullScreen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onResume() {
        activeFullScreen();
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void CreateFolder(){
        File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"TheQuizCross"+File.separator+"PIC");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File folder2 = new File(Environment.getExternalStorageDirectory()+File.separator+"TheQuizCross"+File.separator+"TITLE");
        if (!folder2.exists()) {
            folder2.mkdirs();
        }

    }

}
