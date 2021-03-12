package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText UserEmail,UserPassword,UserConfirmPassword;
    private Button CreatAccountProfile;
    private FirebaseAuth mAuth;
    private ProgressDialog loaddingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        loaddingBar=new ProgressDialog(this);
        UserEmail=(EditText) findViewById(R.id.register_email);
        UserPassword=(EditText) findViewById(R.id.register_password);
        UserConfirmPassword=(EditText) findViewById(R.id.register_confirm_password);
        CreatAccountProfile=(Button) findViewById(R.id.register_create_acc);

       CreatAccountProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CreateNewAccount();
           }
       });

    }
    @Override
    public void onBackPressed() {

        SendUserToLoginActivity();
    }

    private void CreateNewAccount() {
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        String confirmpassword=UserConfirmPassword.getText().toString();
      if(TextUtils.isEmpty(email)){
        Toast.makeText(this,"يرجي كتابة البريد الإلكتروني",Toast.LENGTH_LONG).show();
      }else if (TextUtils.isEmpty(password)){
        Toast.makeText(this,"يرجي إدخال كلمه المرور",Toast.LENGTH_LONG).show();
      }else if (TextUtils.isEmpty(confirmpassword)) {
        Toast.makeText(this,"يرجي إدخال كلمه المرور",Toast.LENGTH_LONG).show();
      }else if (!password.equals(confirmpassword)) {
          Toast.makeText(this,"كلمة المرور غير متطابقة",Toast.LENGTH_LONG).show();
      }else {
          loaddingBar.setTitle("حساب جديد");
          loaddingBar.setMessage("يرجي الانتظار , نقوم بإعداد الحساب");
          loaddingBar.show();
          loaddingBar.setCanceledOnTouchOutside(true);

          mAuth.createUserWithEmailAndPassword(email,password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {

                          if (task.isSuccessful()) {
                              SendUserToSetupActivity();
                              loaddingBar.dismiss();
                              Toast.makeText(RegisterActivity.this, "تم التسجيل بنجاح",Toast.LENGTH_LONG).show();
                          }else {
                              String message=task.getException().getMessage();
                              Toast.makeText(RegisterActivity.this, "حدث خطأ :  \n  "+message,Toast.LENGTH_LONG).show();
                              loaddingBar.dismiss();

                          }
                      }
                  });

      }

    }

    private void SendUserToSetupActivity() {
        Intent setUpInntent=new Intent(this,SetUpActivity.class);
        setUpInntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setUpInntent);
        finish();
    }
    private void SendUserToLoginActivity() {
        Intent LoginIntent=new Intent(this,LoginActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }
}
