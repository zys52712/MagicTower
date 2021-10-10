package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TeleportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teleport);
    }

    public void jump(View view){
        EditText floorNum = findViewById(R.id.floorBox);
        int floor = Integer.parseInt(floorNum.getText().toString());
        GameActivity.useTeleporter(floor);
        finish();
    }
}