package com.example.plattsmapnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=findViewById(R.id.drawer);

        toolbar=findViewById(R.id.toolbar);
        navigationView =findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.Profile:
                Toast.makeText(MainActivity.this, "Profile button selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.CampusMap:
                Intent i =new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.EntSched:
                Intent k =new Intent(MainActivity.this,InputScheduleActivity.class);
                startActivity(k);
                break;
            case R.id.ViewSched:
                Intent l = new Intent(MainActivity.this,ScheduleActivity.class);
                startActivity(l);
                break;

            case R.id.help:
                Intent j =new Intent(MainActivity.this, LogInActivity.class);
                startActivity(j);
                //Toast.makeText(MainActivity.this, "This will lead to a general help section", Toast.LENGTH_SHORT).show();
                break;

            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                //Intent k =new Intent(MainActivity.this, MapsActivity.class);
                //startActivity(k);
                Toast.makeText(MainActivity.this, "You are signed out", Toast.LENGTH_SHORT).show();
                break;

        }
        return false;
    }
}
