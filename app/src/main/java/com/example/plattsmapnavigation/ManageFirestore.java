package com.example.plattsmapnavigation;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        _class.put("days", info[4]);
        db.collection("users").document(userName).collection("classes")
                .document().set(_class);
    }

}
