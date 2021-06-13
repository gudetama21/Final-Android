package com.example.worktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> aList;
    private ListView lv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setTitle("餐桌");

        Button add = findViewById(R.id.add);
        add.setOnClickListener(add_btn);
        Button send = findViewById(R.id.send);
        send.setOnClickListener(send_btn);

        adapter = new ArrayAdapter<String>(this,R.layout.my_select_dialog_multichoice);
        lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setFastScrollEnabled(true);
        aList = new ArrayList();
    }

    private Button.OnClickListener add_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {
                    final EditText account = findViewById(R.id.account);
                    Intent i =getIntent();
                    String email = i.getStringExtra("email");

                    String s = account.getText().toString();
                    MysqlCon con = new MysqlCon();
                    //檢查有無重複帳號
                    boolean repeat = false;
                    for(int k=0; k<aList.size(); k++){
                        if(s.equals(aList.get(k))){
                            repeat=true;
                        }
                    }
                    if(!s.equals(email) && !repeat) {
                        boolean bool = con.checkAccount(s);

                        Looper.prepare();
                        Toast toast;
                        if (bool) {
                            //增加列表
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    adapter.add(s);
                                    lv.setItemChecked(aList.size(),true);
                                    aList.add(s);
                                }
                            });

                            //清空EditText
                            account.post(new Runnable() {
                                public void run() {
                                    account.setText("");
                                }
                            });
                            toast = Toast.makeText(getApplicationContext(), "已成功加入", Toast.LENGTH_SHORT);
                        } else {
                            toast = Toast.makeText(getApplicationContext(), "查無此帳號", Toast.LENGTH_SHORT);
                        }
                        toast.show();
                        Looper.loop();
                    }
                }
            }).start();
        }
    };

    private Button.OnClickListener send_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {

                }
            }).start();
        }
    };
}
