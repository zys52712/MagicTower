package com.zys52712.magictower;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FightActivity extends AppCompatActivity {
    String mobName = "";
    int mobHealth = 0;
    int mobAtk = 0;
    int mobDef = 0;
    int mobGold = 0;
    int mobEXP = 0;

    int pHealth = 0;
    int pAtk = 0;
    int pDef = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);
        Button fight = findViewById(R.id.fight);
        Intent intent = getIntent();
        char mob = intent.getStringExtra(GameActivity.EXTRA_MID).charAt(0);
        TextView pStats = findViewById(R.id.pStats);
        TextView mStats = findViewById(R.id.mStats);
        TextView title = findViewById(R.id.fightTitle);
        TextView mobID = findViewById(R.id.MobID);
        fight.performClick();
        fight.setEnabled(false);

        mobName = GameActivity.charToMobName(mob);
        mobHealth = GameActivity.mobStats(mob, 0);
        mobAtk = GameActivity.mobStats(mob, 1);
        mobDef = GameActivity.mobStats(mob, 2);
        mobGold = GameActivity.mobStats(mob, 3);
        mobEXP = GameActivity.mobStats(mob, 4);

        title.append(mobName);
        mobID.setText(mobName);

        pHealth = GameActivity.pHealth;
        pAtk = GameActivity.pAtk;
        pDef = GameActivity.pDef;

        String pStat = pHealth + "\n\n" + pAtk + "\n\n" + pDef + "\n\n" + negativeToZero(pAtk - mobDef);
        String mStat = mobHealth + "\n\n" + mobAtk + "\n\n" + mobDef + "\n\n" + negativeToZero(mobAtk - pDef);
        updateTextView(pStat,mStat);
    }

    public void onClick(View v) {
        new Thread(new Runnable() {
            public void run() {
                Intent intent = getIntent();
                char mob = intent.getStringExtra(GameActivity.EXTRA_MID).charAt(0);
                TextView pStats = findViewById(R.id.pStats);
                TextView mStats = findViewById(R.id.mStats);
                setFinishOnTouchOutside(false);
                System.out.format("%-10s%-10s%-20s\n", " ", "Player", mobName);
                System.out.format("%-10s%-10d%-20d\n", "Health", pHealth, mobHealth);
                System.out.format("%-10s%-10d%-20d\n", "Attack", pAtk, mobAtk);
                System.out.format("%-10s%-10d%-20d\n\n", "Defense", pDef, mobDef);

                if (mob == 'K') {
                    pHealth -= pHealth / 4;
                }

                long current = currentTimeMillis();

                while (mobHealth > 0) {
                    String pStat = pHealth + "\n\n" + pAtk + "\n\n" + pDef + "\n\n" + negativeToZero(pAtk - mobDef);
                    String mStat = mobHealth + "\n\n" + mobAtk + "\n\n" + mobDef + "\n\n" + negativeToZero(mobAtk - pDef);
                    updateTextView(pStat, mStat);

                    mobHealth -= negativeToZero(pAtk - mobDef);
                    pHealth -= negativeToZero(mobAtk - pDef);
                    negativeToZero(mobHealth);
                    negativeToZero(pHealth);
                    System.out.println(currentTimeMillis());

                    if (pHealth == 0) {
                        System.out.println("\nYou have been killed, thanks for playing");
                        System.exit(0);
                    }

                    if (mobHealth == 0) {
                        pHealth += negativeToZero(mobAtk - pDef);
                    }

                    System.out.format("%-10s%-10d%-10d", "Health", pHealth, mobHealth);
                    if (pAtk <= mobDef) {
                        System.out.println("Player atk below enemy def, did not do damage to enemy");
                    } else if (mobAtk <= pDef) {
                        System.out.println("Enemy atk below player def, did not do damage to player");
                    } else {
                        System.out.print("\n");
                    }
                    current = currentTimeMillis();
                    pause(0.5);
                }

                String endText = String.format("You defeated %s, %d gold and %d exp gained", mobName, mobGold, mobEXP);

                GameActivity.pHealth = pHealth;
                GameActivity.pAtk = pAtk;
                GameActivity.pDef = pDef;
                GameActivity.pGold += mobGold;
                GameActivity.pEXP += mobEXP;

                finish();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(FightActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }

    private void updateTextView(final String s1, final String s2) {
        FightActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView pStats = findViewById(R.id.pStats);
                TextView mStats = findViewById(R.id.mStats);
                pStats.setText(s1);
                mStats.setText(s2);
            }
        });

    }

    public static int negativeToZero(int num) {
        return Math.max(0, num);
    }

    public static void pause(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
        }
    }
}