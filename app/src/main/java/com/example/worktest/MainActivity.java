package com.example.worktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private MysqlCon con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {

            public void run() {
                con = new MysqlCon();
                con.run();
                //保持登入
                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                Intent intent = new Intent();
                if(sp.getString("id","").equals("cus")){
                    boolean check = con.checkInfo(sp.getString("email",""));
                    if(check){
                        intent.setClass(MainActivity.this, TableActivity.class);
                    }
                    else{
                        intent.setClass(MainActivity.this, StatusActivity.class);
                    }
                    intent.putExtra("email",sp.getString("email",""));
                    intent.putExtra("id", sp.getString("id",""));
                    startActivity(intent);
                }
                else if(sp.getString("id","").equals("res")){
                    intent.setClass(MainActivity.this, MemberActivity.class);
                    intent.putExtra("email",sp.getString("email",""));
                    intent.putExtra("id", sp.getString("id",""));
                    startActivity(intent);
                }
            }
        }).start();

        setContentView(R.layout.activity_main);
        setTitle("註冊登入");

        Button ok_button = findViewById(R.id.ok_button);
        ok_button.setOnClickListener(ok_btn);
        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(login_btn);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().toString().length()<7 || !username.getText().toString().contains("@")) {
                    username.setError("帳號不可行");
                } else {
                    username.setError(null);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().length() < 6) {
                    password.setError("密碼長度不夠");
                } else {
                    password.setError(null);
                }
            }
        });

    }
    //註冊
    private Button.OnClickListener ok_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {

                    final EditText username = findViewById(R.id.username);
                    final EditText password = findViewById(R.id.password);
                    final RadioButton cus= findViewById(R.id.cus);
                    String email = username.getText().toString();
                    String pass = password.getText().toString();

                    if(cus.isChecked()) {
                        if (email.length() > 6 && email.contains("@") && pass.length() > 5) {
                            // 將資料寫入資料庫
                            con = new MysqlCon();
                            boolean bool = con.insertData(email, pass);

                            Looper.prepare();
                            Toast toast;
                            if (bool) {
                                toast = Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT);
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, StatusActivity.class);
                                intent.putExtra("email", email);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("email", email);
                                editor.putString("password", pass);
                                editor.putString("id", "cus");
                                editor.apply();

                                startActivity(intent);
                            } else {
                                // 清空 EditText
                                password.post(new Runnable() {
                                    public void run() {
                                        password.setText("");
                                        username.setText("");
                                    }
                                });
                                toast = Toast.makeText(getApplicationContext(), "此帳號已註冊", Toast.LENGTH_SHORT);
                            }
                            toast.show();
                            Looper.loop();
                        }
                    }
                }
            }).start();
        }

    };
    //登入
    private Button.OnClickListener login_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {
                    // 取得 EditText 資料
                    final EditText username = findViewById(R.id.username);
                    final EditText password = findViewById(R.id.password);
                    final RadioGroup user = findViewById(R.id.userGroup);
                    String email = username.getText().toString();
                    String pass = password.getText().toString();
                    boolean bool=false;
                    String id = "";

                    if(email.length()>6 && email.contains("@") && pass.length()>5) {

                        con = new MysqlCon();
                        if(user.getCheckedRadioButtonId() == R.id.cus){
                            bool = con.checkCusData(email, pass);
                            id = "cus";
                        }
                        else if(user.getCheckedRadioButtonId() == R.id.res){
                            bool = con.checkResData(email, pass);
                            id = "res";
                        }
                        Looper.prepare();
                        Toast toast;
                        if (bool) {
                            toast = Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_SHORT);
                            //依登入身分跳轉頁面
                            Intent intent = new Intent();
                            if(user.getCheckedRadioButtonId() == R.id.cus){
                                intent.setClass(MainActivity.this, TableActivity.class);
                            }
                            else{
                                intent.setClass(MainActivity.this, MemberActivity.class);
                            }
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", email);
                            editor.putString("password", pass);
                            editor.putString("id", id);
                            editor.apply();

                            intent.putExtra("email",email);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        } else {
                            toast = Toast.makeText(getApplicationContext(), "帳號或密碼有誤", Toast.LENGTH_SHORT);
                        }
                        toast.show();
                        Looper.loop();
                    }
                }
            }).start();
        }

    };

    public void onBackPressed() {
        moveTaskToBack(true);
    }
}