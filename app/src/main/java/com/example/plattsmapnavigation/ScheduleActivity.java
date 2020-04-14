package com.example.plattsmapnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner; // Import the Scanner class to read text files

public class ScheduleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        TableLayout table = findViewById(R.id.table1);
        FileInputStream fis = null;
        try {
            fis = openFileInput(InputScheduleActivity.FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine())!=null){
                String[] texts = new String[4];
                texts = text.split(" ");
                addRow(table, texts);
                System.out.println(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    private void addRow(TableLayout table, String[] texts){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        final TableRow r1 = new TableRow(this);
        r1.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        int i = 0;
        while(i < texts.length){
            final TextView textField = new TextView(this);
            textField.setInputType(96);
            textField.setWidth(width/4);
            textField.setText(texts[i]);
            textField.setFreezesText(true);
            r1.addView(textField);
            i+=1;
        }
        table.addView(r1);

    }
}
