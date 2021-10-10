package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }

    public void enter(View view){
        EditText commandBox = findViewById(R.id.debugEntry);
        String[] commands = commandBox.getText().toString().split(" ");
        String msg = "";
        int num = 0;
        switch (commands[0]) {
            case "t":
                Intent intent = new Intent(this, TotemActivity.class);
                startActivity(intent);
                break;
            case "tp":
                GameActivity.useTeleporter(Integer.parseInt(0 + commands[1].replaceAll("\\D+","")));
                msg = String.format("Teleported to floor %d", Integer.parseInt(0 + commands[1].replaceAll("\\D+","")));
                break;
            case "s":
                num = Integer.parseInt(0 + commands[2].replaceAll("\\D+",""));
                if (num > 0) {
                    switch (commands[1]) {
                        case "hp":
                            GameActivity.pHealth += num;
                            msg = String.format("Health increased to %d (%d+%d)", GameActivity.pHealth, GameActivity.pHealth - num, num);
                            break;
                        case "atk":
                            GameActivity.pAtk += num;
                            msg = String.format("Attack increased to %d (%d+%d)", GameActivity.pAtk, GameActivity.pAtk - num, num);
                            break;
                        case "def":
                            GameActivity.pDef += num;
                            msg = String.format("Defense increased to %d (%d+%d)", GameActivity.pDef, GameActivity.pDef - num, num);
                            break;
                        case "gold":
                            GameActivity.pGold += num;
                            msg = String.format("Gold increased to %d (%d+%d)", GameActivity.pGold, GameActivity.pGold - num, num);
                            break;
                        case "exp":
                            GameActivity.pEXP += num;
                            msg = String.format("EXP increased to %d (%d+%d)", GameActivity.pEXP, GameActivity.pEXP - num, num);
                            break;
                        case "key":
                            GameActivity.pKeys[0] += num;
                            GameActivity.pKeys[1] += num;
                            GameActivity.pKeys[2] += num;
                            msg = String.format("Each key increased by %d", num);
                            break;
                    }
                }
                else{
                    msg = "unknown command, please enter another one";
                }
                break;
            default:
                msg = "unknown command, please enter another one";
                break;
        }
        GameActivity.viewWeakReference.get().setText(msg);
        finish();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}