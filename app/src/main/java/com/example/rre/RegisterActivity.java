package com.example.rre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username,fullname,email,password;
    Button register;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=findViewById(R.id.user);
        fullname=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        register = findViewById(R.id.register);
        auth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please Wait...");
                pd.show();
                String usr = username.getText().toString();
                String nam = fullname.getText().toString();
                String em = email.getText().toString();
                String pas = password.getText().toString();
                if (TextUtils.isEmpty(usr) || TextUtils.isEmpty(nam) || TextUtils.isEmpty(em) || TextUtils.isEmpty(pas)) {
                    Toast.makeText(RegisterActivity.this, "All Filling Are Required", Toast.LENGTH_SHORT).show();
                } else if (pas.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password Must be 6 Characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(usr, nam, em, pas);
                }
    }
        });
    }
    private void register(final String username,final String fullname,final String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users ").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("Username",username.toLowerCase());
                    hashMap.put("Name",fullname);
                    hashMap.put("Bio","" );
                    hashMap.put("ImageUrl", "default");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, BottumActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "You can not Register with this email or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });

    }
}


