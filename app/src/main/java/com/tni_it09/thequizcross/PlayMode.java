package com.tni_it09.thequizcross;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMode extends Fragment {
    ManageDB manageDB;

    Adapter_sent adapter_sent;
    String T="";
    Button btnAns1,btnAns2,btnAns3,btnAns4;
    TextView txtShowQ,txtEX,txtTitle;
    ImageView img;

    String TITLE="",QUESTION="",ANSWER="",PATH="",STATE="";
    String Ans1="",Ans2="",Ans3="";

    int checkANS;

    public PlayMode() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_play_mode, container, false);
        Button btn_back = (Button) rootView.findViewById(R.id.btn_back);
        btnAns1 = (Button) rootView.findViewById(R.id.btnA1);
        btnAns2 = (Button) rootView.findViewById(R.id.btnA2);
        btnAns3 = (Button) rootView.findViewById(R.id.btnA3);
        btnAns4 = (Button) rootView.findViewById(R.id.btnA4);
        txtShowQ = (TextView) rootView.findViewById(R.id.text_Question_show);
        txtEX = (TextView) rootView.findViewById(R.id.textex);
        txtTitle = (TextView) rootView.findViewById(R.id.text_title);
        img = (ImageView)rootView.findViewById(R.id.image_q);

        adapter_sent = new Adapter_sent(getContext());
        T = adapter_sent.getString();

        txtTitle.setText(T);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenu fragment = new MainMenu();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                //transaction.addToBackStack(null);
                transaction.commit();//
                //transaction.remove(fragment);
                //getFragmentManager().beginTransaction().remove(PlayMode.this).commit();
            }
        });

        manageDB= new ManageDB(getContext());
        setQUERY(T,rootView);
        event(rootView);
        return rootView;
    }

    private void event(final View view){
        btnAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkANS ==1){
                    //btnAns1.setBackground(getResources().getDrawable(R.drawable.shape_button_red));
                    isTRUE();
                }else {
                    btnAns1.setBackground(getResources().getDrawable(R.drawable.shape_button_red));
                    isClear();
                    Toast.makeText(getContext(),"F",Toast.LENGTH_SHORT).show();
                }
                setQUERY(T,view);
            }
        });
        btnAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkANS ==2){
                    isTRUE();
                }else {
                    btnAns2.setBackground(getResources().getDrawable(R.drawable.shape_button_red));
                    isClear();
                    Toast.makeText(getContext(),"F",Toast.LENGTH_SHORT).show();
                }
                setQUERY(T,view);
            }
        });
        btnAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkANS ==3){
                    isTRUE();
                }else {
                    btnAns3.setBackground(getResources().getDrawable(R.drawable.shape_button_red));
                    isClear();
                    Toast.makeText(getContext(),"F",Toast.LENGTH_SHORT).show();
                }
                setQUERY(T,view);
            }
        });
        btnAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkANS ==4){
                    isTRUE();
                }else {
                    btnAns4.setBackground(getResources().getDrawable(R.drawable.shape_button_red));
                    isClear();
                    Toast.makeText(getContext(),"F",Toast.LENGTH_SHORT).show();
                }
                setQUERY(T,view);
            }
        });

    }

    private void setQUERY(String title,View view){

        try{
            String[] tmpTQAPS = manageDB.query_RND_STATE(title).get(0).toString().split("->");
            TITLE = tmpTQAPS[0];
            QUESTION = tmpTQAPS[1];
            ANSWER = tmpTQAPS[2];
            PATH = tmpTQAPS[3];
            STATE = tmpTQAPS[4];
        }catch (Exception e){}

        try {
            Ans1 = manageDB.queryRND_A1(title,QUESTION).get(0).toString();
        }catch (Exception e){}
        try {
            Ans2 = manageDB.queryRND_A2(title,QUESTION,Ans1).get(0).toString();
        }catch (Exception e){}
        try {
            Ans3 = manageDB.queryRND_A3(title,QUESTION,Ans1,Ans2).get(0).toString();
        }catch (Exception e){}

        if(TITLE.equals("")){
            isClear();
            btnAns1.setEnabled(false);
            btnAns2.setEnabled(false);
            btnAns3.setEnabled(false);
            btnAns4.setEnabled(false);
            img.setImageDrawable(getResources().getDrawable(R.drawable.qmak));

        }

        ChoiceRANDOM();
        loadImageFromStorage(view,PATH);
        txtEX.setText(manageDB.getNumState(title)+"");

    }


    private void ChoiceRANDOM(){
        if(Ans3.equals("")){
            //Toast.makeText(getContext(),"A4 null",Toast.LENGTH_SHORT).show();
        }

        Random random = new Random();
        int R = random.nextInt(4);
        switch (R){
            case 0:
                if(Ans1.equals(""))
                    btnAns2.setEnabled(false);
                else btnAns2.setEnabled(true);
                if(Ans2.equals(""))
                    btnAns3.setEnabled(false);
                else btnAns3.setEnabled(true);
                if(Ans3.equals(""))
                    btnAns4.setEnabled(false);
                else btnAns4.setEnabled(true);

                if(!TITLE.equals("")) btnAns1.setEnabled(true);
                btnAns1.setText(ANSWER);
                btnAns2.setText(Ans1);
                btnAns3.setText(Ans2);
                btnAns4.setText(Ans3);
                checkANS = 1;
                break;
            case 1:
                if(Ans1.equals(""))
                    btnAns1.setEnabled(false);
                else btnAns1.setEnabled(true);
                if(Ans2.equals(""))
                    btnAns3.setEnabled(false);
                else btnAns3.setEnabled(true);
                if(Ans3.equals(""))
                    btnAns4.setEnabled(false);
                else btnAns4.setEnabled(true);
                btnAns1.setText(Ans1);
                if(!TITLE.equals("")) btnAns2.setEnabled(true);
                btnAns2.setText(ANSWER);
                btnAns3.setText(Ans2);
                btnAns4.setText(Ans3);
                checkANS = 2;
                break;
            case 2:
                if(Ans2.equals(""))
                    btnAns1.setEnabled(false);
                else btnAns1.setEnabled(true);
                if(Ans1.equals(""))
                    btnAns2.setEnabled(false);
                else btnAns2.setEnabled(true);
                if(Ans3.equals(""))
                    btnAns4.setEnabled(false);
                else btnAns4.setEnabled(true);
                btnAns1.setText(Ans2);
                btnAns2.setText(Ans1);
                if(!TITLE.equals("")) btnAns3.setEnabled(true);
                btnAns3.setText(ANSWER);
                btnAns4.setText(Ans3);
                checkANS = 3;
                break;
            case 3:
                if(Ans3.equals(""))
                    btnAns1.setEnabled(false);
                else btnAns1.setEnabled(true);
                if(Ans1.equals(""))
                    btnAns2.setEnabled(false);
                else btnAns2.setEnabled(true);
                if(Ans2.equals(""))
                    btnAns3.setEnabled(false);
                else btnAns3.setEnabled(true);
                btnAns1.setText(Ans3);
                btnAns2.setText(Ans1);
                btnAns3.setText(Ans2);
                if(!TITLE.equals("")) btnAns4.setEnabled(true);
                btnAns4.setText(ANSWER);
                checkANS = 4;
                break;
        }
        txtShowQ.setText(QUESTION);
    }

    private void loadImageFromStorage(View rootView,String PathFile) {
        File Root = Environment.getExternalStorageDirectory();
        try {
            File f=new File(PathFile);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private void runThread(final View root) {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public void onPause() {
        super.onPause();
    }
    private void isTRUE(){
        manageDB.updateState(QUESTION,"1");
        TITLE = "";
        QUESTION = "";
        ANSWER = "";
        PATH ="";
        STATE = "";
        Ans1="";
        Ans2="";
        Ans3="";
        btnAns1.setText("");
        btnAns2.setText("");
        btnAns3.setText("");
        btnAns4.setText("");
        Toast.makeText(getContext(),"T",Toast.LENGTH_SHORT).show();
    }

    private void isClear(){
        //Thread();
        TITLE = "";
        QUESTION = "";
        ANSWER = "";
        PATH ="";
        STATE = "";
        Ans1="";
        Ans2="";
        Ans3="";
        btnAns1.setText("");
        btnAns2.setText("");
        btnAns3.setText("");
        btnAns4.setText("");
        btnAns1.setBackground(getResources().getDrawable(R.drawable.shape__button_white));
        btnAns2.setBackground(getResources().getDrawable(R.drawable.shape__button_white));
        btnAns3.setBackground(getResources().getDrawable(R.drawable.shape__button_white));
        btnAns4.setBackground(getResources().getDrawable(R.drawable.shape__button_white));
    }
    private void Thread(){
        Thread t = new Thread();
        try {
            t.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
