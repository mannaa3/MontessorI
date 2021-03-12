package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton,NeedNewAccLink;
    private EditText UserEmail,UserPassword;
     private FirebaseAuth mAuth;
    private ProgressDialog loaddingBar;
    private DatabaseReference profileUserRef;
    private String currentUserId;
            String page_flag="0",UIDShared="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        NeedNewAccLink=(Button) findViewById(R.id.register_acc_link);
        UserEmail =(EditText) findViewById(R.id.email);
        UserPassword=(EditText) findViewById(R.id.password);
        LoginButton =(Button)findViewById(R.id.login_button);

        mAuth=FirebaseAuth.getInstance();
        loaddingBar=new ProgressDialog(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();

        if(pref.getString("uid_SharedPreferences", "").equals("")){
            NeedNewAccLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendUserToRegisterActivity();
                }
            });
            LoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AllowUserToLoginIn();

                }
            });

        }else {

            SendUserToMainActivity(pref.getString("page_flage_SharedPreferences", null),pref.getString("uidSharedPredernces", null));

        }



    }
    @Override
    public void onBackPressed() {

      int flag=0;
      if (flag==0){

      }else if(flag==2){
          finish();
      }
    }

    private void AllowUserToLoginIn() {

        if (isNetworkAvailable(this)){

            String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
           
            Toast.makeText(this,"يرجي إدخال البريد الإكتروني ",Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"  من فضلك كلمة المرور ",Toast.LENGTH_LONG).show();

        }else {
            loaddingBar.setTitle("تسجيل الدخول ");
            loaddingBar.setMessage("يرجي الانتظار , جاري تحميل البيانات ");
            loaddingBar.show();
            loaddingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                     if(task.isSuccessful()){


                         currentUserId=mAuth.getCurrentUser().getUid();
                         profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                         profileUserRef.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                 if(datasnapshot.exists()) {
                                     page_flag = datasnapshot.child("flag").getValue().toString();
                                      UIDShared = datasnapshot.child("uid").getValue().toString();

                                     SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                     SharedPreferences.Editor editor = pref.edit();
                                     editor.putString("uid_SharedPreferences",UIDShared);
                                     editor.putString("page_flage_SharedPreferences",page_flag);
                                     editor.apply();

                                     /*
                                     String myprofileImage = datasnapshot.child("profileimage").getValue().toString();
                                     String myUserName = datasnapshot.child("username").getValue().toString();
                                     String myprofileName = datasnapshot.child("fullname").getValue().toString();
                                     String myprofileStatus = datasnapshot.child("staus").getValue().toString();
                                     String myDOB = datasnapshot.child("dop").getValue().toString();
                                     String address = datasnapshot.child("address").getValue().toString();
                                     String myGender = datasnapshot.child("gender").getValue().toString();
                                     String myRelationStatuse = datasnapshot.child("relationshipstatus").getValue().toString();*/
                                     int tempflag = Integer.parseInt(page_flag);
                                     SendUserToMainActivity(page_flag,UIDShared);
//
//                                     if(tempflag==1){
//                                         SendUserToMainActivity();
//                                         loaddingBar.dismiss();
//
//                                     }else if (tempflag==2){
//                                         SendUserToMainActivity();
//                                         loaddingBar.dismiss();
//
//                                     }else if (tempflag==3){
//                                         SendUserToallChild();
//                                         loaddingBar.dismiss();
//
//                                     }else if (tempflag==4){
//                                         loaddingBar.dismiss();
//
//                                     }

                                 }
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });

                        // SendUserToMainActivity();
                         //loaddingBar.dismiss();

                     }else {
                         loaddingBar.dismiss();
                         String message=task.getException().getMessage();

                         Toast.makeText(LoginActivity.this,"حدث خطأ يرجي المحاولي فيما بعد ..."+message,Toast.LENGTH_LONG).show();

                     }
                }
            });


        }
        }else {

            Toast.makeText(this,"لايوجد اتصال بالانترنت ",Toast.LENGTH_LONG ).show();
        }

    }

    private void SendUserToMainActivity(String page_flag,String uidSharedPredernces) {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
      //  mainIntent.putExtra("page_flag",page_flag);
//        mainIntent.putExtra("uidSharedPredernces",uidSharedPredernces);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToRegisterActivity() {
        Intent registerintent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
        finish();
    }
    private void SendUserToallChild() {
        Toast.makeText(LoginActivity.this,"Flag :  "+page_flag,Toast.LENGTH_LONG).show();
        Intent allChild=new Intent(LoginActivity.this, FindAllstudentActivity.class);
        allChild.putExtra("page_flag",page_flag);
        startActivity(allChild);
        finish();
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
