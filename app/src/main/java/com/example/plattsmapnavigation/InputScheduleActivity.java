package com.example.plattsmapnavigation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Arrays;

public class InputScheduleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnEnterSched, addClass;
    public int count = 0;
    private int[] IDs= new int[50];
    private boolean flag = false;
    private String[][] daysSelected = new String[5][7];
    private String[][] timesSelected = new String[5][2];
    private int dNum = 0;
    private int tNum = 0;
    private static final String[] _halls = {"AuSable","Beaumont","Hawkins","Hudson","Redcay","Saranac","Sibley","Ward","Yokum"};
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
        edit_class.setWidth(width/6);
        count +=1;
        r1.addView(edit_class);
        Spinner edit_loc = new Spinner(this);
        ArrayAdapter<CharSequence> adapterH = ArrayAdapter.createFromResource(this, R.array.halls, android.R.layout.simple_spinner_item);
        adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_loc.setId(IDs[count]);
        edit_loc.setAdapter(adapterH);
        edit_loc.setOnItemSelectedListener(this);
        count +=1;
        r1.addView(edit_loc);
        Spinner edit_days = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_days.setAdapter(adapter);
        edit_days.setOnItemSelectedListener(this);
        count +=1;
        r1.addView(edit_days);
        Button btn = new Button(this);
        btn.setText("Time");
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addTimePicker();
            }
        });
        r1.addView(btn);
        //start text
        /*
        EditText edit_start = new EditText(this);
        edit_start.setId(IDs[count]);
        edit_start.setInputType(32);
        edit_start.setWidth(width/6);
        count +=1;
        r1.addView(edit_start);
        //end text
        EditText edit_end = new EditText(this);
        edit_end.setId(IDs[count]);
        edit_end.setWidth(width/6);
        edit_end.setInputType(32);
        count +=1;
        r1.addView(edit_end);*/
        //add row
        table.addView(r1);
    }
    public void addRow2(View view){
        TableLayout table = findViewById(R.id.table);
        if(daysSelected[dNum][0]==null){
            Toast.makeText(InputScheduleActivity.this, "Add days to class before entering new class",Toast.LENGTH_SHORT).show();
        }
        else{
            dNum++;
            tNum++;
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
            edit_class.setWidth(width/6);
            count +=1;
            r1.addView(edit_class);
            Spinner edit_loc = new Spinner(this);
            ArrayAdapter<CharSequence> adapterH = ArrayAdapter.createFromResource(this, R.array.halls, android.R.layout.simple_spinner_item);
            adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            edit_loc.setId(IDs[count]);
            edit_loc.setAdapter(adapterH);
            edit_loc.setOnItemSelectedListener(this);
            count +=1;
            r1.addView(edit_loc);
            Spinner edit_days = new Spinner(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            edit_days.setAdapter(adapter);
            edit_days.setOnItemSelectedListener(this);
            count +=1;
            r1.addView(edit_days);
            Button btn = new Button(this);
            btn.setText("Time");
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    addTimePicker();
                }
            });
            r1.addView(btn);
            //start text
            /*EditText edit_start = new EditText(this);
            edit_start.setId(IDs[count]);
            edit_start.setInputType(32);
            edit_start.setWidth(width/6);
            count +=1;
            r1.addView(edit_start);
            //end text
            EditText edit_end = new EditText(this);
            edit_end.setId(IDs[count]);
            edit_end.setWidth(width/6);
            edit_end.setInputType(32);
            count +=1;
            r1.addView(edit_end);
            //add row*/
            table.addView(r1);
        }
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
                flag = false;
                if (count < 30){
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

        int i = 1;
        int j = 0;
        while(i < count){
            String theDays = "";
            EditText _class = findViewById(i);
            Spinner _loc = findViewById(i+1);
            int x = 0;
            while(daysSelected[j][x] != null){
                if(daysSelected[j][x].charAt(1) == 'h'){
                    theDays = theDays + daysSelected[j][x].charAt(0) + daysSelected[j][x].charAt(1);
                }
                else{
                    theDays = theDays + daysSelected[j][x].charAt(0);
                }
                x++;
            }
            //EditText _start = findViewById(i+3);
            //EditText _end = findViewById(i+4);
            String text = _class.getText() + " " + _loc.getSelectedItem().toString() + " " + theDays + " " + timesSelected[j][0] + " " + timesSelected[j][1] + "\n";
            ManageFirestore.newClass(SignInStatus.UserName, text);
            i += 6;
            j++;
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) throws NullPointerException{
        boolean flag2 = true;
        if(Arrays.asList(_halls).contains(parent.getItemAtPosition(position))){
            flag2 = false;
        }
        //check time spinner, slice first 2 ints to check
        int i = 0;
        //new int to keep track of which array of days being accessed in dayArray array
        if(flag2){
            if(flag){
                while(i  < 7){
                    if(daysSelected[dNum][i] == null){
                        daysSelected[dNum][i] = parent.getItemAtPosition(position).toString();
                        break;
                    }
                    i++;
                }
                for(String x: daysSelected[dNum]){
                    System.out.println(x);
                }
            }
            else{
                flag = !flag;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void addTimePicker(){
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(InputScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10){
                    timesSelected[tNum][1] = String.valueOf(selectedHour)+":0"+String.valueOf(selectedMinute);
                }
                else{
                    timesSelected[tNum][1] = String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute);
                }
                System.out.println(timesSelected[tNum][1]);
            }
        }, 0, 0, true);//Yes 24 hour time
        mTimePicker.setTitle("Select End Time");
        mTimePicker.show();
        mTimePicker = new TimePickerDialog(InputScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10){
                    timesSelected[tNum][0] = String.valueOf(selectedHour)+":0"+String.valueOf(selectedMinute);
                }
                else{
                    timesSelected[tNum][0] = String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute);
                }
                System.out.println(timesSelected[tNum][0]);
            }
        }, 0, 0, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Start Time");
        mTimePicker.show();
    }
}
