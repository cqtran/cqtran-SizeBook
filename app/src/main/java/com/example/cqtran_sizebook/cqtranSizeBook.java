/*
 * Copyright (c) $2017 CMPUT 301 University of Alberta. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.cqtran_sizebook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayAdapter<Data> adapter;
    private ArrayList<Data> pullList = new ArrayList<Data>();

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

    /**
     * At records.setOnItemClickListener able to select item from listview.
     * When a record is selected on the list, already stored values
     * are loaded into the edittext view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cqtran_size_book);

        editName = (EditText) findViewById(R.id.name);
        editDate = (EditText) findViewById(R.id.date);
        editNeck = (EditText) findViewById(R.id.neck);
        editBust = (EditText) findViewById(R.id.bust);
        editChest = (EditText) findViewById(R.id.chest);
        editWaist = (EditText) findViewById(R.id.waist);
        editHip = (EditText) findViewById(R.id.hip);
        editInseam = (EditText) findViewById(R.id.inseam);
        editComment = (EditText) findViewById(R.id.comment);

        recordList = new ArrayList<Data>();
        records = (ListView) findViewById(R.id.listview);
        numOfRecords = (TextView) findViewById(R.id.numofrecords);

        records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                pullList.clear();
                pullList.add(adapter.getItem(position));
                editName.setText(recordList.get(position).getName());
                editDate.setText(recordList.get(position).getDate());
                editNeck.setText(cleanup(recordList.get(position).getNeck()));
                editBust.setText(cleanup(recordList.get(position).getBust()));
                editChest.setText(cleanup(recordList.get(position).getChest()));
                editWaist.setText(cleanup(recordList.get(position).getWaist()));
                editHip.setText(cleanup(recordList.get(position).getHip()));
                editInseam.setText(cleanup(recordList.get(position).getInseam()));
                editComment.setText(recordList.get(position).getComment());
            }
        });
    }

    /**
     * Replaces 0.0 value with "" so it doesn't fill
     * up all the edittext fields.
     * @param string
     * @return
     */
    public String cleanup(String string) {
        if (string.equals("0.0")) {
            return "";
        } else {
            return string;
        }
    }
    /**
     * the onClick function when add is clicked.
     * adds a record to the list.
     * @param v view for add button
     */
    public void addButton(View v){
                Button addBtn = (Button) findViewById(R.id.add);
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

    /**
     * Edits a current record.
     * Has an IndexOutOfBoundsException so app won't crash
     * when edit is pressed and there are no records.
     * @param vv view for edit button
     */
    public void editButton(View vv){
        try {
            Button editBtn = (Button) findViewById(R.id.edit);
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
        catch (IndexOutOfBoundsException ee) {

        }
    }

    /**
     * Deletes a record by removing the it from the list.
     * Has an IndexOutOfBoundsException so app won't crash when delete is pressed
     * and there are no records.
     * @param vvv view for delete button
     */
    public void deleteButton(View vvv){
        Button delBtn = (Button) findViewById(R.id.delete);
        try {
            recordList.remove(position);
            numOfRecords.setText(getString(R.string.counter, recordList.size()));
            adapter.notifyDataSetChanged();
            saveInFile();
        }
        catch (IndexOutOfBoundsException e) {
        }
    }

    /**
     * Takes text user inputs and converts into a Double.
     * @param entry the userinput
     * @return
     */
    public Double parseDouble(EditText entry) {
        Double part;
        try {
            part = Double.parseDouble(entry.getText().toString());
        } catch (NumberFormatException e) {
            part = 0.0;
        }
        return part;
    }

    //taken from lonelyTwitter from lab 301.

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















