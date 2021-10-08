package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_PHP = "";
    public static final String EXTRA_PATK = "";
    public static final String EXTRA_PDEF = "";
    public static final String EXTRA_MID = "";
    public static final String EXTRA_MHP = "";
    public static final String EXTRA_MATK = "";
    public static final String EXTRA_MDEF = "";

    static char[][][] levels = new char[22][13][14];
    static String tempLine = new String();
    static int oldPx = 0;
    static int oldPy = 0;
    static int currentLv = 0;
    static int pX = 6;
    static int pY = 11;
    static int pHealth = 1000;
    static int pAtk = 10;
    static int pDef = 10;
    static int pGold = 0;
    static int pEXP = 0;
    static int pLv = 1;
    static boolean hasTotem = true;
    static boolean hasTeleport = true;
    static int[] pKeys = { 2, 1, 1 };
    static int levelCount = 16;
    // 0 health, 1 atk, 2 def, 3 gold, 4 exp
    // green slime, red ball, skeleton, witch, skeleton soldier, bat, black slime,
    // monsterface, large bat
    static int[][] mobStats = { { 50, 20, 1, 1, 1 }, { 70, 15, 2, 2, 2, }, { 110, 25, 5, 5, 4 }, { 125, 50, 25, 10, 7 },
            { 150, 40, 20, 8, 6 }, { 100, 20, 5, 3, 3 }, { 200, 35, 10, 5, 5 }, { 300, 75, 45, 13, 10, },
            { 150, 65, 30, 10, 8 }, { 450, 150, 90, 22, 19 }, { 550, 160, 90, 25, 20 }, { 100, 200, 110, 30, 25 },
            { 400, 90, 50, 15, 12 }, { 1300, 300, 150, 40, 35 }, { 250, 120, 70, 20, 17 }, { 500, 400, 260, 47, 45 },
            { 500, 115, 65, 15, 15 }, { 700, 250, 125, 32, 30 }, { 900, 450, 330, 50, 50 }, { 1250, 500, 400, 55, 55 },
            { 1500, 560, 460, 60, 60 }, { 1200, 620, 520, 65, 75 }, { 850, 350, 200, 45, 40 },
            { 900, 750, 650, 77, 70 }, { 1200, 980, 900, 88, 75 }, { 1500, 830, 730, 80, 70 },
            { 2000, 680, 590, 70, 65 }, { 2500, 900, 850, 84, 75 }, { 15000, 1000, 1000, 100, 100 } };
    static String[] mobName = { "green slime", "red ball", "skeleton", "sorcerer", "skeleton soldier", "bat",
            "black slime", "monsterface", "large bat", "basic robot", "red bat", "red coat sorcerer", "skeleton master",
            "white knight", "hemp witch", "red coat witch", "bolder monster", "king of weirdness",
            "monsterface soldier", "blue robot", "advanced robot", "dual wielder", "gold soldier", "gold master",
            "spirit warrior", "spirit sorcerer", "blue soldier", "blue skeleton", "red boss" };
    static char[] mobLetter = { 'G', 'r', 's', 'w', 'S', 'b', 'q', 'm', 'B', 'n', 'v', 'W', 'M', 'K', 'h', 'H', 'y',
            'g', 'f', 'j', 'J', 'd', 'c', 'C', 'x', 'X', 'a', 'A', 'z' };
    // 1 red bottle, 2 blue bottle, 3 blue gem, 4 red gem, 5 yellow key, 6 blue key,
    // 7 red key
    // up and down based on the level e.g. {2(y),11(x)} is level 2 going up
    static int[][] upDefaultPos = { { 6, 10 }, { 1, 2 }, { 2, 11 }, { 11, 10 }, { 2, 11 }, // lv0 to lv4
            { 9, 11 }, { 6, 11 }, { 1, 2 }, { 7, 4 }, { 5, 7 }, // lv5 to lv9
            { 2, 11 }, { 10, 11 }, { 2, 11 }, { 6, 10 }, { 4, 1 }, // lv10 to lv14
            { 6, 1 } }; // lv15 to lv19
    static int[][] downDefaultPos = { { 0, 0 }, { 6, 1 }, { 2, 1 }, { 1, 10 }, { 11, 10 }, // lv0 to lv4
            { 1, 10 }, { 10, 10 }, { 6, 11 }, { 2, 1 }, { 8, 5 }, // lv5 to lv9
            { 7, 4 }, { 1, 10 }, { 10, 11 }, { 2, 11 }, { 5, 11 }, // lv10 to lv14
            { 6, 1 }, { 8, 1 } }; // lv15 to lv19
    static Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Context context = getApplicationContext();
        TextView gameWindow = findViewById(R.id.gameWindow);
        //gameWindow.setMovementMethod(new ScrollingMovementMethod());
        gameWindow.setSingleLine(false);
        InputStream levelTxt = null;

        ImageButton up = findViewById(R.id.button_up);
        ImageButton down = findViewById(R.id.button_down);
        ImageButton left = findViewById(R.id.button_left);
        ImageButton right = findViewById(R.id.button_right);

        AssetManager manager = context.getAssets();
        try {
            levelTxt = manager.open("levels.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader levelRead = new BufferedReader(new InputStreamReader(levelTxt));

        // control how many levels to scan
        for (int l = 0; l < levelCount + 1; l++) {
            for (int i = 0; i < 13; i++) {
                try {
                    tempLine = levelRead.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 13; j++) {
                    levels[l][i][j] = tempLine.charAt(j);
                }
                levels[l][i][13] = '\n';
            }
        }

        TextView hp = findViewById(R.id.health);
        TextView atkdef = findViewById(R.id.atkdef);
        TextView gexp = findViewById(R.id.gexp);

        hp.setText(Integer.toString(pHealth));
        atkdef.setText(pAtk + " / " + pDef);
        gexp.setText(pGold + " / " + pEXP);

        up.setOnClickListener(this::moveOnClick);
        down.setOnClickListener(this::moveOnClick);
        left.setOnClickListener(this::moveOnClick);
        right.setOnClickListener(this::moveOnClick);
        //gameWindow.setText(levels[1][5].toString());

        levels[currentLv][pY][pX] = 'P';
        printField();

    }

    public void moveOnClick(View v) {
        // do something when the button is clicked
        // Yes we will handle click here but which button clicked??? We don't know

        // So we will make
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.button_up:
                movePC('w');
                break;
            case R.id.button_down:
                movePC('s');
                break;
            case R.id.button_left:
                movePC('a');
                break;
            case R.id.button_right:
                movePC('d');
                break;
            default:
                break;
        }
        levels[currentLv][pY][pX] = 'P';
        printField();
        updateStats();
    }

    public void updateStats(){

        TextView hp = findViewById(R.id.health);
        TextView atkdef = findViewById(R.id.atkdef);
        TextView gexp = findViewById(R.id.gexp);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hp.setText(Integer.toString(pHealth));
                atkdef.setText(pAtk + " / " + pDef);
                gexp.setText(pGold + " / " + pEXP);
            }
        });
    }


    public void fight(char mob) {
        Intent intent = new Intent(this, FightActivity.class);
        String mobS = String.valueOf(mob);
        intent.putExtra(EXTRA_MID, mobS);
        startActivity(intent);

        /*
        String mobName = charToMobName(mob);
        int mobHealth = mobStats(mob, 0);
        int mobAtk = mobStats(mob, 1);
        int mobDef = mobStats(mob, 2);
        int mobGold = mobStats(mob, 3);
        int mobEXP = mobStats(mob, 4);



        EXTRA_MID = "";
        intent.putExtra(EXTRA_PHP, pHealth);
        intent.putExtra(EXTRA_PATK = "";
        intent.putExtra(EXTRA_PDEF = "";
        intent.putExtra(EXTRA_MHP = "";
        intent.putExtra(EXTRA_MATK = "";
        intent.putExtra(EXTRA_MDEF = "";


        System.out.format("%-10s%-10s%-20s\n", " ", "Player", mobName);
        System.out.format("%-10s%-10d%-20d\n", "Health", pHealth, mobHealth);
        System.out.format("%-10s%-10d%-20d\n", "Attack", pAtk, mobAtk);
        System.out.format("%-10s%-10d%-20d\n\n", "Defense", pDef, mobDef);
        if (mob == 'K') {
            pHealth -= pHealth / 4;
        }

        while (mobHealth > 0) {
            mobHealth -= negativeToZero(pAtk - mobDef);

            pHealth -= negativeToZero(mobAtk - pDef);
            negativeToZero(pHealth);

            if (pHealth == 0) {
                System.out.println("\nYou have been killed, thanks for playing");
                System.exit(0);
            }

            if (mobHealth <= 0) {
                mobHealth = 0;
                pHealth += (mobAtk - pDef);
            }

            updateStats();

            System.out.format("%-10s%-10d%-10d", "Health", pHealth, mobHealth);
            if (pAtk <= mobDef) {
                System.out.println("Player atk below enemy def, did not do damage to enemy");
            } else if (mobAtk <= pDef) {
                System.out.println("Enemy atk below player def, did not do damage to player");
            } else {
                System.out.print("\n");
            }

            pause(0.5);
        }

        pGold += mobGold;
        pEXP += mobEXP;
        System.out.format("\n\nYou defeated %s, %d gold and %d exp gained\n", mobName, mobGold, mobEXP);
        */
    }

    public void game() {
        Scanner sc = new Scanner(System.in);
        String[] commands = new String[5];

        // System.out.println("game started, please enter command");

        while (true) {
            System.out.println("");
            levels[currentLv][pY][pX] = 'P';
            printField();
            System.out.println("\nplease enter command");
            commands = sc.nextLine().split(" ");
            switch (commands[0]) {
                case "w":
                case "a":
                case "s":
                case "d":
                    movePC(commands[0].charAt(0));
                    break;
                case "t":
                    if (hasTotem) {
                        useTotem();
                    } else {
                        System.out.println("You do not have the Totem");
                    }
                    break;
                case "tp":
                    if (hasTeleport) {
                        useTeleporter(Integer.parseInt(commands[1]));
                    } else {
                        System.out.println("You do not have the Teleporter");
                    }
                    break;
                case "debug":
                    switch (commands[1]) {
                        case "hp":
                            pHealth += Integer.parseInt(commands[2]);
                            break;
                        case "atk":
                            pAtk += Integer.parseInt(commands[2]);
                            break;
                        case "def":
                            pDef += Integer.parseInt(commands[2]);
                            break;
                        case "gold":
                            pGold += Integer.parseInt(commands[2]);
                            break;
                        case "exp":
                            pEXP += Integer.parseInt(commands[2]);
                            break;
                        case "key":
                            pKeys[0] += Integer.parseInt(commands[2]);
                            pKeys[1] += Integer.parseInt(commands[2]);
                            pKeys[2] += Integer.parseInt(commands[2]);
                            break;
                    }
                    break;
                default:
                    System.out.println("unknown command, please enter another one");
                    break;
            }
        }
    }

    public void movePC(char direction) {
        oldPx = pX;
        oldPy = pY;

        switch (direction) {
            case 'w':
                pY--;
                break;
            case 'a':
                pX--;
                break;
            case 's':
                pY++;
                break;
            case 'd':
                pX++;
                break;
        }
        // block dependent
        switch (levels[currentLv][pY][pX]) {
            case '█':
            case '*':
            case '~':
                System.out.print("You have hit a wall");
                pX = oldPx;
                pY = oldPy;
                break;
            case 'G':
            case 'r':
            case 's':
            case 'w':
            case 'S':
            case 'b':
            case 'q':
            case 'm':
            case 'B':
            case 'n':
            case 'v':
            case 'W':
            case 'M':
            case 'K':
            case 'h':
            case 'H':
            case 'y':
            case 'g':
            case 'f':
            case 'j':
            case 'J':
            case 'd':
            case 'c':
            case 'C':
            case 'x':
            case 'X':
            case 'a':
            case 'A':
            case 'z':
                System.out.format("Fight initiated with *%s*\n\n", charToMobName(levels[currentLv][pY][pX]));
                fight(levels[currentLv][pY][pX]);
                replaceAndReturn();
                break;
            case 'T':
                switch (currentLv) {
                    case 1:
                        hasTotem = true;
                        System.out.format(
                                "You have gained the totem! Allows you to see stats for the mobs on the board, \nas well as dammage taken if fought (type 't' to use)\n");
                        break;
                    case 9:
                        hasTeleport = true;
                        System.out.format(
                                "You have gained the teleporter! Allows you to jump between levels. (type 'tp #' to use)\n");
                        break;
                }
                replaceAndReturn();
                break;
            case 'û':
                switch (currentLv) {
                    case 6:
                        System.out.print("You leveled up!");
                        pHealth += 1000;
                        pAtk += 7;
                        pDef += 7;
                        pLv++;
                        break;
                    case 13:
                        System.out.print("You leveled up (x3)!");
                        pHealth += 3000;
                        pAtk += 21;
                        pDef += 21;
                        pLv += 3;
                        break;
                }
                replaceAndReturn();
                break;
            case '$':
            case '£':
            case '¥':
                shop(levels[currentLv][pY][pX]);
                pX = oldPx;
                pY = oldPy;
                break;
            case '1':
                System.out.print("You have gained a bottle, health increased by 200!");
                pHealth += 200;
                replaceAndReturn();
                break;
            case '2':
                System.out.print("You have gained a bottle, health increased by 500!");
                pHealth += 500;
                replaceAndReturn();
                break;
            case '3':
                System.out.print("You have gained blue gem, defense increased by 3!");
                pDef += 3;
                replaceAndReturn();
                break;
            case '4':
                System.out.print("You have gained red gem, attack increased by 3!");
                pAtk += 3;
                replaceAndReturn();
                break;
            case '5':
                System.out.print("You gained a yellow key");
                pKeys[0]++;
                replaceAndReturn();
                break;
            case '6':
                System.out.print("You gained a blue key");
                pKeys[1]++;
                replaceAndReturn();
                break;
            case '7':
                System.out.print("You gained a red key");
                pKeys[2]++;
                replaceAndReturn();
                break;
            case 'þ':
                System.out.print("You gained a key of each type!");
                pKeys[0]++;
                pKeys[1]++;
                pKeys[2]++;
                replaceAndReturn();
                break;
            case 'â':
                switch (currentLv) {
                    case 2:
                    case 3:
                        System.out.print("You gained a sword, attack increased by 10!");
                        pAtk += 10;
                        break;
                    case 9:
                        System.out.print("You gained a greatsword, attack increased by 70!");
                        pAtk += 70;
                        break;
                }
                replaceAndReturn();
                break;
            case 'ä':
                switch (currentLv) {
                    case 2:
                    case 5:
                        System.out.print("You gained a shield, defense increased by 10!");
                        pDef += 10;
                        break;
                    case 11:
                        System.out.print("You gained a shield, defense increased by 70!");
                        pDef += 70;
                        break;
                }
                replaceAndReturn();
                break;
            case '+':
                levels[currentLv][oldPy][oldPx] = ' ';
                pX = upDefaultPos[currentLv][0];
                pY = upDefaultPos[currentLv][1];
                currentLv++;
                break;
            case '-':
                levels[currentLv][oldPy][oldPx] = ' ';
                pX = downDefaultPos[currentLv][0];
                pY = downDefaultPos[currentLv][1];
                currentLv--;
                break;
            case '\n':
                System.out.print("\n");
                break;
            case ' ':
                // System.out.format("move successful");
                levels[currentLv][oldPy][oldPx] = ' ';
                break;
            case '░':
                if (pKeys[0] > 0) {
                    System.out.format("You used yellow key on door");
                    pKeys[0]--;
                    levels[currentLv][pY][pX] = ' ';
                } else {
                    System.out.format("You don't have enough yellow keys left");
                }
                pX = oldPx;
                pY = oldPy;
                break;
            case '▒':
                if (pKeys[1] > 0) {
                    System.out.format("You used blue key on door");
                    pKeys[1]--;
                    levels[currentLv][pY][pX] = ' ';
                } else {
                    System.out.format("You don't have enough blue keys left");
                }
                pX = oldPx;
                pY = oldPy;
                break;
            case '▓':
                if (pKeys[2] > 0) {
                    System.out.format("You used red key on door");
                    pKeys[2]--;
                    levels[currentLv][pY][pX] = ' ';
                } else {
                    System.out.format("You don't have enough red keys left");
                }
                pX = oldPx;
                pY = oldPy;
                break;
            default:
                break;
        }
    }

    public static void replaceAndReturn() {
        levels[currentLv][pY][pX] = ' ';
        pX = oldPx;
        pY = oldPy;
    }

    public static void shop(char type) {
        Scanner sc = new Scanner(System.in);
        String selection = new String();
        switch (type) {
            case '$':
                int cost = 25;
                int hp = 800;
                int atkdmg = 4;

                if (currentLv == 11) {
                    cost = 100;
                    hp = 4000;
                    atkdmg = 20;
                }
                System.out.format("Welcome to the shop, what would you like to get? (%d Gold each)\n", cost);
                System.out.format("(1) %d health\n(2) %d attack\n(3) %d defense\n(4) exit shop\n", hp, atkdmg, atkdmg);
                selection = sc.next();
                if (selection.equals("1")) {
                    if (pGold >= cost) {
                        pGold -= cost;
                        pHealth += hp;
                        System.out.format("Health increased by %d\n", hp);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("2")) {
                    if (pGold > cost) {
                        pGold -= cost;
                        pAtk += atkdmg;
                        System.out.format("Attack increased by %d\n", atkdmg);
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("3")) {
                    if (pGold > cost) {
                        pGold -= cost;
                        pDef += atkdmg;
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

                if (currentLv == 13) {
                    lvCost = 270;
                    lv = 3;
                    cost2 = 95;
                    atkdmg2 = 20;
                }

                System.out.println("Welcome to the EXP shop, what would you like to get?");
                System.out.format(
                        "(1) level up (x%d) - %dEXP\n(2) %d attack - %dEXP\n(3) %d defense - %dEXP\n(4) exit shop\n", lv,
                        lvCost, cost2, atkdmg2, cost2, atkdmg2);
                selection = sc.next();
                if (selection.equals("1")) {
                    if (pEXP >= lvCost) {
                        pEXP -= lvCost;
                        pHealth += 1000 * lv;
                        pAtk += 7 * lv;
                        pDef += 7 * lv;
                        pLv++;
                        System.out.println("Leveled up! Stats increased!");
                    } else {
                        System.out.println("Sorry, you don't have enough EXP");
                    }
                } else if (selection.equals("2")) {
                    if (pEXP >= cost2) {
                        pEXP -= cost2;
                        pAtk += atkdmg2;
                        System.out.format("Attack increased by %d\n", atkdmg2);
                    } else {
                        System.out.println("Sorry, you don't have enough EXP");
                    }
                } else if (selection.equals("3")) {
                    if (pEXP >= cost2) {
                        pEXP -= cost2;
                        pDef += atkdmg2;
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
                selection = sc.next();
                if (selection.equals("1")) {
                    if (pGold > 9) {
                        pGold -= 10;
                        pKeys[0]++;
                        System.out.println("You bought a yellow key");
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("2")) {
                    if (pGold > 49) {
                        pGold -= 50;
                        pKeys[1]++;
                        System.out.println("You bought a blue key");
                    } else {
                        System.out.println("Sorry, you don't have enough gold");
                    }
                } else if (selection.equals("3")) {
                    if (pGold > 99) {
                        pGold -= 100;
                        pKeys[2]++;
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

    public static void useTeleporter(int level) {
        if (level < 0 || level > levelCount) {
            System.out.println("level out of bounds, please try again");
        } else {
            levels[currentLv][pY][pX] = ' ';
            currentLv = level;
            if (level == 0) {
                pX = 6;
                pY = 11;
            } else {
                pX = upDefaultPos[currentLv - 1][0];
                pY = upDefaultPos[currentLv - 1][1];
            }
        }
    }

    // mob related
    public static void useTotem() {
        System.out.format("%-2s%-3s%-20s%-6s%-6s%-6s%-6s%-6sEstimated Damage\n", " ", " ", "Mob Name", "HP", "Atk.",
                "Def.", "Gold", "EXP");
        for (int i = 0; i < mobLetter.length; i++) {
            if (checkFieldForChar(mobLetter[i])) {
                totemPrinter(mobLetter[i]);
            }
        }
    }

    public static void totemPrinter(char mob) {
        if (pAtk - mobStats(mob, 2) > 0) {
            int mobDmg = negativeToZero(mobStats(mob, 0) / (pAtk - mobStats(mob, 2)) * ((mobStats(mob, 1) - pDef)));
            if (mob == 'K') {
                System.out.format("%-2s%-3s%-20s%-6d%-6d%-6d%-6d%-6d%-6d\n", mob, letterToBoard(mob),
                        charToMobName(mob), mobStats(mob, 0), mobStats(mob, 1), mobStats(mob, 2), mobStats(mob, 3),
                        mobStats(mob, 4), pHealth / 4 + mobDmg);
            } else {
                System.out.format("%-2s%-3s%-20s%-6d%-6d%-6d%-6d%-6d%-6d\n", mob, letterToBoard(mob),
                        charToMobName(mob), mobStats(mob, 0), mobStats(mob, 1), mobStats(mob, 2), mobStats(mob, 3),
                        mobStats(mob, 4), mobDmg);
            }
        } else {
            System.out.format("%-2s%-3s%-20s%-6d%-6d%-6d%-6d%-6dUnbeatable\n", mob, letterToBoard(mob),
                    charToMobName(mob), mobStats(mob, 0), mobStats(mob, 1), mobStats(mob, 2), mobStats(mob, 3),
                    mobStats(mob, 4));
        }
    }

    public static boolean checkFieldForChar(char char1) {
        for (int i = 0; i < 13; i++) {
            if (new String(levels[currentLv][i]).contains(String.valueOf(char1))) {
                return true;
            }
        }
        return false;
    }

    // 0 health, 1 atk, 2 def, 3 gold, 4 exp
    // green slime, red ball, skeleton, witch, skeleton soldier
    public static int mobStats(char mob, int index) {
        return mobStats[new String(mobLetter).indexOf(mob)][index];
    }

    public static String charToMobName(char mob) {
        return mobName[new String(mobLetter).indexOf(mob)];
    }

    public static int negativeToZero(int num) {
        return Math.max(0, num);
    }

    public static void showguide() {
        System.out.println("GUIDE GOES HERE");
    }

    public void printField() {
        TextView gameWindow = findViewById(R.id.gameWindow);
        gameWindow.setText("");
        for (int i = 0; i < 13; i++) {
            String line = "";
            for (int j = 0; j < 13; j++) {
                line += letterToBoard(levels[currentLv][i][j]);
            }
            gameWindow.append(line + "\n");
        }

        /*
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                System.out.print(letterToBoard(levels[currentLv][i][j]));
            }
            switch (i) {
                case 0:
                    System.out.println("╦═══════════════╗");
                    break;
                case 1:
                    System.out.println("║ Player Stats  ║");
                    break;
                case 2:
                    System.out.format("║ Level   %5d ║\n", pLv);
                    break;
                case 3:
                    System.out.format("║ Health  %5d ║\n", pHealth);
                    break;
                case 4:
                    System.out.format("║ Attack   %4d ║\n", pAtk);
                    break;
                case 5:
                    System.out.format("║ Defense  %4d ║\n", pDef);
                    break;
                case 6:
                    System.out.format("║ Gold    %5d ║\n", pGold);
                    break;
                case 7:
                    System.out.format("║ EXP     %5d ║\n", pEXP);
                    break;
                case 8:
                    System.out.println("║ Keys          ║");
                    break;
                case 9:
                    System.out.format("║ Yellow ░  %3d ║\n", pKeys[0]);
                    break;
                case 10:
                    System.out.format("║ Blue   ▒  %3d ║\n", pKeys[1]);
                    break;
                case 11:
                    System.out.format("║ Red    ▓  %3d ║\n", pKeys[2]);
                    break;
                case 12:
                    System.out.println("╩═══════════════╝");
                    break;
                default:
                    System.out.format("\n");
                    break;
            }
        }
        System.out.format("         Level %d\n", currentLv);

         */
    }

    public static String letterToBoard(char board) {
        switch (board) {
            case '█':
                return "██";
            case '~':
                pause(0.0001);
                if (random.nextDouble() < 0.2) {
                    return "~ ";
                } else if (random.nextDouble() < 0.3) {
                    return " ~";
                } else {
                    return "~~";
                }
            case '*':
                pause(0.0001);
                if (random.nextDouble() < 0.2) {
                    return "* ";
                } else if (random.nextDouble() < 0.3) {
                    return " *";
                } else {
                    return "**";
                }
            case 'P':
                return "PC";
            case 'G':
                return "gs";
            case 'r':
                return "rs";
            case 's':
                return "sk";
            case 'w':
                return "wt";
            case 'S':
                return "Sk";
            case 'b':
                return "bt";
            case 'q':
                return "bs";
            case 'm':
                return "mf";
            case 'B':
                return "Bt";
            case 'n':
                return "rb";
            case 'v':
                return "BT";
            case 'W':
                return "Wt";
            case 'M':
                return "SK";
            case 'K':
                return "WK";
            case 'h':
                return "hs";
            case 'H':
                return "Hs";
            case 'y':
                return "bg";
            case 'g':
                return "kw";
            case 'f':
                return "Mf";
            case 'j':
                return "Rb";
            case 'J':
                return "RB";
            case 'd':
                return "dw";
            case 'c':
                return "GS";
            case 'C':
                return "GM";
            case 'x':
                return "Sw";
            case 'X':
                return "SW";
            case 'a':
                return "Bs";
            case 'A':
                return "BS";
            case 'z':
                return "RB";
            case ' ':
                return "  ";
            case '░':
                return "░░";
            case '▒':
                return "▒▒";
            case '▓':
                return "▓▓";
            case '\n':
                return "\n";
            case '+':
                return "++";
            case '-':
                return "--";
            case '1':
                return "b1";
            case '2':
                return "b2";
            case '3':
                return "g1";
            case '4':
                return "g2";
            case '5':
                return "k1";
            case '6':
                return "k2";
            case '7':
                return "k3";
            case 'â':
                return "sw";
            case 'ä':
                return "sh";
            default:
                return String.format("*" + board);
        }
    }

    public static void pause(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
        }
    }

}