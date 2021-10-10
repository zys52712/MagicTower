package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TotemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totem);

        useTotem();
    }

    public void useTotem() {
        TextView title = findViewById(R.id.totemBody);
        //TextView body = findViewById(R.id.totemBody);
        TextView mobName = findViewById(R.id.mobNames);
        TextView mobStats = findViewById(R.id.mobStats);

        title.setMovementMethod(new ScrollingMovementMethod());
        title.setHorizontallyScrolling(true);
        mobStats.setMovementMethod(new ScrollingMovementMethod());
        mobStats.setHorizontallyScrolling(true);

        title.setText("");
        //body.setText("");
        mobStats.setText("");

        String totemTitle = String.format("%-2s%-3s%-20s%-6s%-5s%-5s%-7s%-10s", " ", " ", "Mob Name", "HP", "ATK",
                "DEF", "  G/E  ", "Est. Dmg");
        String totemMobNames = String.format("%-3s%-20s", " ", "Mob Name------------");
        String totemMobStats = String.format("%-6s%-5s%-5s%-7s%-10s", "HP", "ATK",
                "DEF", "GD/EXP", "Est. Dmg");
        title.setText(totemTitle);
        title.append("\n\n");
        mobName.setText(totemMobNames);
        mobName.append("\n\n");
        mobStats.setText(totemMobStats);
        mobStats.append("\n\n");

        for (int i = 0; i < GameActivity.mobLetter.length; i++) {
            if (GameActivity.checkFieldForChar(GameActivity.mobLetter[i])) {
                totemPrinter(GameActivity.mobLetter[i]);
            }
        }
    }

    public void totemPrinter(char mob) {
        TextView body = findViewById(R.id.totemBody);
        TextView mobStats = findViewById(R.id.mobStats);
        TextView mobNameView = findViewById(R.id.mobNames);

        String totemNames = "";
        String totemStats = "";
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
        String goldEXP = String.format("%d/%d", gold, exp);

        totemNames = String.format("%-3s%-20s\n", boardName, mobName);
        if (pAtk - def > 0) {
            int mobDmg = negativeToZero(health / (pAtk - def) * (atk - pDef));
            if (mob == 'K') {
                mobDmg += GameActivity.pHealth / 4;
            }
            line = String.format("%-2s%-3s%-20s%-6d%-5d%-5d%3d/%-3d%-10d\n", mob, boardName,
                    mobName, health, atk, def, gold, exp, mobDmg);
            totemStats = String.format("%-6d%-5d%-5d%-7s%-10d\n", health, atk, def, goldEXP, mobDmg);

        } else {
            line = String.format("%-2s%-3s%-20s%-6d%-5d%-5d%3d/%-3d%-10s\n", mob, boardName,
                    mobName, health, atk, def, gold, exp, "Unbeatable");
            totemStats = String.format("%-6d%-5d%-5d%-7s%-10s\n", health, atk, def, goldEXP, "Death");

        }
        body.append(line);
        mobNameView.append(totemNames);
        mobStats.append(totemStats);
    }

    public static int negativeToZero(int num) {
        return Math.max(0, num);
    }
}