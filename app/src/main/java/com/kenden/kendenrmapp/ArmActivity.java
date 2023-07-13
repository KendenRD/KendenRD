package com.kenden.kendenrmapp;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenden.kendenrmapp.models.Book;
import com.kenden.kendenrmapp.models.Formular;
import com.kenden.kendenrmapp.models.Reader;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArmActivity extends AppCompatActivity  {
    private ReaderFragment readerFragment = null;
    private fragment_bookis fragmentBookis = null;
    private FrameLayout fr_placee;
    private AutoCompleteTextView autoComTVR, autoComTVB;
    private ArrayAdapter<String> adapter, adapterB;
    private List<String> listData, listDataB;
    private fragment_formular fragmentFormular = null;
    private fragment_catalogizator fragmentCatalogizator = null;
    //private Fragment fragment = null;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private NavigationBarView bNav;
    private Button  btnExit, btnBookiss;
    private DatabaseReference mDB;
    private String READER_KEY = "Reader";
    private DatabaseReference mDBase;
    private String BOOK_KEY = "Book";
    private DatabaseReference mDataBF;
    private String FORMULAR_KEY = "formular";

    ConstraintLayout root;
    private LinearLayout laun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm);
        init();
        fbNav();
        BookR();
        Book();
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ArmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init()
    {
        btnExit=findViewById(R.id.btnExit);
        root=findViewById(R.id.root_element);
        bNav=findViewById(R.id.bNav);
        fr_placee=findViewById(R.id.fr_place);
        autoComTVR=findViewById(R.id.autoComTVR);
        autoComTVB=findViewById(R.id.autoComTVB);
        listData = new ArrayList<>();
        listDataB = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, listData);
        adapterB = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, listDataB);
        autoComTVR.setThreshold(0);
        autoComTVR.setAdapter(adapter);
        autoComTVB.setThreshold(0);
        autoComTVB.setAdapter(adapterB);
        btnBookiss=findViewById(R.id.btnBookiss);
        mDB = FirebaseDatabase.getInstance().getReference(READER_KEY);
        mDBase = FirebaseDatabase.getInstance().getReference(BOOK_KEY);
        mDataBF = FirebaseDatabase.getInstance().getReference(FORMULAR_KEY);
    }

    public void showRegisterReader()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);//вызов всп. окна
        dialog.setTitle("Оформление читателя");
        dialog.setMessage("Введите данные читателя");

        LayoutInflater inflater = LayoutInflater.from(this);//берем шаблон строк
        View register_reader = inflater.inflate(R.layout.register_reader, null);
        dialog.setView(register_reader);

        final MaterialEditText name = register_reader.findViewById(R.id.nameField);
        final MaterialEditText dob = register_reader.findViewById(R.id.dobField);
        final MaterialEditText educ = register_reader.findViewById(R.id.educField);
        final MaterialEditText work = register_reader.findViewById(R.id.workField);
        final MaterialEditText address = register_reader.findViewById(R.id.addressField);
        final MaterialEditText pas = register_reader.findViewById(R.id.pasportField);


        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {//кнопка отменить впл. окно
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Оформить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //проверка на пустоту строки
                if(TextUtils.isEmpty(name.getText().toString()))
                {
                    Snackbar.make(root, "Введите ФИО", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(dob.getText().toString()))
                {
                    Snackbar.make(root, "Введите дату рождения", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(educ.getText().toString()))
                {
                    Snackbar.make(root, "Введите образование", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(work.getText().toString()))
                {
                    Snackbar.make(root, "Введите место работы (учёбы)", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(address.getText().toString()))
                {
                    Snackbar.make(root, "Введите домашний адрес", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pas.getText().toString()))
                {
                    Snackbar.make(root, "Введите серию, номер паспорта", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //оформление читателя

                String id = mDB.getKey();
                String dName = name.getText().toString();
                String dDob = dob.getText().toString();
                String dEduc = educ.getText().toString();
                String dWork = work.getText().toString();
                String dAddress = address.getText().toString();
                String dPas = pas.getText().toString();

                Reader newReader = new Reader(id, dName, dDob, dEduc, dWork, dAddress, dPas);
                mDB.push().setValue(newReader);

                Intent i = getIntent();
                finish();
                startActivity(i);
            }
        });

        dialog.show();
    }

    public void fbNav()
    {
    //нижняя панель навигации

        bNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.readers)
                {
                    fr_placee.setVisibility(View.VISIBLE);
                    autoComTVR.setVisibility(View.GONE);
                    autoComTVB.setVisibility(View.GONE);
                    btnBookiss.setVisibility(View.GONE);
                    readerFragment = new ReaderFragment();
                    fm =getSupportFragmentManager();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fr_place, readerFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                if(item.getItemId() == R.id.bookis)
                {
                    fr_placee.setVisibility(View.GONE);
                    autoComTVR.setVisibility(View.VISIBLE);
                    autoComTVB.setVisibility(View.VISIBLE);
                    btnBookiss.setVisibility(View.VISIBLE);
                }
                if(item.getItemId() == R.id.formular)
                {
                    fr_placee.setVisibility(View.VISIBLE);
                    autoComTVR.setVisibility(View.GONE);
                    autoComTVB.setVisibility(View.GONE);
                    btnBookiss.setVisibility(View.GONE);

                    fragmentFormular = new fragment_formular();
                    fm =getSupportFragmentManager();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fr_place, fragmentFormular);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                if(item.getItemId() == R.id.catalogizator)
                {
                    fr_placee.setVisibility(View.VISIBLE);
                    autoComTVR.setVisibility(View.GONE);
                    autoComTVB.setVisibility(View.GONE);
                    btnBookiss.setVisibility(View.GONE);

                    fragmentCatalogizator = new fragment_catalogizator();
                    fm =getSupportFragmentManager();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fr_place, fragmentCatalogizator);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return false;
            }
        });

    }

    public void onClickRegBook (View view)
    {   //fragment_catalogizator кнопка-переход в fragment_book
        fragment_book fragmentBook = new fragment_book();
        fm =getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fr_place, fragmentBook);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickRegBookBack (View view)
    {
        //fragment_book кнопка назад в каталог
        fragmentCatalogizator = new fragment_catalogizator();
        fm =getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fr_place, fragmentCatalogizator);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickBookContri (View view)
    {
        //fragment_book кнопка забивания книг
        MaterialEditText title, genre, year, org, town, vol, type, size, aftor, copy, iN, place, price, sys, signAftor, kS, mixing;

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
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    public void BookR ()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size() > 0)listData.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Reader reader = ds.getValue(Reader.class);
                    listData.add(reader.dName);
                    assert reader != null;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDB.addValueEventListener(vListener);
    }
    private void Book()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listDataB.size() > 0)listDataB.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Book book  = ds.getValue(Book.class);
                    listDataB.add(book.dTitle);
                    assert book != null;
                }
                adapterB.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDBase.addValueEventListener(vListener);
    }

    public void onClickForm(View view)
    {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String dateTime = simpleDateFormat.format(date);
        String name = autoComTVR.getText().toString();
        String book = autoComTVB.getText().toString();

        Formular newFormular = new Formular(name, book, dateTime);
        mDataBF.push().setValue(newFormular);
    }
    public void onClickShowRegReader (View view)
    {
        showRegisterReader();
    }
}