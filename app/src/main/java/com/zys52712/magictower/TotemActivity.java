package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TotemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totem);

        useTotem();
    }

    public void useTotem() {
        TextView title = findViewById(R.id.totemTitle);
        TextView body = findViewById(R.id.totemBody);

        title.setText("");
        body.setText("");

        String totemTitle = String.format("%-2s%-3s%-20s%-6s%-5s%-5s%-6s%-6s%-10s\n", " ", " ", "Mob Name", "HP", "ATK",
                "DEF", "Gold", "EXP", "Est. Dmg");
        title.setText(totemTitle);

        for (int i = 0; i < GameActivity.mobLetter.length; i++) {
            if (GameActivity.checkFieldForChar(GameActivity.mobLetter[i])) {
                totemPrinter(GameActivity.mobLetter[i]);
            }
        }
    }

    public void totemPrinter(char mob) {
        TextView body = findViewById(R.id.totemBody);
        String line = "";
        // 0 health, 1 atk, 2 def, 3 gold, 4 exp
        String mobName = GameActivity.charToMobName(mob);
        String boardName = GameActivity.letterToBoard(mob);

        int pAtk = GameActivity.pAtk;
        int pDef = GameActivity.pDef;
        int health = GameActivity.mobStats(mob, 0);
        int atk = GameActivity.mobStats(mob, 1);
        int def = GameActivity.mobStats(mob, 2);
        int gold = GameActivity.mobStats(mob, 3);
        int exp = GameActivity.mobStats(mob, 4);

        if (pAtk - def > 0) {
            int mobDmg = negativeToZero(health / (pAtk - def) * (atk - pDef));
            if (mob == 'K') {
                mobDmg += GameActivity.pHealth / 4;
            }
            line = String.format("%-2s%-3s%-20s%-6d%-5d%-5d%-6d%-6d%-10d\n", mob, boardName,
                    mobName, health, atk, def, gold, exp, mobDmg);

        } else {
            line = String.format("%-2s%-3s%-20s%-6d%-5d%-5d%-6d%-6d%-10s\n", mob, boardName,
                    mobName, health, atk, def, gold, exp, "Unbeatable");
        }
        body.append(line + "\n");
    }

    public static int negativeToZero(int num) {
        return Math.max(0, num);
    }
}