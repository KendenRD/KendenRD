package com.kenden.kendenrmapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kenden.kendenrmapp.models.Emp;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference ems;
    private EditText textEmail, textPass, textFullName, textDepart, textPhone, textEmailReg, textPassReg;
    private LinearLayout lLaunch, llLaunch, lReg, llReg;

    private Button btnSignIn, btnReg, btnBack, btnRegis;

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            Intent intent = new Intent(MainActivity.this, ArmActivity.class);
            startActivity(intent);


            Toast.makeText(this, "Вы авторизованы", Toast.LENGTH_SHORT).show();
        } else {
            launcher();

            Toast.makeText(this, "Войдите или зарегистрируйтесь", Toast.LENGTH_SHORT).show();
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
        btnSignIn = findViewById(R.id.btnSignIn);
        btnReg = findViewById(R.id.btnReg);
        btnBack = findViewById(R.id.btnBack);
        btnRegis = findViewById(R.id.btnRegis);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ems = db.getReference("epms");
        relement = findViewById(R.id.relement);
    }



    private void launcher() {
        lLaunch.setVisibility(View.VISIBLE);
        llLaunch.setVisibility(View.VISIBLE);
        lReg.setVisibility(View.GONE);
        llReg.setVisibility(View.GONE);
    }

    private void listReg() {
        lLaunch.setVisibility(View.GONE);
        llLaunch.setVisibility(View.GONE);
        lReg.setVisibility(View.VISIBLE);
        llReg.setVisibility(View.VISIBLE);
    }

    private void onClickSignIn() {
        if (!TextUtils.isEmpty(textEmail.getText().toString()) && !TextUtils.isEmpty(textPass.getText().toString())) {
            mAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, ArmActivity.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Пользователь успешно авторизован", Toast.LENGTH_SHORT).show();
                    } else {
                        launcher();
                        Toast.makeText(getApplicationContext(), "Введены неверные данные", Toast.LENGTH_SHORT).show();
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