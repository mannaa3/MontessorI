package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
/*
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;*/

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity {
    private EditText UserName,FullName,setup_mobile_number,setup_address;
    private Button SaveInfomationbutton;
    private CircleImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,paymentRef;
    String currentUserId;
    private ProgressDialog loaddingBar;
    final static  int gallery_pick=1234;
    Uri selectedImage=null;
    private StorageReference UserProfilrImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        loaddingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        UserName=(EditText)findViewById(R.id.setup_userName);
        FullName=(EditText)findViewById(R.id.setup_full_userName);
        setup_address=(EditText)findViewById(R.id.setup_address);
        setup_mobile_number=(EditText)findViewById(R.id.setup_mobile_number);
        profileImage=(CircleImageView) findViewById(R.id.setup_profile_image);
        SaveInfomationbutton =(Button)findViewById(R.id.setup_information_button);

        currentUserId=mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        paymentRef=FirebaseDatabase.getInstance().getReference().child("payment").child(currentUserId);

        UserProfilrImageRef= FirebaseStorage.getInstance().getReference().child("profileimages");


        SaveAccountsetuoInformationTemp();
        SaveInfomationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountsetuoInformation();

            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

              /*  Intent galleryIntent= new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,gallery_pick);*/
            }
        });
/*
               UserRef.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()){
                       if (dataSnapshot.hasChild("profileimage")) {
                           String image = dataSnapshot.child("profileimage").getValue().toString();
                            Glide.with(SetUpActivity.this).load(image).into(profileImage);

                           // Picasso.with(SetUpActivity.this).load(image).placeholder(R.drawable.profile).into(profileImage);
                       }else {
                           Toast.makeText(SetUpActivity.this, "pleas selcet profile image first", Toast.LENGTH_LONG).show();


                       }
                   }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
*/



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                      selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    profileImage.setImageURI(selectedImage);
                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                }else {
                    Toast.makeText(SetUpActivity.this,"لم يتم اختيار الصوره",Toast.LENGTH_LONG).show();
                }
        }
         /*if(requestCode==gallery_pick&&resultCode==RESULT_OK&&data!=null){
            Uri imageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                loaddingBar.setTitle("Profile Image");
                loaddingBar.setMessage("Please Wait ,Updataing your profile imge.. ");
                loaddingBar.show();
                loaddingBar.setCanceledOnTouchOutside(true);

                Uri resultUri =result.getUri();
                final StorageReference filepath=UserProfilrImageRef.child(currentUserId+".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            Toast.makeText(SetUpActivity.this,"profile image stord succfuly",Toast.LENGTH_LONG).show();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                UserRef.child("profileimage").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    Intent setupIntent=new Intent(SetUpActivity.this,SetUpActivity.class);
                                                    startActivity(setupIntent);
                                                    Toast.makeText(SetUpActivity.this,"profilr images stord firbas databse succfuly",Toast.LENGTH_LONG).show();
                                                    loaddingBar.dismiss();
                                                }else {
                                                    String message =task.getException().getMessage();
                                                    Toast.makeText(SetUpActivity.this,"   Error Occuer"+message,Toast.LENGTH_LONG).show();
                                                    loaddingBar.dismiss();

                                                }
                                            }
                                        });
                                // complete the rest of your code
                            }
                        });

                    }
                });
            }else {
                 Toast.makeText(SetUpActivity.this,"   Error croping image not ",Toast.LENGTH_LONG).show();
                loaddingBar.dismiss();



            }
        }
        */
    }

    private void SaveAccountsetuoInformationTemp() {
        String username ="لم يتم ادخال بيانات";
        String fullname ="لم يتم ادخال بيانات";
        String mobile_number ="لم يتم ادخال بيانات";
        String address ="لم يتم ادخال بيانات";

            HashMap userMap=new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("mobile",mobile_number);
            userMap.put("address",address);
            userMap.put("staus","لم يتم ادخال بيانات");
            userMap.put("gender","لم يتم ادخال بيانات");
            userMap.put("dop","لم يتم ادخال بيانات");
            userMap.put("relationshipstatus","لم يتم ادخال بيانات");
            userMap.put("flag","1");
            userMap.put("uid",mAuth.getCurrentUser().getUid());
            userMap.put("email",mAuth.getCurrentUser().getEmail());
            userMap.put("profileimage","https://firebasestorage.googleapis.com/v0/b/poster-69d52.appspot.com/o/profileimages%2FbRq5IgWofmSQI4mn7gEVBOnZyEu2.jpg?alt=media&token=a81c12a3-d515-4682-a683-120a5fe2f120");

            HashMap userpaymentMap=new HashMap();
            userpaymentMap.put("mon1","false");
            userpaymentMap.put("mon2","false");
            userpaymentMap.put("mon3","false");
            userpaymentMap.put("mon4","false");
            userpaymentMap.put("mon5","false");
            userpaymentMap.put("mon6","false");
            userpaymentMap.put("mon7","false");
            userpaymentMap.put("mon8","false");
            userpaymentMap.put("mon9","false");
            userpaymentMap.put("mon10","false");
            userpaymentMap.put("mon11","false");
            userpaymentMap.put("mon12","false");
            userpaymentMap.put("bus1","false");
            userpaymentMap.put("bus2","false");
            userpaymentMap.put("bus3","false");
            userpaymentMap.put("bus4","false");
            userpaymentMap.put("bus5","false");
            userpaymentMap.put("bus6","false");
            userpaymentMap.put("bus7","false");
            userpaymentMap.put("bus8","false");
            userpaymentMap.put("bus9","false");
            userpaymentMap.put("bus10","false");
            userpaymentMap.put("bus11","false");
            userpaymentMap.put("bus12","false");


            paymentRef.updateChildren(userpaymentMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){


                    }else {
                        String message =task.getException().getMessage();
                        Toast.makeText(SetUpActivity.this,"حدث خطأ "+message,Toast.LENGTH_LONG).show();


                    }
                }
            });



            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){

                    }else {
                        String message =task.getException().getMessage();


                    }
                }
            });

/*********************************************/



        }
    private void SaveAccountsetuoInformation() {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String mobile_number = setup_mobile_number.getText().toString();
        String address = setup_address.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this,"يرجي كتابة اسم الطفل",Toast.LENGTH_LONG).show();


        } else if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(this,"يرجي كتاية اسم الطفل بالكامل",Toast.LENGTH_LONG).show();


        } else if (TextUtils.isEmpty(mobile_number)) {
            Toast.makeText(this,"يرجي كتاية رقم الموبيل ",Toast.LENGTH_LONG).show();


        }else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this,"يرجي كتابة العنوان",Toast.LENGTH_LONG).show();


        }else {
            loaddingBar.setTitle("حفظ البيانات");
            loaddingBar.setMessage("يرجي الإنتظار , سنقوم تحويلك تلقائي للفحة الرئسية");
            loaddingBar.show();
            loaddingBar.setCanceledOnTouchOutside(true);

            HashMap userMap=new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("mobile",mobile_number);
            userMap.put("address",address);
            userMap.put("staus","Hi there , i am psoter social network develoed by codeecafe");
            userMap.put("gender","none");
            userMap.put("dop","none");
            userMap.put("relationshipstatus","none");
            userMap.put("flag","1");
            userMap.put("uid",mAuth.getCurrentUser().getUid());
            userMap.put("email",mAuth.getCurrentUser().getEmail());
            userMap.put("profileimage","https://firebasestorage.googleapis.com/v0/b/poster-69d52.appspot.com/o/profileimages%2FbRq5IgWofmSQI4mn7gEVBOnZyEu2.jpg?alt=media&token=a81c12a3-d515-4682-a683-120a5fe2f120");

            HashMap userpaymentMap=new HashMap();
            userpaymentMap.put("mon1","false");
            userpaymentMap.put("mon2","false");
            userpaymentMap.put("mon3","false");
            userpaymentMap.put("mon4","false");
            userpaymentMap.put("mon5","false");
            userpaymentMap.put("mon6","false");
            userpaymentMap.put("mon7","false");
            userpaymentMap.put("mon8","false");
            userpaymentMap.put("mon9","false");
            userpaymentMap.put("mon10","false");
            userpaymentMap.put("mon11","false");
            userpaymentMap.put("mon12","false");
            userpaymentMap.put("bus1","false");
            userpaymentMap.put("bus2","false");
            userpaymentMap.put("bus3","false");
            userpaymentMap.put("bus4","false");
            userpaymentMap.put("bus5","false");
            userpaymentMap.put("bus6","false");
            userpaymentMap.put("bus7","false");
            userpaymentMap.put("bus8","false");
            userpaymentMap.put("bus9","false");
            userpaymentMap.put("bus10","false");
            userpaymentMap.put("bus11","false");
            userpaymentMap.put("bus12","false");


            paymentRef.updateChildren(userpaymentMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){


                    }else {
                        String message =task.getException().getMessage();
                        Toast.makeText(SetUpActivity.this,"حدث خطأ "+message,Toast.LENGTH_LONG).show();


                    }
                }
            });



            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){
                        Toast.makeText(SetUpActivity.this,"تم إنشاء الحساب بنجاح",Toast.LENGTH_LONG).show();

                    }else {
                        String message =task.getException().getMessage();
                        Toast.makeText(SetUpActivity.this,"حدث خطأ"+message,Toast.LENGTH_LONG).show();


                    }
                }
            });

/*********************************************/
            final StorageReference filepath=UserProfilrImageRef.child(currentUserId+".jpg");
            if (selectedImage == null){

                loaddingBar.dismiss();
                SendUserToLoginActivity();
            }else {
                filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                UserRef.child("profileimage").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    loaddingBar.dismiss();
                                                    SendUserToLoginActivity();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(SetUpActivity.this, "حدث خطأ" + message, Toast.LENGTH_LONG).show();
                                                    loaddingBar.dismiss();

                                                }
                                            }
                                        });
                                // complete the rest of your code
                            }
                        });

                    }
                });
            }
/*********************************************/



        }
    }

    @Override
    public void onBackPressed() {

        SendUserToLoginActivity();
    }
    private void SendUserToLoginActivity() {
        Intent LoginIntent=new Intent(this,LoginActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }
    private void SendUserToMainActivty() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
       // editor.putString("uid_SharedPreferences",UIDShared);
        editor.putString("page_flage_SharedPreferences","1");
        editor.apply();

        Intent mainIntent=new Intent(SetUpActivity.this,MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
    }
}
