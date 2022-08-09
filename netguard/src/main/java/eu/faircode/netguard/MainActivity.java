package eu.faircode.netguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    TextView tv_greetings,tv_userName;
    SwitchCompat swEnabled;
    Button button_Started,btn_monthly,button_trialed;
    BillingClient billingClient;
    String userName,trialEnded,trial,buy;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        initViews();
        getCurrentTime();

        trialEnded = Utilities.getString(MainActivity.this,"trialEnded");
        trial = Utilities.getString(MainActivity.this,"trial");
        buy = Utilities.getString(MainActivity.this,"buy");
        if (buy.equals("yes"))
        {
            button_Started.setVisibility(View.GONE);
            button_trialed.setVisibility(View.VISIBLE);
            button_trialed.setText("Getting Started");
            btn_monthly.setVisibility(View.GONE);
        }
         if (trial.equals("yes"))
        {
            button_Started.setVisibility(View.GONE);
            button_trialed.setVisibility(View.VISIBLE);
        }
         if (trialEnded.equals("yes"))
        {
            button_Started.setVisibility(View.GONE);
            button_trialed.setVisibility(View.GONE);
        }
        button_Started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.saveInt(getApplicationContext(),"trial_time",(int) System.currentTimeMillis());
                Utilities.saveString(getApplicationContext(),"trial","yes");
                startActivity(new Intent(getApplicationContext(),ActivityMain.class));
                finish();
            }
        });

        SessionManager manager = new SessionManager(this);

        userName = manager.getName();

        tv_userName.setText(userName);



        Paper.init(MainActivity.this);
        setupbillingclint();
        btn_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Paper.book().read("inapp", false)) {

                } else {
                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("android.test.purchased"))
                            .setType(BillingClient.SkuType.INAPP)
                            .build();

                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                            Toast.makeText(MainActivity.this, "" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
//                             Toast.makeText(AddPicturesActivity.this, "" + list.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(0))
                                        .build();
                                billingClient.launchBillingFlow(MainActivity.this, billingFlowParams);
                            } else {
                                Toast.makeText(MainActivity.this, "Cannot Remove ads", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }




    private void getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        String time = dateFormat.format(c.getTime());

        if (Integer.parseInt(time)>= 6 && Integer.parseInt(time)<10){
            tv_greetings.setText("Good Morning");
            img.setImageResource(R.drawable.sunny);

        }
        else if (Integer.parseInt(time)>= 10 && Integer.parseInt(time)<15){
            tv_greetings.setText("Good Noon");
            img.setImageResource(R.drawable.sunny);
        }
        else if (Integer.parseInt(time)>= 15 && Integer.parseInt(time)<17){
            tv_greetings.setText("Good Afternoon");
            img.setImageResource(R.drawable.sunny);
        }
        else if(Integer.parseInt(time)>=17 && Integer.parseInt(time)<20){
            tv_greetings.setText("Good Evening");
            img.setImageResource(R.drawable.moon);
        }
        else
        {
            tv_greetings.setText("Good Night");
            img.setImageResource(R.drawable.moon);
        }
    }
    private void initViews() {
        tv_greetings = findViewById(R.id.greetings);
        tv_userName = findViewById(R.id.text_userName);
        swEnabled = findViewById(R.id.swEnabled);
        button_Started = findViewById(R.id.started);
        button_trialed = findViewById(R.id.freeTrialed);
        btn_monthly = findViewById(R.id.btn_monthly);
        img = findViewById(R.id.img);
    }
    public void MAin(View view) {
        Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
        startActivity(intent);
    }

    private void setupbillingclint() {

        billingClient = BillingClient.newBuilder(MainActivity.this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                for (Purchase purchase : list) {
                    handlePurchase(purchase);
                }
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {


                    Toast.makeText(MainActivity.this, "Successfully Remove ads", Toast.LENGTH_LONG).show();
                    Paper.book().write("inapp", true);
                    Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
                    Utilities.saveString(getApplicationContext(),"buy","yes");
                    startActivity(intent);
                    finish();
//                    showEditDialog();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    Toast.makeText(MainActivity.this, "Purchases Restored...", Toast.LENGTH_LONG).show();
                    Paper.book().write("inapp", true);
                    Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
                    Utilities.saveString(getApplicationContext(),"buy","yes");
                    startActivity(intent);
                    finish();
//                    showEditDialog();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) {
                    Toast.makeText(MainActivity.this, "Billing Unavailable..!", Toast.LENGTH_SHORT).show();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {
                    Toast.makeText(MainActivity.this, "Billing Error..!", Toast.LENGTH_SHORT).show();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                    Toast.makeText(MainActivity.this, "Service Disconnected..!", Toast.LENGTH_SHORT).show();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_TIMEOUT) {
                    Toast.makeText(MainActivity.this, "Service Timeout..!", Toast.LENGTH_SHORT).show();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE) {
                    Toast.makeText(MainActivity.this, "Service Unavailable..!", Toast.LENGTH_SHORT).show();
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(MainActivity.this, "Billing not Completed..!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Billing Error..!", Toast.LENGTH_SHORT).show();
                }


            }
        }).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(MainActivity.this, "startConnection" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {


//                Toast.makeText(MainActivity.this, "You are disconnected from billing", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        Toast.makeText(MainActivity.this, String.valueOf("token:  " + purchase.getPurchaseToken()), Toast.LENGTH_SHORT).show();
        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }
}