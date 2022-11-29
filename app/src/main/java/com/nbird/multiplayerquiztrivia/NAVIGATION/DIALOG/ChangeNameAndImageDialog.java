package com.nbird.multiplayerquiztrivia.NAVIGATION.DIALOG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChangeNameAndImageDialog {

    String image;
    String username;
    Context context;
    View v;
    TextView usernameTextView;
    ImageView userImage;


    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    CardView cardView11, cardView12, cardView13, cardView14, cardView15, cardView16, cardView17, cardView18, cardView19, cardView20;
    CardView cardView21, cardView22, cardView23, cardView24, cardView25, cardView26;

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7, linearLayout8, linearLayout9, linearLayout10;
    LinearLayout linearLayout11, linearLayout12, linearLayout13, linearLayout14, linearLayout15, linearLayout16, linearLayout17, linearLayout18, linearLayout19, linearLayout20;
    LinearLayout linearLayout21, linearLayout22, linearLayout23, linearLayout24, linearLayout25, linearLayout26;

    ImageView nav_image;

    TextInputEditText usernameEditText;
    AppData appData;
    Uri imageUri;

    String urlAva1 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava1.png?alt=media&token=39fc4486-0021-443f-974d-daa3fc17bec2";
    String urlAva2 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava2.png?alt=media&token=e19cd95b-6012-4fe7-94bb-003c1b9f92c0";
    String urlAva3 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava3.png?alt=media&token=d0a90643-c6f3-446e-a386-8de3dc052765";
    String urlAva4 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava4.png?alt=media&token=ff5f2d52-eb31-4d55-af59-2de2ecda6a51";
    String urlAva5 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava5.png?alt=media&token=6a354ce9-d45a-4bfa-8c26-6d1db00ceb84";
    String urlAva6 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava6.png?alt=media&token=445f480e-c646-45b1-b55c-8ff86469e97f";
    String urlAva7 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava7.png?alt=media&token=41f409b7-6c19-4827-9723-fbd8856215f2";
    String urlAva8 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava8.png?alt=media&token=4a3682cc-3242-4582-b3c3-fff6591b7af5";
    String urlAva9 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava9.png?alt=media&token=f9048bb1-fd25-48fe-95ed-65f70f0607b3";
    String urlAva10 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava10.png?alt=media&token=881f92b8-973a-462a-8a3c-8c6f70cd888b";
    String urlAva11 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava11.png?alt=media&token=26a5711e-2a8c-449e-bc3a-4a2d98811cc1";
    String urlAva12 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava12.png?alt=media&token=e09e3572-c79d-4bb3-9756-29f18967f0db";
    String urlAva13 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava13.png?alt=media&token=82a5dece-0e9a-4e2c-824a-1c4b441073bc";
    String urlAva14 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava14.png?alt=media&token=bb2e7b65-2709-432b-add4-cac32bf3cd6d";
    String urlAva15 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava15.png?alt=media&token=fea2cbe9-2359-4577-a113-562e54341031";
    String urlAva16 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava16.png?alt=media&token=f31d2368-92f4-4e82-a384-aaabf11c6dc3";
    String urlAva17 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava17.png?alt=media&token=4d7eacfd-d75e-432d-bf6d-bc95acdb3ebf";
    String urlAva18 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava18.png?alt=media&token=2e1493eb-905a-47cc-b86c-0540d5a7561c";
    String urlAva19 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava19.png?alt=media&token=faa84af3-2195-46b7-a44f-d6b272698443";
    String urlAva20 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava20.png?alt=media&token=b7aee3f8-a774-4ceb-9be9-4bd4dba39b04";
    String urlAva21 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava21.png?alt=media&token=d32fccfd-57c3-4f07-99c6-0da9cc921d5e";
    String urlAva22 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava22.png?alt=media&token=37793d6e-55bf-4be6-9c3e-b49f69cb25f8";
    String urlAva23 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava23.png?alt=media&token=c4fe300d-b6cb-4102-b956-082c89ed6bcb";
    String urlAva24 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava24.png?alt=media&token=35520f68-1000-4c84-b8ed-56b2bf64b14f";
    String urlAva25 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava25.png?alt=media&token=c2eec723-3f40-4dc6-a294-caa4db20cf66";
    String urlAva26 = "https://firebasestorage.googleapis.com/v0/b/mindscape-3a832.appspot.com/o/avatarIcons%2Fava26.png?alt=media&token=2cd731f2-e525-49b3-ac81-dc939cbd82fe";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");
    DatabaseReference myRef = database.getReference();

    FirebaseStorage storage;
    StorageReference storageReference;

    String mailid123;
    AppString appString;

    public ChangeNameAndImageDialog(String image, String username, Context context, View v, TextView usernameTextView, ImageView userImage) {
        this.image = image;
        this.username = username;
        this.context = context;
        this.v = v;
        this.usernameTextView=usernameTextView;
        this.userImage=userImage;
    }

    public void start(){



        mAuth = FirebaseAuth.getInstance();


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_profile_selection, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);
        ((TextView) view1.findViewById(R.id.textTitle)).setText(" Enter Username ");
        ((Button) view1.findViewById(R.id.buttonYes)).setText("OK");
        ((TextView) view1.findViewById(R.id.textView6)).setText("Select An Avatar Or Upload Your Pic ");
        nav_image = ((ImageView) view1.findViewById(R.id.propic));
        usernameEditText = ((TextInputEditText) view1.findViewById(R.id.username));

        cardView1 = (CardView) view1.findViewById(R.id.cardview1);
        cardView2 = (CardView) view1.findViewById(R.id.cardview2);
        cardView3 = (CardView) view1.findViewById(R.id.cardview3);
        cardView4 = (CardView) view1.findViewById(R.id.cardview4);
        cardView5 = (CardView) view1.findViewById(R.id.cardview5);
        cardView6 = (CardView) view1.findViewById(R.id.cardview6);
        cardView7 = (CardView) view1.findViewById(R.id.cardview7);
        cardView8 = (CardView) view1.findViewById(R.id.cardview8);
        cardView9 = (CardView) view1.findViewById(R.id.cardview9);
        cardView10 = (CardView) view1.findViewById(R.id.cardview10);
        cardView11 = (CardView) view1.findViewById(R.id.cardview11);
        cardView12 = (CardView) view1.findViewById(R.id.cardview12);
        cardView13 = (CardView) view1.findViewById(R.id.cardview13);
        cardView14 = (CardView) view1.findViewById(R.id.cardview14);
        cardView15 = (CardView) view1.findViewById(R.id.cardview15);
        cardView16 = (CardView) view1.findViewById(R.id.cardview16);
        cardView17 = (CardView) view1.findViewById(R.id.cardview17);
        cardView18 = (CardView) view1.findViewById(R.id.cardview18);
        cardView19 = (CardView) view1.findViewById(R.id.cardview19);
        cardView20 = (CardView) view1.findViewById(R.id.cardview20);
        cardView21 = (CardView) view1.findViewById(R.id.cardview21);
        cardView22 = (CardView) view1.findViewById(R.id.cardview22);
        cardView23 = (CardView) view1.findViewById(R.id.cardview23);
        cardView24 = (CardView) view1.findViewById(R.id.cardview24);
        cardView25 = (CardView) view1.findViewById(R.id.cardview25);
        cardView26 = (CardView) view1.findViewById(R.id.cardview26);


        linearLayout1 = (LinearLayout) view1.findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) view1.findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) view1.findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) view1.findViewById(R.id.linearLayout4);
        linearLayout5 = (LinearLayout) view1.findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) view1.findViewById(R.id.linearLayout6);
        linearLayout7 = (LinearLayout) view1.findViewById(R.id.linearLayout7);
        linearLayout8 = (LinearLayout) view1.findViewById(R.id.linearLayout8);
        linearLayout9 = (LinearLayout) view1.findViewById(R.id.linearLayout9);
        linearLayout10 = (LinearLayout) view1.findViewById(R.id.linearLayout10);
        linearLayout11 = (LinearLayout) view1.findViewById(R.id.linearLayout11);
        linearLayout12 = (LinearLayout) view1.findViewById(R.id.linearLayout12);
        linearLayout13 = (LinearLayout) view1.findViewById(R.id.linearLayout13);
        linearLayout14 = (LinearLayout) view1.findViewById(R.id.linearLayout14);
        linearLayout15 = (LinearLayout) view1.findViewById(R.id.linearLayout15);
        linearLayout16 = (LinearLayout) view1.findViewById(R.id.linearLayout16);
        linearLayout17 = (LinearLayout) view1.findViewById(R.id.linearLayout17);
        linearLayout18 = (LinearLayout) view1.findViewById(R.id.linearLayout18);
        linearLayout19 = (LinearLayout) view1.findViewById(R.id.linearLayout19);
        linearLayout20 = (LinearLayout) view1.findViewById(R.id.linearLayout20);
        linearLayout21 = (LinearLayout) view1.findViewById(R.id.linearLayout21);
        linearLayout22 = (LinearLayout) view1.findViewById(R.id.linearLayout22);
        linearLayout23 = (LinearLayout) view1.findViewById(R.id.linearLayout23);
        linearLayout24 = (LinearLayout) view1.findViewById(R.id.linearLayout24);
        linearLayout25 = (LinearLayout) view1.findViewById(R.id.linearLayout25);
        linearLayout26 = (LinearLayout) view1.findViewById(R.id.linearLayout26);


        avaMunupulator(cardView1, urlAva1, linearLayout1);
        avaMunupulator(cardView2, urlAva2, linearLayout2);
        avaMunupulator(cardView3, urlAva3, linearLayout3);
        avaMunupulator(cardView4, urlAva4, linearLayout4);
        avaMunupulator(cardView5, urlAva5, linearLayout5);
        avaMunupulator(cardView6, urlAva6, linearLayout6);
        avaMunupulator(cardView7, urlAva7, linearLayout7);
        avaMunupulator(cardView10, urlAva10, linearLayout10);
        avaMunupulator(cardView8, urlAva8, linearLayout8);
        avaMunupulator(cardView9, urlAva9, linearLayout9);
        avaMunupulator(cardView11, urlAva11, linearLayout11);
        avaMunupulator(cardView12, urlAva12, linearLayout12);
        avaMunupulator(cardView13, urlAva13, linearLayout13);
        avaMunupulator(cardView14, urlAva14, linearLayout14);
        avaMunupulator(cardView15, urlAva15, linearLayout15);
        avaMunupulator(cardView16, urlAva16, linearLayout16);
        avaMunupulator(cardView17, urlAva17, linearLayout17);
        avaMunupulator(cardView18, urlAva18, linearLayout18);
        avaMunupulator(cardView19, urlAva19, linearLayout19);
        avaMunupulator(cardView20, urlAva20, linearLayout20);
        avaMunupulator(cardView21, urlAva21, linearLayout21);
        avaMunupulator(cardView22, urlAva22, linearLayout22);
        avaMunupulator(cardView23, urlAva23, linearLayout23);
        avaMunupulator(cardView24, urlAva24, linearLayout24);
        avaMunupulator(cardView25, urlAva25, linearLayout25);
        avaMunupulator(cardView26, urlAva26, linearLayout26);


        appData = new AppData();
        mailid123 = appData.getSharedPreferencesString(appString.SP_MAIN, appString.SP_MY_MAIL, context);
        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        usernameEditText.setText(username);


        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }


        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:


                            if (username()) {
                                String usernameEntered = usernameEditText.getText().toString();



                                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("userName").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                        appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,context,usernameEntered);
                                        usernameTextView.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,context));
                                    }
                                });
                                try {
                                    alertDialog.dismiss();
                                } catch (Exception e) {
                                }
                            }
                    }
                }
                return false;
            }
        });

        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (username()) {
                    String usernameEntered = usernameEditText.getText().toString();

                    myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("userName").setValue(usernameEntered).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,context,usernameEntered);

                            usernameTextView.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,context));

                        }
                    });
                    try {
                        alertDialog.dismiss();
                    } catch (Exception e) {
                    }
                }

            }
        });
    }


    public void avaMunupulator(CardView cardView, String urlAva, LinearLayout linearLayout) {

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settingBlack();
                linearLayout.setBackgroundResource(R.drawable.double_color_2);
                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("imageURL").setValue(urlAva).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context,urlAva);
                            userImage.setImageResource(0);
                            Glide.with(context).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context)).apply(RequestOptions
                                            .bitmapTransform(new RoundedCorners(18)))
                                    .into(userImage);
                        } else {

                        }
                    }
                });
            }
        });


    }

    private boolean username() {
        String name1 = usernameEditText.getText().toString();
        //String noWhihteSpaces=("\\A\\w{4,20}\\z");
        if (name1.isEmpty()) {
            usernameEditText.setError("Field cannot be empty");
            return false;
        } else if (name1.length() > 10) {
            usernameEditText.setError("Username should be less than 10 characters");
            return false;
        } else
            usernameEditText.setError(null);
        return true;
    }


    public void settingBlack() {
        linearLayout1.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout2.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout3.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout4.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout5.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout6.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout7.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout8.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout9.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout10.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout11.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout12.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout13.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout14.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout15.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout16.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout17.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout18.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout19.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout20.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout21.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout22.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout23.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout24.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout25.setBackgroundResource(R.drawable.border_theme_2);
        linearLayout26.setBackgroundResource(R.drawable.border_theme_2);
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

               /* if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } */
                if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) context).startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }



    private void uploadImage() {


        if (imageUri != null) {

            try {
                settingBlack();
            } catch (Exception e) {

            }


            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            storageReference = storageReference.child("NEW_APP/images/" + mailid123);


            // adding listeners on upload
            // or failure of image
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    try {

                                        StorageReference urlref = storageReference;
                                        urlref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUrl) {
                                                String imageurl = downloadUrl.toString();

                                                myRef.child("NEW_APP").child("User").child(mAuth.getCurrentUser().getUid()).child("personal").child("imageURL").setValue(imageurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            appData.setSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context,imageurl);
                                                            userImage.setImageResource(0);
                                                            Glide.with(context).load(imageurl).apply(RequestOptions
                                                                            .bitmapTransform(new RoundedCorners(18)))
                                                                    .into(userImage);

                                                        } else {

                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    } catch (Exception e) {

                                    }
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(context,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

}
