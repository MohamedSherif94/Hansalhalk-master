package com.hansalhalk.users_list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hansalhalk.HelperClass;
import com.hansalhalk.R;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private User mCurrentUser;

    //header
    private TextView mNameTextView;
    private ImageView mProfileImageView;
    private TextView mAddressTextView;
    private TextView mDepartmentTextView;

    private TextView mUserNameTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private TextView mFacebookTextView;
    private TextView mTwitterTextView;
    private TextView mInstagramTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("بيانات المستخدم");

        initializeComponents();
        mCurrentUser = (User) getIntent().getSerializableExtra(HelperClass.USER);

        //Header
        mNameTextView.setText(mCurrentUser.getName());
        Picasso.get().load(HelperClass.profile_images_url + mCurrentUser.getAvatar()).placeholder(R.drawable.avatar).into(mProfileImageView);
        mAddressTextView.setText(mCurrentUser.getAddress());
        mDepartmentTextView.setText(mCurrentUser.getCategory().get("title").toString());

        mUserNameTextView.setText(mCurrentUser.getName());
        mEmailTextView.setText(mCurrentUser.getEmail());
        mPhoneTextView.setText(mCurrentUser.getPhone());
        mFacebookTextView.setText(mCurrentUser.getFacebook());
        mTwitterTextView.setText(mCurrentUser.getTwitter());
        mInstagramTextView.setText(mCurrentUser.getInstagram());
    }

    private void initializeComponents() {
        //Header
        mNameTextView = findViewById(R.id.user_profile_name_textview);
        mProfileImageView = findViewById(R.id.user_profile_image);
        mAddressTextView = findViewById(R.id.user_profile_address_textview);
        mDepartmentTextView = findViewById(R.id.user_profile_department_textview);

        mUserNameTextView = findViewById(R.id.user_profile_username_textview);
        mEmailTextView = findViewById(R.id.user_profile_email_textview);
        mPhoneTextView = findViewById(R.id.user_profile_phone_textview);
        mFacebookTextView = findViewById(R.id.user_profile_facebook_textview);
        mTwitterTextView = findViewById(R.id.user_profile_twitter_textview);
        mInstagramTextView = findViewById(R.id.user_profile_instagram_textview);
    }

    public void sendEmailToUser(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mEmailTextView.getText().toString().trim()));
        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        startActivity(Intent.createChooser(emailIntent, "Choose Your Favourite App"));
    }

    public void callUserPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mPhoneTextView.getText().toString().trim()));
        startActivity(intent);
    }

    public void openFacebookPage(View view) {
        Intent facebookIntent = null;

        try {

            //getPackageManager().getPackageInfo("com.facebook.katana", 0);
           // replace 107786425949934 with your page ID
           // facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + mFacebookTextView.getText().toString().trim()));
            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mFacebookTextView.getText().toString().trim()));
            startActivity(facebookIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Page Url", Toast.LENGTH_SHORT).show();
        }
    }

    public void openTwitterPage(View view) {
        Intent twitterIntent = null;
        try {
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterTextView.getText().toString().trim()));
            startActivity(twitterIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Page Url", Toast.LENGTH_SHORT).show();
        }
    }

    public void openInstagramPage(View view) {
        Intent instagramIntent = null;
        try {
            instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mInstagramTextView.getText().toString().trim()));
            startActivity(instagramIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Page Url", Toast.LENGTH_SHORT).show();
        }
    }
}
