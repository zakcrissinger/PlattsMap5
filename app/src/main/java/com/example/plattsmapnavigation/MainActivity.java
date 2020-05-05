package com.example.plattsmapnavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Menu menu;
    public TextView welcomeMessage, nextClass;
    List<List<String>> allClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        navigationView =findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        welcomeMessage = findViewById(R.id.welcome);
        nextClass = findViewById(R.id.nextclass);
        AdjustSignInState(menu);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //fixGoogleMapBug();
        welcomeMessage.setText(updateWelcomeMessage());
        if(!SignInStatus.SignedIn){
            nextClass.setText("Sign in to see your next class!");
        }
        else{
            getClasses();
        }
        nextClass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(SignInStatus.NextClassLocation != null){
                    System.out.println(SignInStatus.NextClassLocation);
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void fixGoogleMapBug() {
        SharedPreferences googleBug = getSharedPreferences("google_bug", Context.MODE_PRIVATE);
        if (!googleBug.contains("fixed")) {
            File corruptedZoomTables = new File(getFilesDir(), "ZoomTables.data");
            corruptedZoomTables.delete();
            googleBug.edit().putBoolean("fixed", true).apply();
        }
    }
    public void AdjustSignInState(Menu menu){
        if (SignInStatus.SignedIn){
            menu.findItem(R.id.signin).setVisible(false);
            menu.findItem(R.id.signout).setVisible(true);
            menu.findItem(R.id.EntSched).setVisible(true);
        }
        else{
            menu.findItem(R.id.signout).setVisible(false);
            menu.findItem(R.id.signin).setVisible(true);
            menu.findItem(R.id.EntSched).setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.CampusMap:
                Intent i =new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.EntSched:
                Intent k =new Intent(MainActivity.this,InputScheduleActivity.class);
                startActivity(k);
                break;
            case R.id.ViewSched:
                if (SignInStatus.SignedIn == false){
                    Toast.makeText(MainActivity.this, "You must be signed in to view schedule.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent l = new Intent(MainActivity.this,ScheduleActivity.class);
                    startActivity(l);
                }
                break;

            case R.id.signin:
                Intent j =new Intent(MainActivity.this, LogInActivity.class);
                startActivity(j);
                break;

            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                SignInStatus.SignedIn = false;
                AdjustSignInState(menu);
                Intent m =new Intent(MainActivity.this, LogInActivity.class);
                startActivity(m);
                break;

        }
        return false;
    }

    private String getTimeOfDay(){
        int hour = hourAndMinutes()[0];
        if(hour < 12){
            return "Good Morning, ";
        }
        else if(hour >= 12 & hour < 17){
            return "Good Afternoon, ";
        }
        else{
            return "Good Evening, ";
        }
    }
    private int[] hourAndMinutes(){
        String time = Calendar.getInstance().getTime().toString();
        int i = 0;
        char[] now = new char[8];
        while(i < time.length()){
            if(time.charAt(i+2)==(':')){
                int j = 0;
                while(j < 5){
                    now[j] = time.charAt(i);
                    i++;
                    j++;
                }
                break;
            }
            i++;
        }
        int hour = Character.getNumericValue(now[0])*10;
        hour = hour + Character.getNumericValue(now[1]);
        int minute = Character.getNumericValue(now[3])*10;
        minute = minute + Character.getNumericValue(now[4]);
        int[] times = new int[2];
        times[0] = hour;
        times[1] = minute;
        return(times);
    }
    private String getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch(day){
            case 1:
                return("Sun");
            case 2:
                return("M");
            case 3:
                return("T");
            case 4:
                return("W");
            case 5:
                return("Th");
            case 6:
                return("F");
            case 7:
                return("Sat");
        }
    return null;
    }
    private String updateWelcomeMessage(){
        if(SignInStatus.SignedIn){
            String str = SignInStatus.UserName.split("@")[0];
            String name = str.substring(0, 1).toUpperCase() + str.substring(1);
            return(getTimeOfDay()+name+".");
        }
        else{
            return("Welcome to PlattsMap!");
        }
    }
    private void sendClasses(List<List<String>> list){
        //nextClass.setText(updateNextClassMessage());
        int[] times = hourAndMinutes();
        char day = getDayOfWeek().toCharArray()[0];
        int timeGap = 100000000;
        int i = 0, theClass = 0;
        for(List<String> cls: list){
            int j = 0;
            while(j < cls.get(2).length()){
                //if(day == 'T'){
                //
               // }
                if(day == cls.get(2).charAt(j)){
                    int classHour;
                    if(cls.get(3).length() == 4){
                        classHour = Character.getNumericValue(cls.get(3).charAt(0));
                    }
                    else{
                        classHour = Character.getNumericValue(cls.get(3).charAt(0))*10;
                        classHour = classHour + Character.getNumericValue(cls.get(3).charAt(1));
                    }
                    int classMin = Character.getNumericValue(cls.get(3).charAt(0))*10;
                    classMin = classMin + Character.getNumericValue(cls.get(3).charAt(1));
                    int now = times[0] + times[1]/60;
                    int classTime = classHour + classMin/60;
                    if(classTime - now > 0 & classTime - now < timeGap){
                        timeGap = classTime - now;
                        theClass = i;
                    }
                }
                j++;
            }
            i++;
        }
        if(timeGap == 100000000){
            nextClass.setText("No more classes for the day!");
        }
        else{
            List<String> closeClass = list.get(theClass);
            String message = "Your next class is " + closeClass.get(0) + " in " + closeClass.get(1) + " Hall at " + closeClass.get(3);
            SignInStatus.NextClassLocation = closeClass.get(1)+" Hall";
            nextClass.setText(message);
        }


    }


    private String getClasses() {
        Query qClasses;
        List<List<String>> classes = new ArrayList<>();
        try {
            qClasses = db.collection("users").document(SignInStatus.UserName).collection("classes");
        }
        catch (Exception NullPointerException){
            return("Enter a schedule to see your upcoming classes!");
        }

        qClasses.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> list = new ArrayList<>();
                            list.add(document.getString("class"));
                            list.add(document.getString("location"));
                            list.add(document.getString("start"));
                            list.add(document.getString("end"));
                            list.add(document.getString("days"));
                            classes.add(list);
                        }
                    }
                    sendClasses(classes);
                }
            });
        return null;
        }
    }
