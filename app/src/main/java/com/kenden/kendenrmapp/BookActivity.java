package com.kenden.kendenrmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kenden.kendenrmapp.models.Book;
import com.rengwuxian.materialedittext.MaterialEditText;

public class BookActivity extends AppCompatActivity {


    private Button btnBackkk, btnContri;
    private MaterialEditText title, genre, year, org, town, vol, type, size, aftor, copy, iN, place, price, sys, signAftor, kS, mixing;

    private DatabaseReference mDBase;

    private String BOOK_KEY = "Book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        init();

        btnBackkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActivity.this, CatActivity.class);
                startActivity(intent);
            }
        });

        btnContri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void init()
    {
        btnBackkk = findViewById(R.id.btnBackkk);
        btnContri = findViewById(R.id.btnContri);
        title = findViewById(R.id.title);
        genre = findViewById(R.id.genre);
        year = findViewById(R.id.year);
        org = findViewById(R.id.org);
        town = findViewById(R.id.town);
        vol = findViewById(R.id.vol);
        type = findViewById(R.id.type);
        size = findViewById(R.id.size);
        aftor = findViewById(R.id.aftor);
        copy = findViewById(R.id.copy);
        iN = findViewById(R.id.iN);
        place = findViewById(R.id.place);
        price = findViewById(R.id.price);
        sys = findViewById(R.id.sys);
        signAftor = findViewById(R.id.signAftor);
        kS = findViewById(R.id.kS);
        mixing = findViewById(R.id.mixing);

    }

    public void contributeBook()
    {
        mDBase = FirebaseDatabase.getInstance().getReference(BOOK_KEY);
        String dTitle = title.getText().toString();
        String dGenre = genre.getText().toString();
        String dYear = year.getText().toString();
        String dOrg = org.getText().toString();
        String dTown = town.getText().toString();
        String dVol = vol.getText().toString();
        String dType = type.getText().toString();
        String dSize = size.getText().toString();
        String dAftor = aftor.getText().toString();
        String dCopy = copy.getText().toString();
        String diN = iN.getText().toString();
        String dPlace = place.getText().toString();
        String dPrice = price.getText().toString();
        String dSys = sys.getText().toString();
        String dSignAftor = signAftor.getText().toString();
        String dkS = kS.getText().toString();
        String dMixing = mixing.getText().toString();

        Book newBook = new Book(dTitle, dGenre, dYear, dOrg, dTown, dVol, dType, dSize, dAftor, dCopy, diN, dPlace, dPrice, dSys, dSignAftor, dkS, dMixing);
        mDBase.push().setValue(newBook);
        Toast.makeText(this, "Данные внесены", Toast.LENGTH_SHORT).show();
    }
}