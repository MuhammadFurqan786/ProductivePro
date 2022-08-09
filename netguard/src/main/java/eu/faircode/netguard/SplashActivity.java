package eu.faircode.netguard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    Button btn_login;
    Intent intent;
    int savedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        savedTime = Utilities.getInt(SplashActivity.this,"trial_time");
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedTime != 0) {
                    int currentTime = (int) System.currentTimeMillis();
                    //adding 3 days
                    int endTime = savedTime + (3 * 24 * 60 * 60 * 1000);
                    if (currentTime < endTime) {
                        SharedPreferences sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);
                        boolean isLoggedIn = sharedPreferences.getBoolean("login", false);
                        Toast.makeText(SplashActivity.this, "Trial is not Ended", Toast.LENGTH_SHORT).show();
                        intent = null;
                        if (isLoggedIn) {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                            Toast.makeText(SplashActivity.this, "Already Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent(SplashActivity.this, LoginActivity.class);
                            Toast.makeText(SplashActivity.this, "No user Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    } else {
                        showTrialDialog();
                        Toast.makeText(SplashActivity.this, "Your Trail has ended!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);
                    boolean isLoggedIn = sharedPreferences.getBoolean("login", false);
                    intent = null;
                    if (isLoggedIn) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        Toast.makeText(SplashActivity.this, "Already Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                        Toast.makeText(SplashActivity.this, "No user Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void showTrialDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashActivity.this);
        builder1.setMessage("Your Trial Version is Ended.");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Buy Premium",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utilities.saveString(SplashActivity.this,"trialEnded","yes");
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                });

        builder1.setNegativeButton(
                "Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}