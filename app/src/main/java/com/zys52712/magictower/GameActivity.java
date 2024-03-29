package com.zys52712.magictower;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    public static WeakReference<TextView> viewWeakReference;

    public static final String EXTRA_MID = "";
    public static final String EXTRA_TYPE = "";
    static char[][][] levels = new char[22][13][14];
    static String tempLine;
    static int oldPx = 0;
    static int oldPy = 0;
    static boolean firstRun = true;
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
    static int[][] downDefaultPos = { { 0, 0 }, { 6, 2 }, { 2, 1 }, { 1, 10 }, { 11, 10 }, // lv0 to lv4
            { 1, 10 }, { 10, 10 }, { 6, 11 }, { 2, 1 }, { 8, 5 }, // lv5 to lv9
            { 7, 8 }, { 1, 10 }, { 10, 11 }, { 2, 11 }, { 5, 11 }, // lv10 to lv14
            { 6, 1 }, { 8, 1 } }; // lv15 to lv19
    static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Context context = getApplicationContext();
        TextView gameWindow = findViewById(R.id.gameWindow);
        gameWindow.setSingleLine(false);
        InputStream levelTxt = null;

        ImageButton up = findViewById(R.id.button_up);
        ImageButton down = findViewById(R.id.button_down);
        ImageButton left = findViewById(R.id.button_left);
        ImageButton right = findViewById(R.id.button_right);
        Button invincible = findViewById(R.id.invincibility);
        viewWeakReference = new WeakReference<>(findViewById(R.id.msgBox));

        if(firstRun) {
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
            firstRun = false;
        }

        up.setOnClickListener(this::moveOnClick);
        down.setOnClickListener(this::moveOnClick);
        left.setOnClickListener(this::moveOnClick);
        right.setOnClickListener(this::moveOnClick);
        invincible.setOnClickListener(this::invincibility);
        //gameWindow.setText(levels[1][5].toString());

        levels[currentLv][pY][pX] = 'P';
        printField();
    }

    public void invincibility(View v){
        pHealth = 50000;
        pAtk = 3000;
        pDef = 5000;
        pGold = 1000;
        pEXP = 1000;

        printField();
    }

    @SuppressLint("NonConstantResourceId")
    public void moveOnClick(View v) {
        TextView message = findViewById(R.id.msgBox);
        message.setText("");

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        printField();
    }

    public void fight(char mob) {
        Intent intent = new Intent(this, FightActivity.class);
        String mobS = String.valueOf(mob);
        intent.putExtra(EXTRA_MID, mobS);
        startActivity(intent);
    }

    public void shop(char type) {
        Intent intent = new Intent(this, ShopActivity.class);
        String typeS = String.valueOf(type);
        intent.putExtra(EXTRA_TYPE, typeS);
        startActivity(intent);
    }

    public void totem(View view) {
        Intent intent = new Intent(this, TotemActivity.class);
        startActivity(intent);
    }

    public void teleport(View view) {
        Intent intent = new Intent(this, TeleportActivity.class);
        startActivity(intent);
    }

    public void debug(View view) {
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void movePC(char direction) {
        TextView message = findViewById(R.id.msgBox);
        String msg;

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
                char mob = levels[currentLv][pY][pX];
                String mobName = GameActivity.charToMobName(mob);
                int mobGold = GameActivity.mobStats(mob, 3);
                int mobEXP = GameActivity.mobStats(mob, 4);

                fight(levels[currentLv][pY][pX]);

                String endText = String.format("You defeated * %s *\n %d gold and %d exp gained", mobName, mobGold, mobEXP);
                message.setText(endText);
                replaceAndReturn();
                printField();
                break;
            case 'T':
                switch (currentLv) {
                    case 1:
                        hasTotem = true;
                        msg = "You have gained the totem!\nAllows you to see stats for the mobs on the board";
                        message.setText(msg);
                        break;
                    case 9:
                        hasTeleport = true;
                        msg = "You have gained the teleporter!\nAllows you to jump between levels.\n";
                        message.setText(msg);
                        break;
                }
                replaceAndReturn();
                break;
            case 'û':
                switch (currentLv) {
                    case 6:
                        message.setText("You leveled up!");
                        pHealth += 1000;
                        pAtk += 7;
                        pDef += 7;
                        pLv++;
                        break;
                    case 13:
                        message.setText("You leveled up (x3)!");
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
                message.setText("You gained a small health potion,\nhealth increased by 200!");
                pHealth += 200;
                replaceAndReturn();
                break;
            case '2':
                message.setText("You gained a large health potion,\nhealth increased by 500!");
                pHealth += 500;
                replaceAndReturn();
                break;
            case '3':
                message.setText("You gained blue gem, defense increased by 3!");
                pDef += 3;
                replaceAndReturn();
                break;
            case '4':
                message.setText("You gained red gem, attack increased by 3!");
                pAtk += 3;
                replaceAndReturn();
                break;
            case '5':
                message.setText("You gained a ░ key");
                pKeys[0]++;
                replaceAndReturn();
                break;
            case '6':
                message.setText("You gained a ▒ key");
                pKeys[1]++;
                replaceAndReturn();
                break;
            case '7':
                message.setText("You gained a ▓ key");
                pKeys[2]++;
                replaceAndReturn();
                break;
            case 'þ':
                message.setText("You gained a key of each type!");
                pKeys[0]++;
                pKeys[1]++;
                pKeys[2]++;
                replaceAndReturn();
                break;
            case 'â':
                switch (currentLv) {
                    case 2:
                    case 3:
                        message.setText("You gained a sword, attack increased by 10!");
                        pAtk += 10;
                        break;
                    case 9:
                        message.setText("You gained a greatsword, attack increased by 70!");
                        pAtk += 70;
                        break;
                }
                replaceAndReturn();
                break;
            case 'ä':
                switch (currentLv) {
                    case 2:
                    case 5:
                        message.setText("You gained a shield, defense increased by 10!");
                        pDef += 10;
                        break;
                    case 11:
                        message.setText("You gained a shield, defense increased by 70!");
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
                    message.setText("You used yellow key on door");
                    pKeys[0]--;
                    levels[currentLv][pY][pX] = ' ';
                } else {
                    message.setText("You don't have any yellow keys left");
                }
                pX = oldPx;
                pY = oldPy;
                break;
            case '▒':
                if (pKeys[1] > 0) {
                    message.setText("You used blue key on door");
                    pKeys[1]--;
                    levels[currentLv][pY][pX] = ' ';
                } else {
                    message.setText("You don't have any blue keys left");
                }
                pX = oldPx;
                pY = oldPy;
                break;
            case '▓':
                if (pKeys[2] > 0) {
                    message.setText("You used red key on door");
                    pKeys[2]--;
                    levels[currentLv][pY][pX] = ' ';
                } else {
                    message.setText("You don't have any red keys left");
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

    @SuppressLint("SetTextI18n, DefaultLocale")
    public static void useTeleporter(int level) {
        if (level < 0 || level > levelCount) {
            viewWeakReference.get().setText("Input out of bounds, please try again");
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
            levels[currentLv][pY][pX] = 'P';String temp = String.format("Teleported to floor %d", currentLv);
            viewWeakReference.get().setText(temp);
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

    public static void showguide() {
        System.out.println("GUIDE GOES HERE");
    }

    @SuppressLint("DefaultLocale")
    public void printField() {
        TextView gameWindow = findViewById(R.id.gameWindow);
        TextView test = findViewById(R.id.testView);

        gameWindow.setText("");
        test.setText("");
        for (int i = 0; i < 13; i++) {
            StringBuilder line = new StringBuilder();
            String temp;
            for (int j = 0; j < 13; j++) {
                line.append(letterToBoard(levels[currentLv][i][j]));
            }

            switch (i) {
                case 0:
                    temp = "══════════╗";
                    break;
                case 1:
                    temp = String.format(" Floor %2d ║", currentLv);
                    break;
                case 2:
                    temp = String.format(" LV  %4d ║", pLv);
                    break;
                case 3:
                    temp = String.format(" HP %5d ║", pHealth);
                    break;
                case 4:
                    temp = String.format(" ATK %4d ║", pAtk);
                    break;
                case 5:
                    temp = String.format(" DEF %4d ║", pDef);
                    break;
                case 6:
                    temp = String.format(" GOLD%4d ║", pGold);
                    break;
                case 7:
                    temp = String.format(" EXP %4d ║", pEXP);
                    break;
                case 8:
                    temp = " Keys     ║";
                    break;
                case 9:
                    temp = String.format(" ░ key%3d ║", pKeys[0]);
                    break;
                case 10:
                    temp = String.format(" ▒ key%3d ║", pKeys[1]);
                    break;
                case 11:
                    temp = String.format(" ▓ key%3d ║", pKeys[2]);
                    break;
                case 12:
                    temp = "══════════╝";
                    break;
                default:
                    temp = "";
                    break;
            }
            line.append(temp);
            gameWindow.append(line + "\n");
        }
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
                return "*" + board;
        }
    }

    public static void pause(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}