package net.knaxel.thepod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister ;
    private EditText mEmail,mPhone,mDisplayName,mUserName, mPassword,mPasswordRepeat;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        firebaseAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mAuth = FirebaseAuth.getInstance();

        mRegister = findViewById(R.id.register);

        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);
        mDisplayName = findViewById(R.id.display_name);
        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mPasswordRepeat = findViewById(R.id.passwordRepeat);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String phone = mPhone.getText().toString();
                final String display_name = mDisplayName.getText().toString();
                final String user_name = mUserName.getText().toString();
                final String password = mPassword.getText().toString();
                final String passwordRepeat = mPasswordRepeat.getText().toString();


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplication(),  "Sign up Error", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = mAuth.getCurrentUser().getUid().toString();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map userInfo = new HashMap<>();
                            userInfo.put("email", email);
                            userInfo.put("phone", phone);
                            userInfo.put("display_name", display_name);
                            userInfo.put("user_name", user_name);
                            userInfo.put("profile_image_url", "default");

                             db.collection("users").get();
                             db.collection("users").document(userId).set(userInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(getApplication(), SplashScreenActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplication(), "There's a connection error.", Toast.LENGTH_LONG).show();
                                }

                            });

                        }
                    }
                });
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}