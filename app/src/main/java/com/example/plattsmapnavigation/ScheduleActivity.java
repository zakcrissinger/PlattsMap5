package com.example.plattsmapnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ScheduleActivity extends AppCompatActivity{
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        TableLayout table = findViewById(R.id.table1);
        Query classes = db.collection("users").document(SignInStatus.UserName).collection("classes");
        List<String> list = new ArrayList<>();
        classes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //list.add();
                        list.add(document.getString("class"));
                        list.add(document.getString("location"));
                        list.add(document.getString("days"));
                        list.add(document.getString("start"));
                        list.add(document.getString("end"));
                    }
                    Log.d(TAG, list.toString());
                    if(list == null){
                        System.out.println("CLASS LIST IS 0 or NULL!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                    else{
                        SignInStatus.HasSched = true;
                        int j = 0;
                        int i = 0;
                        String[] str = new String[5];
                        while(i < list.size()){
                            str[i-j] = list.get(i);
                            if (((i+1) % 5) == 0){
                                addRow(table, str);
                                str = new String[5];
                                j = i+1;
                            }
                            i+=1;
                        }
                    }
                }
                 else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
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
            textField.setWidth(width/5);
            textField.setText(texts[i]);
            textField.setFreezesText(true);
            r1.addView(textField);
            i+=1;
        }
        table.addView(r1);

    }
}
