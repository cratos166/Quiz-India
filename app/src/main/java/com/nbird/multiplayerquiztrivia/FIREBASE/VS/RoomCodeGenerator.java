package com.nbird.multiplayerquiztrivia.FIREBASE.VS;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RoomCodeGenerator {


    public int start(){
        Random random=new Random();
        int int_random = random.nextInt(980000000)+10000000;
        return int_random;
    }

}
