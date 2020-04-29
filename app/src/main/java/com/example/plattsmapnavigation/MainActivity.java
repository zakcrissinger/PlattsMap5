package com.example.plattsmapnavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.view.Menu;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       System.out.println(getTimeOfDay());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        navigationView =findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        AdjustSignInState(menu);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        fixGoogleMapBug();

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
                break;

        }
        return false;
    }

    public String getTimeOfDay(){
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
    public String getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch(day){
            case 1:
                return("Sunday");
            case 2:
                return("Monday");
            case 3:
                return("Tuesday");
            case 4:
                return("Wednesday");
            case 5:
                return("Thursday");
            case 6:
                return("Firday");
            case 7:
                return("Saturday");
        }
    return null;
    }
}
