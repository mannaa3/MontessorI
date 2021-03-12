package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText userName,userprofName,userStatus,userCountry,userGender,userRelation,userDOB;
    private Button UpdateAccountSettingButton;
    private CircleImageView userprofileImage;
    private DatabaseReference settingUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID,page_flag="0";
    final static  int gallery_pick=1;
    private ProgressDialog loaddingBar;
    private StorageReference UserProfilrImageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        loaddingBar=new ProgressDialog(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               //  page_flag= null;
            } else {
               //  page_flag= extras.getString("page_flag");
            }
        } else {
            // page_flag= (String) savedInstanceState.getSerializable("page_flag");
        }
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        settingUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfilrImageRef= FirebaseStorage.getInstance().getReference().child("profileimages");

        mToolbar=(Toolbar) findViewById(R.id.setting_toobar);
        mToolbar.setTitle("إعدادات الحساب الشخصي ");
        setSupportActionBar(mToolbar);
        userName=(EditText)findViewById(R.id.setting_username);
        userprofName=(EditText)findViewById(R.id.setting_profile_full_name);
        userStatus=(EditText)findViewById(R.id.setting_status);
        userCountry=(EditText)findViewById(R.id.setting_profile_country);
        userGender=(EditText)findViewById(R.id.setting_gender);
        userRelation=(EditText)findViewById(R.id.setting_relationship_status);
        userDOB=(EditText)findViewById(R.id.setting_dob);
        userprofileImage=(CircleImageView)findViewById(R.id.setting_profile_image);
        UpdateAccountSettingButton=(Button)findViewById(R.id.update_account_setting_button);
        settingUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    String myprofileImage=datasnapshot.child("profileimage").getValue().toString();
                    String myUserName=datasnapshot.child("username").getValue().toString();
                    String myprofileName=datasnapshot.child("fullname").getValue().toString();
                    String myprofileStatus=datasnapshot.child("staus").getValue().toString();
                    String myDOB=datasnapshot.child("dop").getValue().toString();
                    String myCountry=datasnapshot.child("country").getValue().toString();
                    String myGender=datasnapshot.child("gender").getValue().toString();
                    String myRelationStatuse=datasnapshot.child("relationshipstatus").getValue().toString();
                    Picasso.with(SettingActivity.this).load(myprofileImage).placeholder(R.drawable.profile).into(userprofileImage) ;
                    userName.setText(myUserName);
                    userprofName.setText(myprofileName);
                    userStatus.setText(myprofileStatus);
                    userDOB.setText(myDOB);
                    userCountry.setText(myCountry);
                    userGender.setText(myGender);
                    userRelation.setText(myRelationStatuse);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UpdateAccountSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatAccountInfo();
            }
        });
        userprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,gallery_pick);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallery_pick&&resultCode==RESULT_OK&&data!=null){
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
                loaddingBar.setCanceledOnTouchOutside(true);
                loaddingBar.show();

                Uri resultUri =result.getUri();
                final StorageReference filepath=UserProfilrImageRef.child(currentUserID+".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        Toast.makeText(SettingActivity.this,"profile image stord succfuly",Toast.LENGTH_LONG).show();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                settingUserRef.child("profileimage").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    Intent setupIntent=new Intent(SettingActivity.this,SettingActivity.class);
                                                    startActivity(setupIntent);
                                                    Toast.makeText(SettingActivity.this,"profilr images stord firbas databse succfuly",Toast.LENGTH_LONG).show();
                                                    loaddingBar.dismiss();
                                                }else {
                                                    String message =task.getException().getMessage();
                                                    Toast.makeText(SettingActivity.this,"   Error Occuer"+message,Toast.LENGTH_LONG).show();
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
                Toast.makeText(SettingActivity.this,"   Error croping image not ",Toast.LENGTH_LONG).show();
                loaddingBar.dismiss();



            }
        }
*/
    }

    private void ValidatAccountInfo() {

        String username=userName.getText().toString();
        String profilename=userprofName.getText().toString();
        String status=userStatus.getText().toString();
        String dob=userDOB.getText().toString();
        String country=userCountry.getText().toString();
        String gender=userGender.getText().toString();
        String relation=userRelation.getText().toString();
        if(TextUtils.isEmpty(username)){

            Toast.makeText(this,"plz wite username..",Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(status)){

            Toast.makeText(this,"plz wite username..",Toast.LENGTH_LONG).show();
        }
        else  if (TextUtils.isEmpty(profilename)){

            Toast.makeText(this,"plz wite username..",Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(status)){

            Toast.makeText(this,"plz wite username..",Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(dob)){

            Toast.makeText(this,"plz wite dob..",Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(country)){

            Toast.makeText(this,"plz wite country..",Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(gender)){

            Toast.makeText(this,"plz wite gender..",Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(relation)){

            Toast.makeText(this,"plz wite Relation..",Toast.LENGTH_LONG).show();
        }else {
            loaddingBar.setTitle("Profile Image");
            loaddingBar.setMessage("Please Wait ,Updataing your profile Info.. ");
            loaddingBar.setCanceledOnTouchOutside(true);
            loaddingBar.show();

            UpdateAccountinfo(username,profilename,status,dob,country,gender,relation);
        }
    }

    private void UpdateAccountinfo(String username, String profilename, String status, String dob, String country, String gender, String relation) {

        HashMap userMap=new HashMap();
        userMap.put("username",username);
        userMap.put("fullname",profilename);
        userMap.put("staus",status);
        userMap.put("dop",dob);
        userMap.put("country",country);
        userMap.put("gender",gender);
        userMap.put("relationshipstatus",relation);
        settingUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful()){
                    Toast.makeText(SettingActivity.this,"Account Setting Update Succesfully..",Toast.LENGTH_LONG).show();
                    loaddingBar.dismiss();


                }
                else {
                    Toast.makeText(SettingActivity.this,"Error occured..",Toast.LENGTH_LONG).show();
                    loaddingBar.dismiss();

                }
            }
        });

    }

    private void SendUserToMainActivity() {

        Intent mainIntent=new Intent(SettingActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
}
