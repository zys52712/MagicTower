package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        String[] offer = new String[5];

        String selection = "7";
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

                System.out.format("Welcome to the shop, what would you like to get? (%d Gold each)\n", cost);
                System.out.format("(1) %d health\n(2) %d attack\n(3) %d defense\n(4) exit shop\n", hp, atkdmg, atkdmg);
                offer[1] = String.format("(1) %d health", hp);
                offer[2] = String.format("(2) %d attack", atkdmg);
                offer[3] = String.format("(3) %d defense", atkdmg);
                offer[4] = String.format("(4) exit shop");

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
                            System.out.format("Health increased by %d\n", finalHp);
                        } else {
                            System.out.println("Sorry, you don't have enough gold");
                        }
                    }
                });

                int finalAtkdmg = atkdmg;
                offer2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold > finalCost) {
                            GameActivity.pGold -= finalCost;
                            GameActivity.pAtk += finalAtkdmg;
                            System.out.format("Attack increased by %d\n", finalAtkdmg);
                        } else {
                            System.out.println("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (GameActivity.pGold > finalCost) {
                            GameActivity.pGold -= finalCost;
                            GameActivity.pDef += finalAtkdmg;
                            System.out.format("Defense increased by %d\n", finalAtkdmg);
                        } else {
                            System.out.println("Sorry, you don't have enough gold");
                        }
                    }
                });

                offer4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                });

                //selection = 1;
                if (selection.equals("1")) {
                    if (GameActivity.pGold >= cost) {
                        GameActivity.pGold -= cost;
                        GameActivity.pHealth += hp;
                        System.out.format("Health increased by %d\n", hp);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("2")) {
                    if (GameActivity.pGold > cost) {
                        GameActivity.pGold -= cost;
                        GameActivity.pAtk += atkdmg;
                        System.out.format("Attack increased by %d\n", atkdmg);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("3")) {
                    if (GameActivity.pGold > cost) {
                        GameActivity.pGold -= cost;
                        GameActivity.pDef += atkdmg;
                        System.out.format("Defense increased by %d\n", atkdmg);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else {
                    System.out.println("unknown input, please choose again");
                }
                break;
        }
    }


    public void shop(char type){
        TextView offer1 = findViewById(R.id.offer1);
        TextView offer2 = findViewById(R.id.offer2);
        TextView offer3 = findViewById(R.id.offer3);
        TextView offer4 = findViewById(R.id.offer4);

        String[] offer = new String[5];

        String selection = "1";
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
                System.out.format("Welcome to the shop, what would you like to get? (%d Gold each)\n", cost);
                System.out.format("(1) %d health\n(2) %d attack\n(3) %d defense\n(4) exit shop\n", hp, atkdmg, atkdmg);
                offer[1] = String.format("(1) %d health", hp);
                offer[2] = String.format("(2) %d attack", hp);
                offer[3] = String.format("(3) %d defense", hp);
                offer[4] = String.format("(4) exit shop", hp);

                offer1.setText(offer[1]);
                offer2.setText(offer[2]);
                offer3.setText(offer[3]);
                offer4.setText(offer[4]);

                //selection = 1;
                if (selection.equals("1")) {
                    if (GameActivity.pGold >= cost) {
                        GameActivity.pGold -= cost;
                        GameActivity.pHealth += hp;
                        System.out.format("Health increased by %d\n", hp);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("2")) {
                    if (GameActivity.pGold > cost) {
                        GameActivity.pGold -= cost;
                        GameActivity.pAtk += atkdmg;
                        System.out.format("Attack increased by %d\n", atkdmg);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("3")) {
                    if (GameActivity.pGold > cost) {
                        GameActivity.pGold -= cost;
                        GameActivity.pDef += atkdmg;
                        System.out.format("Defense increased by %d\n", atkdmg);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else {
                    System.out.println("unknown input, please choose again");
                }
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

                System.out.println("Welcome to the EXP shop, what would you like to get?");
                System.out.format(
                        "(1) level up (x%d) - %dEXP\n(2) %d attack - %dEXP\n(3) %d defense - %dEXP\n(4) exit shop\n", lv,
                        lvCost, cost2, atkdmg2, cost2, atkdmg2);
                //selection = 1;
                if (selection.equals("1")) {
                    if (GameActivity.pEXP >= lvCost) {
                        GameActivity.pEXP -= lvCost;
                        GameActivity.pHealth += 1000 * lv;
                        GameActivity.pAtk += 7 * lv;
                        GameActivity.pDef += 7 * lv;
                        GameActivity.pLv++;
                        System.out.println("Leveled up! Stats increased!");
                    } else {
                        System.out.println("Sorry, you don't have enough EXP");
                    }
                } else if (selection.equals("2")) {
                    if (GameActivity.pEXP >= cost2) {
                        GameActivity.pEXP -= cost2;
                        GameActivity.pAtk += atkdmg2;
                        System.out.format("Attack increased by %d\n", atkdmg2);
                    } else {
                        System.out.println("Sorry, you don't have enough EXP");
                    }
                } else if (selection.equals("3")) {
                    if (GameActivity.pEXP >= cost2) {
                        GameActivity.pEXP -= cost2;
                        GameActivity.pDef += atkdmg2;
                        System.out.format("Defense increased by %d\n", atkdmg2);
                    } else {
                        System.out.println("Sorry, you don't have enough EXP");
                    }
                } else {
                    System.out.println("unknown input, please choose again");
                }
                break;
            case '¥':
                System.out.println("Welcome to the key shop, what would you like to get?");
                System.out.format(
                        "(1) Yellow Key - 10 Gold\n(2) Blue Key - 50 Gold\n(3) Red Key - 100 Gold\n(4) exit shop\n");
                //selection = 1;
                if (selection.equals("1")) {
                    if (GameActivity.pGold > 9) {
                        GameActivity.pGold -= 10;
                        GameActivity.pKeys[0]++;
                        System.out.println("You bought a yellow key");
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("2")) {
                    if (GameActivity.pGold > 49) {
                        GameActivity.pGold -= 50;
                        GameActivity.pKeys[1]++;
                        System.out.println("You bought a blue key");
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("3")) {
                    if (GameActivity.pGold > 99) {
                        GameActivity.pGold -= 100;
                        GameActivity.pKeys[2]++;
                        System.out.println("You bought a red key");
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else {
                    System.out.println("unknown input, please choose again");
                }
                break;
        }
    }
}