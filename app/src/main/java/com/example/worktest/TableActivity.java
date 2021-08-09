package com.example.worktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class TableActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> aList, oList;
    private ListView lv;
    private MysqlCon con;
    private Intent i ;
    private String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setTitle("餐桌");

        Button add = findViewById(R.id.add);
        add.setOnClickListener(add_btn);
        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(delete_btn);
        Button send = findViewById(R.id.send);
        send.setOnClickListener(send_btn);

        adapter = new ArrayAdapter<String>(this,R.layout.my_select_dialog_multichoice);
        lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setFastScrollEnabled(true);
        aList = new ArrayList<>();
        //已儲存列表
        new Thread(new Runnable() {

            public void run() {
                con = new MysqlCon();
                i =getIntent();
                email = i.getStringExtra("email");
                oList = con.getFri(email);
                runOnUiThread(new Runnable() {
                    public void run() {
                        for(int j=0; j<oList.size(); j++){
                            adapter.add(oList.get(j));
                            lv.setItemChecked(j,true);
                        }
                    }
                });
            }
        }).start();

    }

    private Button.OnClickListener add_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {
                    final EditText account = findViewById(R.id.account);

                    String s = account.getText().toString();
                    con = new MysqlCon();
                    //檢查有無重複帳號
                    boolean repeat = false;
                    for(int k=0; k<aList.size(); k++){
                        if(s.equals(aList.get(k))){
                            repeat=true;
                        }
                    }
                    for(int k=0; k<oList.size(); k++){
                        if(s.equals(oList.get(k))){
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
                                    lv.setItemChecked(adapter.getCount(),true);
                                    adapter.add(s);
                                    aList.add(s);
                                }
                            });
                            con.insertFri(email, s);
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

    private Button.OnClickListener delete_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {
                    for(int j=0; j<adapter.getCount(); j++) {
                        if (lv.isItemChecked(j)) {
                            String s = adapter.getItem(j);
                            con.delFri(email, s);
                            int finalJ = j;
                            if(oList.contains(s)){
                                oList.remove(s);
                            }
                            else {
                                aList.remove(s);
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    adapter.remove(adapter.getItem(finalJ));
                                    lv.setItemChecked(finalJ,false);
                                }
                            });
                        }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                Intent intent = new Intent();
                intent.setClass(TableActivity.this, MainActivity.class);
                SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
                break;
            case R.id.change:
                Intent i = new Intent();
                i.setClass(TableActivity.this, MemberActivity.class);
                i.putExtra("email",i.getStringExtra("email"));
                i.putExtra("id", i.getStringExtra("id"));
                startActivity(i);
                break;
        }
        return true;

    }
}
