package com.nbird.multiplayerquiztrivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.Model.FirstTime;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=1;
    AppData appData;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRequest();
        appData=new AppData();
       

        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            signupDialog(this);

        }

    }

    public void signupDialog(Context context){

        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_sign_up,(ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        SignInButton googsignin=(SignInButton) view1.findViewById(R.id.googlesignin);


        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        googsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog loadingDialog=new Dialog(context);
                loadingDialog.setContentView(R.layout.loading_screen);
                loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                loadingDialog.setCancelable(true);
                loadingDialog.show();
                signIn();
            }
        });
    }


    public void createRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            String personEmail = account.getEmail();
                            appData.setSharedPreferencesString(AppString.SP_MAIN, AppString.SP_MY_MAIL,MainActivity.this,personEmail);

                            table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("firstTime").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        if(snapshot.getValue(Integer.class)==1){
                                            Toast.makeText(getBaseContext(), "Logged In Successful!", Toast.LENGTH_LONG).show();
                                            loadingDialog.dismiss();
                                            // NOT FOR FIRST TIME
                                        }

                                    } catch (Exception e) {
                                        //  FOR FIRST TIME
                                            Random rand = new Random();

                                            final int setNumber = rand.nextInt(95000)+1;
                                            String playerName="Player"+setNumber;

                                            FirstTime firstTime=new FirstTime(AppString.IMAGE_URL,playerName,1,0);

                                            table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("personal").setValue(firstTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getBaseContext(), "Logged In Successful!", Toast.LENGTH_LONG).show();
                                                        loadingDialog.dismiss();
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Record Not Saved!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {

                            Toast.makeText(getBaseContext(), "Error!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            loadingDialog.dismiss();

                        }
                    }
                });
    }

}