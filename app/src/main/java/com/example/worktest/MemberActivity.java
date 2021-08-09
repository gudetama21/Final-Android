package com.example.worktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemberActivity extends AppCompatActivity {
    private EditText oldpass, newpass, newpass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        setTitle("個人設定");

        oldpass = findViewById(R.id.oldpass);
        newpass = findViewById(R.id.newpass);
        newpass2 = findViewById(R.id.newpass2);
        Button change_button = findViewById(R.id.ch_button);
        change_button.setOnClickListener(change_btn);

        newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newpass.getText().toString().length() < 6) {
                    newpass.setError("密碼長度不夠");
                } else {
                    newpass.setError(null);
                }
            }
        });

    }

    private Button.OnClickListener change_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {
                    String old = oldpass.getText().toString();
                    String new1 = newpass.getText().toString();
                    String new2 = newpass2.getText().toString();
                    Intent i =getIntent();
                    String email = i.getStringExtra("email");
                    String id = i.getStringExtra("id");

                    Looper.prepare();
                    Toast toast;
                    //檢查兩次輸入的新密碼是否相同
                    if(!new1.equals(new2)){
                        toast = Toast.makeText(getApplicationContext(), "新密碼不一致", Toast.LENGTH_SHORT);
                    }
                    else{
                        MysqlCon con = new MysqlCon();
                        boolean check;
                        if(id.equals("cus")){
                            check = con.checkCusData(email, old);
                            if(!check){
                                toast = Toast.makeText(getApplicationContext(), "原密碼錯誤", Toast.LENGTH_SHORT);
                            }
                            else{
                                con.changeCusPass(old, new1);
                                toast = Toast.makeText(getApplicationContext(), "密碼修改完成", Toast.LENGTH_SHORT);
                            }
                        }
                        else{
                            check = con.checkResData(email, old);
                            if(!check){
                                toast = Toast.makeText(getApplicationContext(), "原密碼錯誤", Toast.LENGTH_SHORT);
                            }
                            else{
                                con.changeResPass(old, new1);
                                toast = Toast.makeText(getApplicationContext(), "密碼修改完成", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    toast.show();
                    Looper.loop();
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
                intent.setClass(MemberActivity.this, MainActivity.class);
                SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
                break;

            case R.id.change:
                Intent i = new Intent();
                i.setClass(MemberActivity.this, MemberActivity.class);
                i.putExtra("email",i.getStringExtra("email"));
                i.putExtra("id", i.getStringExtra("id"));
                startActivity(i);
                break;
        }
        return true;
    }
}

