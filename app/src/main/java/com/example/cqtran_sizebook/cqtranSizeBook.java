/*
 * Copyright (c) $2017 CMPUT 301 University of Alberta. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.cqtran_sizebook;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class cqtranSizeBook extends AppCompatActivity {

    private static final String FILENAME = "file.sav";
    private ListView records;
    private int position;
    private ArrayList<Data> recordList;
    private EditText editName;
    private EditText editDate;
    private EditText editNeck;
    private EditText editBust;
    private EditText editChest;
    private EditText editWaist;
    private EditText editHip;
    private EditText editInseam;
    private EditText editComment;
    private TextView numOfRecords;
    private ArrayAdapter<Data> adapter;
    private ArrayList<Data> pullList = new ArrayList<Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cqtran_size_book);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        recordList = new ArrayList<Data>();
        records = (ListView) findViewById(R.id.listview);
        Button addButton = (Button) findViewById(R.id.add);
        Button deleteButton = (Button) findViewById(R.id.delete);
        Button editButton = (Button) findViewById(R.id.edit);
        numOfRecords = (TextView) findViewById(R.id.numofrecords);
        editName = (EditText) findViewById(R.id.name);
        editDate = (EditText) findViewById(R.id.date);
        editNeck = (EditText) findViewById(R.id.neck);
        editBust = (EditText) findViewById(R.id.bust);
        editChest = (EditText) findViewById(R.id.chest);
        editWaist = (EditText) findViewById(R.id.waist);
        editHip = (EditText) findViewById(R.id.hip);
        editInseam = (EditText) findViewById(R.id.inseam);
        editComment = (EditText) findViewById(R.id.comment);
        Resources res = getResources();
        Toast.makeText(cqtranSizeBook.this,"Please enter your details. Fields are optional except for name",Toast.LENGTH_LONG).show();


        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = editName.getText().toString();
                String date = editDate.getText().toString();
                Double neck = parseDouble(editNeck);
                Double bust = parseDouble(editBust);
                Double chest = parseDouble(editChest);
                Double waist = parseDouble(editWaist);
                Double hip = parseDouble(editHip);
                Double inseam = parseDouble(editInseam);
                String comment = editComment.getText().toString();
                Data newEntry = new Data(name, date, neck, bust, chest, waist, hip, inseam, comment);
                recordList.add(newEntry);
                numOfRecords.setText(getString(R.string.counter,recordList.size()));
                adapter.notifyDataSetChanged();
                saveInFile();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String date = editDate.getText().toString();
                Double neck = parseDouble(editNeck);
                Double bust = parseDouble(editBust);
                Double chest = parseDouble(editChest);
                Double waist = parseDouble(editWaist);
                Double hip = parseDouble(editHip);
                Double inseam = parseDouble(editInseam);
                String comment = editComment.getText().toString();
                Data newEntry = new Data(name, date, neck, bust, chest, waist, hip, inseam, comment);
                recordList.set(position, newEntry);
                adapter.notifyDataSetChanged();
                saveInFile();
            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recordList.remove(position);
                    Data newEntry = new Data("", "", Double.parseDouble("0"),
                            Double.parseDouble("0"), Double.parseDouble("0"), Double.parseDouble("0"),
                            Double.parseDouble("0"), Double.parseDouble("0"), "");
                    numOfRecords.setText(getString(R.string.counter, recordList.size()));
                    adapter.notifyDataSetChanged();
                    saveInFile();
                }
                catch (IndexOutOfBoundsException e) {
                    Toast.makeText(cqtranSizeBook.this, "Please click on entry before deleting",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                pullList.clear();
                pullList.add(adapter.getItem(position));
                editName.setText(recordList.get(position).getName());
                editDate.setText(recordList.get(position).getDate());
                editNeck.setText(recordList.get(position).getNeck());
                editBust.setText(recordList.get(position).getBust());
                editChest.setText(recordList.get(position).getChest());
                editWaist.setText(recordList.get(position).getWaist());
                editHip.setText(recordList.get(position).getHip());
                editInseam.setText((recordList.get(position).getInseam()));
                editComment.setText(recordList.get(position).getComment());
            }
        });
    }


    public Double parseDouble(EditText entry) {
        Double part;
        try {
            part = Double.parseDouble(entry.getText().toString());
        } catch (NumberFormatException e) {
            part = 0.0;
        }
        return part;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Data>(this, R.layout.activity_data, recordList);
        records.setAdapter(adapter);
        numOfRecords.setText(getString(R.string.counter,recordList.size()));
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type ListType = new TypeToken<ArrayList<Data>>() {
            }.getType();
            recordList = gson.fromJson(in, ListType);
        } catch (FileNotFoundException e) {
            recordList = new ArrayList<Data>();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(recordList, out);
            out.flush();

            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}















