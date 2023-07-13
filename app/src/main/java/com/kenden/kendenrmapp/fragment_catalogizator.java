package com.kenden.kendenrmapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenden.kendenrmapp.models.Book;

import java.util.ArrayList;
import java.util.List;


public class fragment_catalogizator extends Fragment {

    private ListView listCat;
    private ArrayAdapter<String> adapterC;
    private List<String> listCatD;
    private List<Book> listTempL;
    private DatabaseReference mDBase;
    private String BOOK_KEY = "Book";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogizator, container, false);

        listCat = view.findViewById(R.id.listCat);
        listCatD = new ArrayList<>();
        listTempL = new ArrayList<>();
        adapterC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listCatD);
        listCat.setAdapter(adapterC);
        mDBase = FirebaseDatabase.getInstance().getReference(BOOK_KEY);

        showListCat();

        return view;
    }

    private void showListCat()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(listCatD.size() > 0)listCatD.clear();
                if(listTempL.size() > 0)listTempL.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Book book = ds.getValue(Book.class);
                    listCatD.add(book.dTitle);
                    listTempL.add(book);
                    assert book != null;
                }
                adapterC.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDBase.addValueEventListener(valueEventListener);
    }

    private void setOnClickItemL() {
        listCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = listTempL.get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());//вызов всп. окна
                dialog.setTitle(book.dTitle);
                dialog.setMessage(book.dAftor);

                LayoutInflater inflater = LayoutInflater.from(getActivity());//берем шаблон строк
                View fragment_form = inflater.inflate(R.layout.fragment_form, null);
                dialog.setView(fragment_form);

                final ListView listField = fragment_form.findViewById(R.id.listF);
                final ArrayAdapter<String> adapterFF = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, listCatD);
                listField.setAdapter(adapterFF);

                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {//кнопка отменить впл. окно
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Списать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
                dialog.show();
            }
        });
    }
}