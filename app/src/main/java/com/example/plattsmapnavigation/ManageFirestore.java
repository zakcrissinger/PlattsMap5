package com.example.plattsmapnavigation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ManageFirestore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static List<String> classList;
    private List<String> newList;

    public static void newUser(String userName){
        Map<String, Object> name = new HashMap<>();
        name.put("username", userName.split("@")[0]);
        db.collection("users").document(userName)
                .set(name);
    }
    public static void newClass(String userName, String classInfo){
        String[] info = classInfo.split(" ");
        Map<String, Object> _class = new HashMap<>();
        _class.put("class", info[0]);
        _class.put("location", info[1]);
        _class.put("start", info[2]);
        _class.put("end", info[3]);
        db.collection("users").document(userName).collection("classes")
                .document().set(_class);
    }

}
