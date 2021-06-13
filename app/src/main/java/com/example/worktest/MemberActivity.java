package com.example.worktest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        setTitle("個人設定");
    }

}