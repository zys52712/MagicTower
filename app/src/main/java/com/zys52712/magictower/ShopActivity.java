package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Scanner;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        char type = intent.getStringExtra(GameActivity.EXTRA_TYPE).charAt(0);

        TextView offer1 = findViewById(R.id.offer1);
        TextView offer2 = findViewById(R.id.offer2);
        TextView offer3 = findViewById(R.id.offer3);
        TextView offer4 = findViewById(R.id.offer4);
        TextView shopTitle = findViewById(R.id.shopView);
        TextView info = findViewById(R.id.infoView);

        String[] offer = new String[5];

        String shopHeader = "";

        switch (type) {
            case '$':
                int cost = 25;
                int hp = 800;
                int atkdmg = 4;

                if (GameActivity.currentLv == 11) {
                    cost = 100;
                    hp = 4000;
                    atkdmg = 20;
                }

                shopHeader = String.format("Welcome to the gold shop\n (%dG per upgrade, you have %dG)", cost, GameActivity.pGold);
                shopTitle.setText(shopHeader);
                //System.out.format("(1) %d health\n(2) %d attack\n(3) %d defense\n(4) exit shop\n", hp, atkdmg, atkdmg);
                offer[1] = String.format("+%d health", hp);
                offer[2] = String.format("+%d attack", atkdmg);
                offer[3] = String.format("+%d defense", atkdmg);
                offer[4] = String.format("exit shop");

                offer1.setText(offer[1]);
                offer2.setText(offer[2]);
                offer3.setText(offer[3]);
                offer4.setText(offer[4]);

                int finalCost = cost;
                int finalHp = hp;
                offer1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold >= finalCost) {
                            GameActivity.pGold -= finalCost;
                            GameActivity.pHealth += finalHp;
                            String infoMsg = String.format("Health increased to %d (%d+%d)", GameActivity.pHealth, GameActivity.pHealth - finalHp, finalHp);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the gold shop!\n (%dG per upgrade, you have %dG)", finalCost, GameActivity.pGold);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                int finalAtkdmg = atkdmg;
                offer2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold >= finalCost) {
                            GameActivity.pGold -= finalCost;
                            GameActivity.pAtk += finalAtkdmg;
                            String infoMsg = String.format("Attack increased to %d (%d+%d)", GameActivity.pAtk, GameActivity.pAtk - finalAtkdmg, finalAtkdmg);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the gold shop!\n (%dG per upgrade, you have %dG)", finalCost, GameActivity.pGold);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold >= finalCost) {
                            GameActivity.pGold -= finalCost;
                            GameActivity.pDef += finalAtkdmg;
                            String infoMsg = String.format("Defense increased to %d (%d+%d)", GameActivity.pDef, GameActivity.pDef - finalAtkdmg, finalAtkdmg);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the gold shop!\n (%dG per upgrade, you have %dG)", finalCost, GameActivity.pGold);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case '£':
                int lvCost = 100;
                int lv = 1;
                int cost2 = 30;
                int atkdmg2 = 5;

                if (GameActivity.currentLv == 13) {
                    lvCost = 270;
                    lv = 3;
                    cost2 = 95;
                    atkdmg2 = 20;
                }

                shopHeader = String.format("Welcome to the exp shop\n (prices vary, you have %dEXP)", GameActivity.pEXP);
                shopTitle.setText(shopHeader);
                //System.out.format("(1) %d health\n(2) %d attack\n(3) %d defense\n(4) exit shop\n", hp, atkdmg, atkdmg);
                offer[1] = String.format("level up x%d (%dEXP)", lv, lvCost);
                offer[2] = String.format("+%d attack  (%dEXP)", atkdmg2, cost2);
                offer[3] = String.format("+%d defense  (%dEXP)", atkdmg2, cost2);
                offer[4] = String.format("exit shop");

                offer1.setText(offer[1]);
                offer2.setText(offer[2]);
                offer3.setText(offer[3]);
                offer4.setText(offer[4]);

                int finalLvCost = lvCost;
                int finalLv = lv;
                int finalCost2 = cost2;
                int finalatkdmg2 = atkdmg2;
                offer1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pEXP >= finalLvCost) {
                            GameActivity.pEXP -= finalLvCost;
                            GameActivity.pHealth += 1000 * finalLv;
                            GameActivity.pAtk += 7 * finalLv;
                            GameActivity.pDef += 7 * finalLv;
                            GameActivity.pLv++;
                            String infoMsg = String.format("Leveled up! Stats increased!");
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the exp shop!\n (Prices vary, you have %dEXP)", GameActivity.pEXP);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pEXP >= finalCost2) {
                            GameActivity.pEXP -= finalCost2;
                            GameActivity.pAtk += finalatkdmg2;
                            String infoMsg = String.format("Attack increased to %d (%d+%d)", GameActivity.pAtk, GameActivity.pAtk - finalatkdmg2, finalatkdmg2);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the exp shop!\n (Prices vary, you have %dEXP)", GameActivity.pEXP);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough exp");
                        }
                    }
                });

                offer3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pEXP >= finalCost2) {
                            GameActivity.pEXP -= finalCost2;
                            GameActivity.pDef += finalatkdmg2;
                            String infoMsg = String.format("Defense increased to %d (%d+%d)", GameActivity.pDef, GameActivity.pDef - finalatkdmg2, finalatkdmg2);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the exp shop!\n (Prices vary, you have %dEXP)", GameActivity.pEXP);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough exp");
                        }
                    }
                });

                offer4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case '¥':
                shopHeader = String.format("Welcome to the keys shop!\n (Prices vary, you have %dG)", GameActivity.pGold);
                shopTitle.setText(shopHeader);
                offer[1] = String.format("░ key (10G)");
                offer[2] = String.format("▒ key (50G)");
                offer[3] = String.format("▓ key (100G)");
                offer[4] = String.format("exit shop");

                offer1.setText(offer[1]);
                offer2.setText(offer[2]);
                offer3.setText(offer[3]);
                offer4.setText(offer[4]);

                offer1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold >= 10) {
                            GameActivity.pGold -= 10;
                            GameActivity.pKeys[0]++;
                            String infoMsg = String.format("░ key increased to %d", GameActivity.pKeys[0]);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the keys shop!\n (Prices vary, you have %dG)", GameActivity.pGold);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold >= 50) {
                            GameActivity.pGold -= 50;
                            GameActivity.pKeys[1]++;
                            String infoMsg = String.format("▒ key increased to %d", GameActivity.pKeys[1]);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the keys shop!\n (Prices vary, you have %dG)", GameActivity.pGold);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold >= 100) {
                            GameActivity.pGold -= 100;
                            GameActivity.pKeys[2]++;
                            String infoMsg = String.format("▓ key increased to %d", GameActivity.pKeys[2]);
                            info.setText(infoMsg);
                            String shopHeader = String.format("Welcome to the keys Shop\n (Prices vary, you have %dG)", GameActivity.pGold);
                            shopTitle.setText(shopHeader);
                        } else {
                            info.setText("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
        }
    }
}