package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar mtoolbar;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postlist;
     private FirebaseAuth mAuth;
    private DatabaseReference UserRef,PostsRef,LikesRef;
    Query query;
    private CircleImageView NavProfileImage;
    private TextView NavProfilrUserName;
    private ImageButton AddNewPostButton;
    private  String currentUserID,page_flag="0";
    Boolean LikerChecker =false;
    private ProgressDialog loaddingBar;

    private AppBarConfiguration mAppBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // mtoolbar=(Toolbar) findViewById(R.id.main_app_bar);
        //mtoolbar.setTitle("Montessori House");
        //setSupportActionBar(mtoolbar);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_app_bar);
        toolbar.setTitle("Montessori House");

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();



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
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef=FirebaseDatabase.getInstance().getReference().child("posts");
        LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        query = FirebaseDatabase.getInstance().getReference().child("posts");
         SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
         currentUserID =pref.getString("uid_SharedPreferences",null);
         page_flag=pref.getString("page_flage_SharedPreferences",null);
         loaddingBar=new ProgressDialog(this);

          AddNewPostButton=(ImageButton)findViewById(R.id.add_new_post_button);
         drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        postlist=(RecyclerView) findViewById(R.id.all_users_post_list);
        postlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postlist.setLayoutManager(linearLayoutManager);

        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage =(CircleImageView)navView.findViewById(R.id.nav_profilr_image);
        NavProfilrUserName=(TextView)navView.findViewById(R.id.nav_use_full_name);
        DispalyAllUserPosts();

        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("fullname")){
                        String fullName=dataSnapshot.child("fullname").getValue().toString();
                        NavProfilrUserName.setText(fullName);

                    }else {
                        Toast.makeText(MainActivity.this," عفو , هذا الاسم غير موجود",Toast.LENGTH_LONG).show();


                    }
                    if (dataSnapshot.hasChild("profileimage")){
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                            Glide.with(MainActivity.this).load(image).into(NavProfileImage);
                    }
                    // Picasso.with(SetUpActivity.this).load(image).placeholder(R.drawable.profile).into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             UserMenuSelector(item);
                return false;
            }
        });
         AddNewPostButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SendUserToPostActivty();
             }
         });
    }

    private void DispalyAllUserPosts() {
        if (isNetworkAvailable(this)){

        FirebaseRecyclerOptions<posts_Model> options =
                new FirebaseRecyclerOptions.Builder<posts_Model>()
                        .setQuery(query, posts_Model.class)
                        .build();

        FirebaseRecyclerAdapter<posts_Model, postsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<posts_Model, postsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull postsViewHolder viewHolder, int position, @NonNull posts_Model model) {
                       /*  posts.class,
                                 R.layout.all_post_layout,
                                 postsViewHolder.class,
                                 PostsRef*/
                        final String postKey = getRef(position).getKey();
                        viewHolder.setfullname(model.getFullname());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getData());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setprofileimage(getApplicationContext(), model.getProfileimage());
                        viewHolder.setPostimagee(getApplicationContext(), model.getPostimage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent clickPostIntent = new Intent(MainActivity.this, Modify_PostActivity.class);
                                clickPostIntent.putExtra("postKey", postKey);
                                startActivity(clickPostIntent);
                            }
                        });
                      /*   //viewHolder.setLikebuttonStatus(postKey);

                         viewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 Intent commentsIntent=new Intent(MainActivity.this,CommentsActivity.class);
                                 commentsIntent.putExtra("postKey",postKey);
                                 startActivity(commentsIntent);

                             }
                         });
                         viewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {

                                 LikerChecker=true;
                                 LikesRef.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (LikerChecker.equals(true)){
                                            if (dataSnapshot.child(postKey).hasChild(currentUserID))
                                            {

                                                LikesRef.child(postKey).child(currentUserID).removeValue();
                                                LikerChecker=false;
                                            }else {
                                                LikesRef.child(postKey).child(currentUserID).setValue(true);


                                            }
                                        }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {

                                     }
                                 });
                             }
                         });*/
                    }

                    @NonNull
                    @Override
                    public postsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_post_card_layout, parent, false);

                        return new postsViewHolder(view);
                    }

                };
        firebaseRecyclerAdapter.startListening();
        postlist.setAdapter(firebaseRecyclerAdapter);
    }else{
            loaddingBar.setTitle("تنبية الاتصال");
            loaddingBar.setMessage("يرجي الاتصال بالانترنت");
            loaddingBar.show();
            loaddingBar.setCanceledOnTouchOutside(true);


        }

    }

    public class  postsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton LikePostButton,CommentPostButton;
        TextView DisplayNoOfLikes;
        int CountLikes;
        String CurrentUserID;
        DatabaseReference likesRef;
        public postsViewHolder(@NonNull View itemView) {

            super(itemView);
            mView=itemView;
        /*    LikePostButton=(ImageButton) mView.findViewById(R.id.like_button);
            CommentPostButton=(ImageButton) mView.findViewById(R.id.comment_button);
            DisplayNoOfLikes=(TextView)mView.findViewById(R.id.display_no_of_likes);*/
            LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
            CurrentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        public void setLikebuttonStatus(final  String Postkey){
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(Postkey).hasChild(currentUserID)){
                         CountLikes=(int)dataSnapshot.child(Postkey).getChildrenCount();
                         LikePostButton.setImageResource(R.drawable.like);
                         DisplayNoOfLikes.setText((Integer.toString(CountLikes)+("  Likes")));
                    }else {
                        CountLikes=(int)dataSnapshot.child(Postkey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.dislike);
                        DisplayNoOfLikes.setText((Integer.toString(CountLikes)+("  Likes")));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void setfullname(String fullname){
            TextView userName =(TextView) mView.findViewById(R.id.post_user_name);
            userName.setText(fullname);

        }
        public void setprofileimage(Context ctx, String profileimage){
            ImageView image=(CircleImageView) mView.findViewById(R.id.post_profile_image);
            //Picasso.with(ctx).load(profileimage).into(image);
            Glide.with(MainActivity.this).load(profileimage).into(image);

        }
        public void setTime(String time){
            TextView post_time =(TextView) mView.findViewById(R.id.post_time);
            post_time.setText("   "+time);

        }
        public void setDate(String data){
            TextView post_Date =(TextView) mView.findViewById(R.id.post_date);
            post_Date.setText("   "+  data);
        }public void setDescription(String description){
            TextView post_description =(TextView) mView.findViewById(R.id.post_description);
            post_description.setText(description);
        }public void setPostimagee(Context ctx,String postimage){
            ImageView image =(ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(postimage).into(image);
            //Glide.with(MainActivity.this).load(postimage).into(image);

        }

    }
    private void SendUserToPostActivty() {
        Intent addNewPostIntent=new Intent(MainActivity.this, Make_PostActivity.class);
        startActivity(addNewPostIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentuser=mAuth.getCurrentUser();
        if (currentuser == null){
            SendUserToLoginActivty();
        }else {
            CheckUserExistence();

        }
    }

    private void CheckUserExistence() {
        final  String current_use_id=mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_use_id)){
                    SendUserToSetupActivty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToSetupActivty() {
        Intent setUpIntent=new Intent(this,SetUpActivity.class);
        setUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setUpIntent);
        finish();

    }

    private void SendUserToLoginActivty() {
        Intent loginintent=new Intent(MainActivity.this,LoginActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
    }

    public void setSupportActionBar(Toolbar mtoolbar) {

    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {
            /*case R.id.nav_post:
             SendUserToPostActivty();
             break;
            case R.id.nav_profile:
                SendUserToSentrepoertActivity();
                 break;

            */
            case R.id.nav_home:
                if (isNetworkAvailable(this)){
                SendUserToMainActivity(page_flag);
                }else{
                    loaddingBar.setTitle("تنبية الاتصال");
                    loaddingBar.setMessage("يرجي الاتصال بالانترنت");
                    loaddingBar.show();
                    loaddingBar.setCanceledOnTouchOutside(true);

                }

                break;
            case R.id.nav_profile:
                if (isNetworkAvailable(this)){
                     SendUserToProfileActivty(page_flag);
                break;
                }else{
                    loaddingBar.setTitle("تنبية الاتصال");
                    loaddingBar.setMessage("يرجي الاتصال بالانترنت");
                    loaddingBar.show();
                    loaddingBar.setCanceledOnTouchOutside(true);


                }

                    case R.id.nav_show_report:
                if (isNetworkAvailable(this)){
                if (page_flag.equals("1")) {
                    SendUserToShowReportActivty(page_flag);
                }else if (page_flag.equals("2"))
                {
                    SendUserToFindallstudentsActivty(page_flag,"report");
                }else if(page_flag.equals("3")){
                    SendUserToFindallstudentsActivty(page_flag,"report");
                }
                }else{
                    loaddingBar.setTitle("تنبية الاتصال");
                    loaddingBar.setMessage("يرجي الاتصال بالانترنت");
                    loaddingBar.show();
                    loaddingBar.setCanceledOnTouchOutside(true);


                }

                break;
              case R.id.nav_recharge:
                  if (isNetworkAvailable(this)){
                 if (page_flag.equals("1")) {
                     SendUserTorechargeActivity();
                 }else if (page_flag.equals("2"))
                 {
                     Toast.makeText(this,"ليس لديك الصلاحية للقيام بهذه الخطوة",Toast.LENGTH_LONG).show();
                 }else if(page_flag.equals("3")){
                     SendUserToFindallstudentsActivty(page_flag,"recharge");
                 }
                  }else{
                      loaddingBar.setTitle("تنبية الاتصال");
                      loaddingBar.setMessage("يرجي الاتصال بالانترنت");
                      loaddingBar.show();
                      loaddingBar.setCanceledOnTouchOutside(true);


                  }

                  break;
           /* case R.id.nav_friends:
                SendUserToFindFriendsActivty();
                Toast.makeText(this,"Friends",Toast.LENGTH_LONG).show();
                break;*/

            case R.id.nav_num1:
                 Intent num1=new Intent(Intent.ACTION_DIAL);
                num1.setData(Uri.parse("tel:01065339919"));
                startActivity(num1);

                 break;
            case R.id.nav_num2:
                  Intent num2=new Intent(Intent.ACTION_DIAL);
                num2.setData(Uri.parse("tel:01275904512"));
                startActivity(num2);
                 break;
            case R.id.nav_num3:

                Intent num3=new Intent(Intent.ACTION_DIAL);
                num3.setData(Uri.parse("tel:0482669862"));
                startActivity(num3);
                break;
                /*
                case R.id.nav_setting:
                    SendUserToSettingActivty();
                   Toast.makeText(this,"Setting ",Toast.LENGTH_LONG).show();
                 break;
                 */

            case R.id.nav_logout:
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("uid_SharedPreferences","");
                editor.putString("page_flage_SharedPreferences","");
                editor.apply();
                mAuth.signOut();
               /* SharedPreferencesModel sharedPreferencesModel=new SharedPreferencesModel();
                sharedPreferencesModel.setPage_flage_SharedPredernces("");
                sharedPreferencesModel.setUidSharedPredernces("");*/
                SendUserToLoginActivty();
                 break;
        }
    }
    @Override
    public void onBackPressed(){

    }


    private void SendUserToSettingActivty() {
        Intent setting=new Intent(MainActivity.this,SettingActivity.class);
    //    setting.putExtra("page_flag",page_flag);
        setting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setting);
        finish();
    }
    private void SendUserToShowReportActivty(String page_flag) {
        Intent showReport=new Intent(MainActivity.this, show_ReportListDays.class);
        //showReport.putExtra("page_flag",page_flag);
        showReport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(showReport);
        finish();
    }
    private void SendUserToProfileActivty(String page_flag) {
        Intent profile=new Intent(MainActivity.this,ProfileActivity.class);
        //profile.putExtra("page_flag",page_flag);
        profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profile);
        finish();
    }
    private void SendUserTorechargeActivity() {
        Intent recharge=new Intent(MainActivity.this, show_recharge.class);
       // recharge.putExtra("page_flag",page_flag);
        recharge.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(recharge);
        finish();
    }
    private void SendUserToFindallstudentsActivty(String page_flag,String page_name){
        Intent Allstudent=new Intent(MainActivity.this, FindAllstudentActivity.class);
        //Allstudent.putExtra("page_flag",page_flag);
        Allstudent.putExtra("page_name",page_name);
        startActivity(Allstudent);
        finish();
    }
    private void SendUserToSentrepoertActivity() {
        Intent report=new Intent(MainActivity.this,FindAllstudentActivity.class);
       // report.putExtra("page_flag",page_flag);
        startActivity(report);
        finish();
    }
    private void SendUserToMainActivity(String page_flag) {
        Intent mainIntent=new Intent(MainActivity.this,MainActivity.class);
     //   mainIntent.putExtra("page_flag",page_flag);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
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
