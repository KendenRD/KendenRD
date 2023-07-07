package com.kenden.kendenrmapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenden.kendenrmapp.models.Emp;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference ems;
    private EditText textEmail, textPass, textFullName, textDepart, textPhone, textEmailReg, textPassReg;
    private LinearLayout lLaunch, llLaunch, lReg, llReg, lSignIn, llSignIn;
    private TextView textSignInName;

    private Button btnSignIn, btnReg, btnBack, btnRegis, btnBackSignOut, btnIntant;

    RelativeLayout relement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listReg();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher();
            }
        });
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegis();
            }
        });
        btnBackSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                launcher();
            }
        });
        btnIntant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            showUserName();
            Emp newEmp = new Emp();
            String s = newEmp.getFullname();
            //String username = "Вы вошли как: " + cUser.getEmail();


            Toast.makeText(this, newEmp.getFullname(), Toast.LENGTH_SHORT).show();
        } else {
            launcher();

            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }

    }

    public void init() {
        textEmail = findViewById(R.id.textEmail);
        textPass = findViewById(R.id.textPass);
        textFullName = findViewById(R.id.textFullName);
        textDepart = findViewById(R.id.textDepart);
        textPhone = findViewById(R.id.textPhone);
        textEmailReg = findViewById(R.id.textEmailReg);
        textPassReg = findViewById(R.id.textPassReg);
        lLaunch = findViewById(R.id.lLaunch);
        llLaunch = findViewById(R.id.llLaunch);
        lReg = findViewById(R.id.lReg);
        llReg = findViewById(R.id.llReg);
        lSignIn = findViewById(R.id.lSignIn);
        llSignIn = findViewById(R.id.llSignIn);
        textSignInName = findViewById(R.id.textSignInName);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnReg = findViewById(R.id.btnReg);
        btnBack = findViewById(R.id.btnBack);
        btnRegis = findViewById(R.id.btnRegis);
        btnBackSignOut = findViewById(R.id.btnBackSignOut);
        btnIntant = findViewById(R.id.btnIntent);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ems = db.getReference("epms");
        relement = findViewById(R.id.relement);
    }

    private void showUserName() {
        lLaunch.setVisibility(View.GONE);
        llLaunch.setVisibility(View.GONE);
        lReg.setVisibility(View.GONE);
        llReg.setVisibility(View.GONE);
        lSignIn.setVisibility(View.VISIBLE);
        llSignIn.setVisibility(View.VISIBLE);
    }

    private void launcher() {
        lLaunch.setVisibility(View.VISIBLE);
        llLaunch.setVisibility(View.VISIBLE);
        lReg.setVisibility(View.GONE);
        llReg.setVisibility(View.GONE);
        lSignIn.setVisibility(View.GONE);
        llSignIn.setVisibility(View.GONE);
    }

    private void listReg() {
        lLaunch.setVisibility(View.GONE);
        llLaunch.setVisibility(View.GONE);
        lReg.setVisibility(View.VISIBLE);
        llReg.setVisibility(View.VISIBLE);
        lSignIn.setVisibility(View.GONE);
        llSignIn.setVisibility(View.GONE);
    }

    private void onClickSignIn() {
        if (!TextUtils.isEmpty(textEmail.getText().toString()) && !TextUtils.isEmpty(textPass.getText().toString())) {
            mAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        showUserName();
                        Toast.makeText(getApplicationContext(), "User Sign In Successeful", Toast.LENGTH_SHORT).show();
                    } else {
                        launcher();
                        Toast.makeText(getApplicationContext(), "User Sign In failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }




    private void onClickRegis() {
        //позже
        if (!TextUtils.isEmpty(textFullName.getText().toString()) && !TextUtils.isEmpty(textDepart.getText().toString()) &&
                !TextUtils.isEmpty(textPhone.getText().toString()) && !TextUtils.isEmpty(textEmailReg.getText().toString()) &&
                !TextUtils.isEmpty(textPassReg.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(textEmailReg.getText().toString(), textPassReg.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Emp emp = new Emp();
                            emp.setFullname(textFullName.getText().toString());
                            emp.setDepart(textDepart.getText().toString());
                            emp.setPhone(textPhone.getText().toString());
                            emp.setEmail(textEmailReg.getText().toString());
                            emp.setPassword(textPassReg.getText().toString());

                            ems.child(emp.getFullname()).setValue(emp)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Пользователь добавлен!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Введите все данные", Toast.LENGTH_SHORT).show();
        }
    }



}