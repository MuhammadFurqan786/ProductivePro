package eu.faircode.netguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SignUpActivity extends AppCompatActivity {

    ImageView back_screen;
    FirebaseAuth auth;
    Button buttonSignup;
    KProgressHUD hud;
    EditText ed_fname,ed_email,ed_phone,ed_pass,ed_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        back_screen = findViewById(R.id.back_Screen);
        buttonSignup = findViewById(R.id.btn_signUp);
        ed_fname = findViewById(R.id.edit_user_Name);
        ed_email = findViewById(R.id.edit_email);
        ed_phone = findViewById(R.id.edit_phone);
        ed_pass = findViewById(R.id.edit_confirmPassword);
        ed_dob = findViewById(R.id.edit_dob);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fname = ed_fname.getText().toString().trim();
                final String email = ed_email.getText().toString().trim();
                final String phone = ed_phone.getText().toString().trim();
                String pass = ed_pass.getText().toString().trim();
                final String dob = ed_dob.getText().toString().trim();

                if (fname.isEmpty() || email.isEmpty()|| phone.isEmpty()|| pass.isEmpty()|| dob.isEmpty())
                {
                    Toast.makeText(SignUpActivity.this, "Please Add Missing Details", Toast.LENGTH_SHORT).show();
                }
                else {
                    hud = KProgressHUD.create(SignUpActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                hud.dismiss();
                                Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                String uid = auth.getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
                                reference.child("name").setValue(fname);
                                reference.child("email").setValue(email);
                                reference.child("phone").setValue(phone);
                                reference.child("dob").setValue(dob);

                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                hud.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        back_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}