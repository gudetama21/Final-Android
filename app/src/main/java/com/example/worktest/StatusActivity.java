package com.example.worktest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class StatusActivity extends AppCompatActivity {
    private EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        setTitle("填寫資料");

        date= findViewById(R.id.date);
        date.setOnClickListener(mClickListener);
        Button s_button = findViewById(R.id.s_button);
        s_button.setOnClickListener(s_btn);
    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            Calendar mcalendar = Calendar.getInstance();
            int day= mcalendar.get(Calendar.DAY_OF_MONTH);
            int year= mcalendar.get(Calendar.YEAR);
            int month= mcalendar.get(Calendar.MONTH);
            Calendar now = Calendar.getInstance();
            int nowy =now.get(Calendar.YEAR);

            new DatePickerDialog(StatusActivity.this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    if(nowy-year>5) {
                        date.setText(year + "/" + (month + 1) + "/" + day);
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "年齡太小", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }, year,month, day).show();

        }

    };

    private Button.OnClickListener s_btn = new Button.OnClickListener() {

        public void onClick(View v) {

            new Thread(new Runnable() {

                public void run() {
                    final EditText date = findViewById(R.id.date);
                    final RadioGroup radio= findViewById(R.id.genderGroup);
                    final EditText cityt = findViewById(R.id.city);
                    final EditText townt = findViewById(R.id.town);
                    CheckBox ch1 = findViewById(R.id.food1);
                    CheckBox ch2 = findViewById(R.id.food2);
                    CheckBox ch3 = findViewById(R.id.food3);
                    CheckBox ch4 = findViewById(R.id.food4);
                    CheckBox ch5 = findViewById(R.id.food5);
                    CheckBox ch6 = findViewById(R.id.food6);
                    String bday = date.getText().toString();
                    String city = cityt.getText().toString();
                    String town = townt.getText().toString();
                    //判斷有無勾選
                    int b1=0,b2=0,b3=0,b4=0,b5=0,b6=0;
                    if(ch1.isChecked()){ b1=1; }
                    if(ch2.isChecked()){ b2=1; }
                    if(ch3.isChecked()){ b3=1; }
                    if(ch4.isChecked()){ b4=1; }
                    if(ch5.isChecked()){ b5=1; }
                    if(ch6.isChecked()){ b6=1; }

                    Looper.prepare();
                    Toast toast;
                    if(bday.length()>7 && radio.getCheckedRadioButtonId()!=-1 && city.length()>2 && town.length()>2){
                        MysqlCon con = new MysqlCon();
                        Intent i =getIntent();
                        String email = i.getStringExtra("email");
                        final RadioButton r = findViewById(radio.getCheckedRadioButtonId());

                        con.insertInfo(email, bday, r.getText().toString(),city,town,b1,b2,b3,b4,b5,b6);

                        toast = Toast.makeText(getApplicationContext(), "註冊完成", Toast.LENGTH_SHORT);

                        Intent intent = new Intent();
                        intent.setClass(StatusActivity.this, MemberActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else{
                        toast = Toast.makeText(getApplicationContext(), "欄位不得有空", Toast.LENGTH_SHORT);
                    }
                    toast.show();
                    Looper.loop();
                }
            }).start();
        }

    };
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}