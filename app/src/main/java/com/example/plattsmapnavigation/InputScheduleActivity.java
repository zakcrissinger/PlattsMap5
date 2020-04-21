package com.example.plattsmapnavigation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class InputScheduleActivity extends AppCompatActivity {
    Button btnEnterSched, addClass;
    public int count = 0;
    private DocumentReference mDocRef;
    public static final String FILE_NAME = "classes_1.txt";
    private int[] IDs= new int[50];

    private void setIDs(){
        int i = 0;
        while(i < IDs.length){
            IDs[i] = i;
            i += 1;
        }
    }
    public void addRow(TableLayout table){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        TableRow r1 = new TableRow(this);
        r1.setId(IDs[count]);
        count += 1;
        r1.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        //class text
        EditText edit_class = new EditText(this);
        edit_class.setId(IDs[count]);
        edit_class.setInputType(96);
        edit_class.setWidth(width/4);
        count +=1;
        r1.addView(edit_class);
        EditText edit_loc = new EditText(this);
        edit_loc.setId(IDs[count]);
        edit_loc.setInputType(96);
        edit_loc.setWidth(width/4);
        count +=1;
        r1.addView(edit_loc);
        //start text
        EditText edit_start = new EditText(this);
        edit_start.setId(IDs[count]);
        edit_start.setInputType(32);
        edit_start.setWidth(width/4);
        count +=1;
        r1.addView(edit_start);
        //end text
        EditText edit_end = new EditText(this);
        edit_end.setId(IDs[count]);
        edit_end.setWidth(width/4);
        edit_end.setInputType(32);
        count +=1;
        r1.addView(edit_end);
        //add row
        table.addView(r1);
    }
    public void addRow2(View view){
        TableLayout table = findViewById(R.id.table);
        TableRow r1 = new TableRow(this);
        r1.setId(IDs[count]);
        count += 1;
        r1.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        //class text
        EditText edit_class = new EditText(this);
        edit_class.setId(IDs[count]);
        edit_class.setInputType(96);
        count +=1;
        r1.addView(edit_class);
        //loc text
        EditText edit_loc = new EditText(this);
        edit_loc.setId(IDs[count]);
        edit_loc.setInputType(96);
        count +=1;
        r1.addView(edit_loc);
        //start text
        EditText edit_start = new EditText(this);
        edit_start.setId(IDs[count]);
        edit_start.setInputType(32);
        count +=1;
        r1.addView(edit_start);
        //end text
        EditText edit_end = new EditText(this);
        edit_end.setId(IDs[count]);
        edit_end.setInputType(32);
        count +=1;
        r1.addView(edit_end);
        //add row
        table.addView(r1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIDs();
        setContentView(R.layout.activity_input_schedule);
        btnEnterSched = findViewById(R.id.EnterBtn);
        btnEnterSched.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    EnterSchedule(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addClass = findViewById(R.id.addClass);
        addClass.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (count < 29){
                    addRow2(v);
                }
                else{
                    Toast.makeText(InputScheduleActivity.this, "Maximum Classes Created",Toast.LENGTH_SHORT).show();
                }
            }
        });
        TableLayout table = findViewById(R.id.table);
        addRow(table);

    }

    public void EnterSchedule(View view) throws IOException {
        SignInStatus.HasSched = true;
        Intent k = new Intent(InputScheduleActivity.this, ScheduleActivity.class);
        startActivity(k);
        TableLayout table = findViewById(R.id.table);
        FileOutputStream myObj = null;
        myObj = openFileOutput(FILE_NAME, MODE_PRIVATE);

        int i = 1;
        while(i < count){
            EditText _class = findViewById(i);
            EditText _loc = findViewById(i+1);
            EditText _start = findViewById(i+2);
            EditText _end = findViewById(i+3);
            String text = _class.getText() + " " + _loc.getText() + " " + _start.getText() + " " + _end.getText() + "\n";
            ManageFirestore.newClass(SignInStatus.UserName, text);
            myObj.write(text.getBytes());
            i += 5;
        }
        myObj.close();


    }
}
