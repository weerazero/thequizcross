package com.tni_it09.thequizcross;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */


public class MainMenu extends Fragment {

    ManageDB manageDB;
    private boolean checkOn = false;

    private ListViewAdapter listAdapter;
    private ListView listView;

    private ArrayList TITLE;
    private int size=0;
    private int P=0;

    private Button btn_addTitle,btn_addQuiz;
    private TextView txtGameAll,txtPointx,txtLV;
    public MainMenu() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        checkOn = true;
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        P=0;
        manageDB = new ManageDB(getContext());
        btn_addTitle = (Button) rootView.findViewById(R.id.button_add_Titlex);
        btn_addQuiz = (Button) rootView.findViewById(R.id.button_addQuiz);
        txtGameAll = (TextView) rootView.findViewById(R.id.textView5);
        txtPointx = (TextView)rootView.findViewById(R.id.point);
        txtLV = (TextView) rootView.findViewById(R.id.textView18);
        int mm=manageDB.getPoint();
        txtPointx.setText(mm+"");
        if(mm<5)
            txtLV.setText("Beginner");
        else if(mm<10)
            txtLV.setText("Novice");
        else if(mm<20)
            txtLV.setText("Master");
        else if(mm<40)
            txtLV.setText("Professional");
        else if(mm>=40)
            txtLV.setText("MAX");
        listView = (ListView) rootView.findViewById(R.id.list);
        try {
            size = manageDB.queryTitle().size();
        }catch (Exception e){}
        txtGameAll.setText(size+"");
        TITLE = manageDB.queryTitle();

        listAdapter = new ListViewAdapter();
        listView.setAdapter(listAdapter);


        // we take the background image and button reference from the header

        btn_addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditQC fragment = new EditQC();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btn_addTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTitle fragment = new AddTitle();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Adapter_sent adapter_sent = new Adapter_sent(getContext());
                adapter_sent.setString(TITLE.get(arg2).toString());
                PlayMode fragment = new PlayMode();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();//
            }
        });

        return rootView;
    }

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder= null;
            if (view == null) {

                view = getActivity().getLayoutInflater().inflate(R.layout.item_listview, null);
                holder = new ViewHolder();
                holder.name= (TextView) view.findViewById(R.id.item_listview_title);
                holder.icon= (ImageView) view.findViewById(R.id.item_listview_authorIcon);

                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();

            }

            holder.name.setText(TITLE.get(i).toString());
            holder.icon.setImageBitmap(loadImageFromStorage(manageDB.queryTitlePath().get(i).toString()));
            return view;
        }

    }

    private Bitmap loadImageFromStorage(String PathFile) {
        Bitmap b = null;
        try {
            File f=new File(PathFile);
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public class ViewHolder {
        TextView name;
        ImageView icon;
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