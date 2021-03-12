package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class Make_PostActivity extends AppCompatActivity {
    private ProgressDialog loaddingBar;
    private Toolbar mToolBar;
    private ImageButton SelectPostimage;
    private Button UpdatepostButton;
    private EditText PostDescription;
    private static final int gallery_pick = 1;
    private Uri selectedImage = null;

    private String Description, page_flag = "0";
    private StorageReference PostImagesRefrence;
    private DatabaseReference UserRef, PostRef;
    private FirebaseAuth mAuth;
    private String saveCurentData, saveCurentTime, postRandomName, downloadUrl, curent_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        loaddingBar = new ProgressDialog(this);
        PostImagesRefrence = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("posts");
        mAuth = FirebaseAuth.getInstance();
        curent_user_id = mAuth.getCurrentUser().getUid();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
               // page_flag = null;
            } else {
               // page_flag = extras.getString("page_flag");
            }
        } else {
          //  page_flag = (String) savedInstanceState.getSerializable("page_flag");
        }

        SelectPostimage = (ImageButton) findViewById(R.id.select_post_image);
        UpdatepostButton = (Button) findViewById(R.id.update_post_button);
        PostDescription = (EditText) findViewById(R.id.post_description);
        mToolBar = (Toolbar) findViewById(R.id.update_post__page_toolbar);
        mToolBar.setTitle("Update Post");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SelectPostimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        if (isNetworkAvailable(this)) {
            UpdatepostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidatePostInfo();

                }
            });
        } else {
            loaddingBar.setTitle("تنبية الاتصال");
            loaddingBar.setMessage("يرجي الاتصال بالانترنت");
            loaddingBar.show();
            loaddingBar.setCanceledOnTouchOutside(true);


        }

    }

    private void ValidatePostInfo() {
        Description = PostDescription.getText().toString();
        if (selectedImage == null && TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "يرجي كتابة شئ او ارفاق صوره", Toast.LENGTH_LONG).show();

        } else {
            loaddingBar.setTitle("جاري الحفظ");
            loaddingBar.setMessage("يرجي الانتظار ,جاري حفظ البيانات ");
            loaddingBar.show();
            loaddingBar.setCanceledOnTouchOutside(false);

            StoringImageToFirebaseStorage();

        }
    }

    private void StoringImageToFirebaseStorage() {


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurentData = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurentTime = currentTime.format(calForTime.getTime());
        postRandomName = saveCurentData + saveCurentTime;
        if (selectedImage == null) {
            SavaingPostInformationToDatabase();


        } else {
            StorageReference filepath = PostImagesRefrence.child("postimages").child(selectedImage.getLastPathSegment() + postRandomName + ".jpg");

            filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();

                    Toast.makeText(Make_PostActivity.this, "pImage uploade succfully in storge..", Toast.LENGTH_LONG).show();

                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            Toast.makeText(Make_PostActivity.this, "get download url." + downloadUrl, Toast.LENGTH_LONG).show();

                            SavaingPostInformationToDatabase();

                        }
                    });
                }
            });
        }

    }

    private void SavaingPostInformationToDatabase() {
        try {


            UserRef.child(curent_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userFullName = dataSnapshot.child("fullname").getValue().toString();
                        String userprofileImage = dataSnapshot.child("profileimage").getValue().toString();

                        if (selectedImage != null && TextUtils.isEmpty(Description)) {
                            HashMap postMap = new HashMap();
                            postMap.put("uid", curent_user_id);
                            postMap.put("data", saveCurentData);
                            postMap.put("time", saveCurentTime);
                            postMap.put("description", "" + Description);
                            postMap.put("uid", curent_user_id);
                            postMap.put("postimage", downloadUrl);
                            postMap.put("profileimage", userprofileImage);
                            postMap.put("fullname", userFullName);
                            PostRef.child(curent_user_id + postRandomName).updateChildren(postMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                loaddingBar.dismiss();
                                                SendUserToMainActivty();
                                                Toast.makeText(Make_PostActivity.this, "تم حفظ المنشور", Toast.LENGTH_LONG).show();

                                            } else {
                                                loaddingBar.dismiss();
                                                String message = task.getException().getMessage().toString();
                                                Toast.makeText(Make_PostActivity.this, "حدث خطأ" + message, Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        } else if (selectedImage == null && Description != "") {
                            HashMap postMap = new HashMap();
                            postMap.put("uid", curent_user_id);
                            postMap.put("data", saveCurentData);
                            postMap.put("time", saveCurentTime);
                            postMap.put("description", Description);
                            postMap.put("uid", curent_user_id);
                            postMap.put("postimage", "downloadUrl");
                            postMap.put("profileimage", userprofileImage);
                            postMap.put("fullname", userFullName);
                            PostRef.child(curent_user_id + postRandomName).updateChildren(postMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                loaddingBar.dismiss();
                                                SendUserToMainActivty();
                                                Toast.makeText(Make_PostActivity.this, "تم حفظ المنشور", Toast.LENGTH_LONG).show();

                                            } else {
                                                loaddingBar.dismiss();
                                                String message = task.getException().getMessage().toString();
                                                Toast.makeText(Make_PostActivity.this, "حدث خطأ" + message, Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        } else {
                            HashMap postMap = new HashMap();
                            postMap.put("uid", curent_user_id);
                            postMap.put("data", saveCurentData);
                            postMap.put("time", saveCurentTime);
                            postMap.put("description", Description);
                            postMap.put("uid", curent_user_id);
                            postMap.put("postimage", downloadUrl);
                            postMap.put("profileimage", userprofileImage);
                            postMap.put("fullname", userFullName);
                            PostRef.child(curent_user_id + postRandomName).updateChildren(postMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                loaddingBar.dismiss();
                                                SendUserToMainActivty();
                                                Toast.makeText(Make_PostActivity.this, "تم حفظ المنشور", Toast.LENGTH_LONG).show();

                                            } else {
                                                loaddingBar.dismiss();
                                                String message = task.getException().getMessage().toString();
                                                Toast.makeText(Make_PostActivity.this, "حدث خطأ" + message, Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(Make_PostActivity.this, "ERRoR Occured .." + e, Toast.LENGTH_LONG).show();

        }
    }

    private void OpenGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            SendUserToMainActivty();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if(requestCode==gallery_pick&&resultCode==RESULT_OK&&data!=null) {
              imageUri=data.getData();
            SelectPostimage.setImageURI(imageUri);
          */
        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    SelectPostimage.setImageURI(selectedImage);
                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                } else {
                    Toast.makeText(Make_PostActivity.this, "لم يتم اختيار الصوره", Toast.LENGTH_LONG).show();

                }
        }
    }

    private void SendUserToMainActivty() {
        Intent mainIntent = new Intent(Make_PostActivity.this, MainActivity.class);
        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        // finish();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wificonn != null && wificonn.isConnected()) || (mobileconn != null && mobileconn.isConnected())) {
            return true;
        } else {
            return false;
        }


    }
}