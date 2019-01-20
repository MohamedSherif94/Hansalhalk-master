package com.hansalhalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;

import java.io.File;

import static com.hansalhalk.HomeActivity.USER_DATA_JO;
import static com.hansalhalk.HomeActivity.USER_IMAGE_URL;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreference;
    private String token;

    InputMethodManager imm;

    private boolean isFirstClickUsername = true;
    private boolean isFirstClickMail = true;
    private boolean isFirstClickPhone = true;
    private boolean isFirstClickFacebook = true;
    private boolean isFirstClickTwitter = true;
    private boolean isFirstClickInstagram = true;

    //header
    private TextView mTextViewName;
    private ImageView mImageViewProfile;
    private ImageView mImageViewProfileChangeIc;
    private TextView mTextViewAddress;

    private EditText mEditTextName;
    private EditText mEditTextMail;
    private EditText mEditTextPhone;
    private EditText mEditTextFacebook;
    private EditText mEditTextTwitter;
    private EditText mEditTextInstagram;

    private ImageView mNameEditIc;
    private ImageView mMailEditIc;
    private ImageView mPhoneEditIc;
    private ImageView mFacebookEditIc;
    private ImageView mTwitterEditIc;
    private ImageView mInstagramEditIc;

    private static final int GALLERY_PICK = 1;
    private File mImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initializeComponents();

        try {
            mTextViewName.setText(USER_DATA_JO.getString("name"));
            Picasso.get().load(USER_IMAGE_URL).placeholder(R.drawable.avatar).into(mImageViewProfile);
            mTextViewAddress.setText(USER_DATA_JO.getString("address"));

            mEditTextName.setText(USER_DATA_JO.getString("name"));
            mEditTextMail.setText(USER_DATA_JO.getString("email"));
            mEditTextPhone.setText(USER_DATA_JO.getString("phone"));
            mEditTextFacebook.setText(USER_DATA_JO.getString("facebook"));
            mEditTextTwitter.setText(USER_DATA_JO.getString("twitter"));
            mEditTextInstagram.setText(USER_DATA_JO.getString("instagram"));
        } catch (JSONException e) {
            Toast.makeText(ProfileActivity.this, "هناك خطأ فى السيرفر !!!", Toast.LENGTH_SHORT).show();
        }

        mImageViewProfileChangeIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageAsFile();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mSharedPreference = getSharedPreferences("token_file", MODE_PRIVATE);
        token = mSharedPreference.getString("token", null);
    }

    private void initializeComponents(){
        mProgressDialog = new ProgressDialog(this);

        //Header
        mTextViewName = findViewById(R.id.profile_name_textview);
        mImageViewProfile = findViewById(R.id.profile_image);
        mImageViewProfileChangeIc = findViewById(R.id.profile_change_image_ic);
        mTextViewAddress = findViewById(R.id.profile_address_textview);

        mEditTextName = findViewById(R.id.profile_username_edittext);
        mEditTextMail = findViewById(R.id.profile_email_edittext);
        mEditTextPhone = findViewById(R.id.profile_phone_edittext);
        mEditTextFacebook = findViewById(R.id.profile_facebook_edittext);
        mEditTextTwitter = findViewById(R.id.profile_twitter_edittext);
        mEditTextInstagram = findViewById(R.id.profile_instagram_edittext);

        mNameEditIc = findViewById(R.id.profile_username_edit_ic);
        mMailEditIc = findViewById(R.id.profile_email_edit_ic);
        mPhoneEditIc = findViewById(R.id.profile_phone_edit_ic);
        mFacebookEditIc = findViewById(R.id.profile_facebook_edit_ic);
        mTwitterEditIc = findViewById(R.id.profile_twitter_edit_ic);
        mInstagramEditIc = findViewById(R.id.profile_instagram_edit_ic);
    }

    public void editUsername(View view) {
        if (isFirstClickUsername){
            isFirstClickUsername = false;
            mNameEditIc.setImageResource(R.drawable.ic_true);
            mNameEditIc.setBackground(null);
            enableEdit(mEditTextName);
        }
        else {
            isFirstClickUsername = true;
            mNameEditIc.setImageResource(R.drawable.ic_edit);
            mNameEditIc.setBackground(getResources().getDrawable(R.drawable.shape_bg_edit));
            disableEdit(mEditTextName);

            mTextViewName.setText(mEditTextName.getText().toString().trim());
            update_user_data("name", mEditTextName.getText().toString().trim());
        }
    }

    public void editEmail(View view) {
        if (isFirstClickMail){
            isFirstClickMail = false;
            mMailEditIc.setImageResource(R.drawable.ic_true);
            mMailEditIc.setBackground(null);
            enableEdit(mEditTextMail);
        }
        else {
            isFirstClickMail = true;
            mMailEditIc.setImageResource(R.drawable.ic_edit);
            mMailEditIc.setBackground(getResources().getDrawable(R.drawable.shape_bg_edit));
            disableEdit(mEditTextMail);

            update_user_data("email", mEditTextMail.getText().toString().trim());
        }
    }

    public void editPhoneNo(View view) {
        if (isFirstClickPhone){
            isFirstClickPhone = false;
            mPhoneEditIc.setImageResource(R.drawable.ic_true);
            mPhoneEditIc.setBackground(null);
            enableEdit(mEditTextPhone);
        }
        else {
            isFirstClickPhone = true;
            mPhoneEditIc.setImageResource(R.drawable.ic_edit);
            mPhoneEditIc.setBackground(getResources().getDrawable(R.drawable.shape_bg_edit));
            disableEdit(mEditTextPhone);

            update_user_data("phone", mEditTextPhone.getText().toString().trim());
        }
    }

    public void editFacebook(View view) {
        if (isFirstClickFacebook){
            isFirstClickFacebook = false;
            mFacebookEditIc.setImageResource(R.drawable.ic_true);
            mFacebookEditIc.setBackground(null);
            enableEdit(mEditTextFacebook);
        }
        else {
            isFirstClickFacebook = true;
            mFacebookEditIc.setImageResource(R.drawable.ic_edit);
            mFacebookEditIc.setBackground(getResources().getDrawable(R.drawable.shape_bg_edit));
            disableEdit(mEditTextFacebook);

            update_user_data("facebook", mEditTextFacebook.getText().toString().trim());
        }
    }

    public void editTwitter(View view) {
        if (isFirstClickTwitter){
            isFirstClickTwitter = false;
            mTwitterEditIc.setImageResource(R.drawable.ic_true);
            mTwitterEditIc.setBackground(null);
            enableEdit(mEditTextTwitter);
        }
        else {
            isFirstClickTwitter = true;
            mTwitterEditIc.setImageResource(R.drawable.ic_edit);
            mTwitterEditIc.setBackground(getResources().getDrawable(R.drawable.shape_bg_edit));
            disableEdit(mEditTextTwitter);

            update_user_data("twitter", mEditTextTwitter.getText().toString().trim());
        }
    }

    public void editInstagram(View view) {
        if (isFirstClickInstagram){
            isFirstClickInstagram = false;
            mInstagramEditIc.setImageResource(R.drawable.ic_true);
            mInstagramEditIc.setBackground(null);
            enableEdit(mEditTextInstagram);
        }
        else {
            isFirstClickInstagram = true;
            mInstagramEditIc.setImageResource(R.drawable.ic_edit);
            mInstagramEditIc.setBackground(getResources().getDrawable(R.drawable.shape_bg_edit));
            disableEdit(mEditTextInstagram);

            update_user_data("instagram", mEditTextInstagram.getText().toString().trim());
        }
    }

    public void enableEdit(EditText editText) {
        editText.setKeyListener(new AppCompatEditText(getApplicationContext()).getKeyListener());
        //  editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setCursorVisible(true);
        editText.requestFocus();
    }

    public void disableEdit(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private void update_user_data(String key, String value) {

        if (HelperClass.isOnline(ProfileActivity.this)){

            mProgressDialog.setTitle("تحديث البيانات");
            mProgressDialog.setMessage("من فضلك إنتظر حتى يتم تحديث البيانات...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            AndroidNetworking.post(HelperClass.update_user_info_url)
                    .addQueryParameter("token", token)
                    .addQueryParameter(key, value)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            mProgressDialog.dismiss();

                            Toast.makeText(ProfileActivity.this, "تم تحديث البيانات بنجاح !!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError anError) {
                            mProgressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "حدث خطأ فى تحديث البيانات !!!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }else {
            Toast.makeText(ProfileActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImageAsFile(){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                 Uri resultUri = result.getUri();
                mImageFile = new File(resultUri.getPath());

                if (HelperClass.isOnline(ProfileActivity.this)){
                    mImageViewProfile.setImageURI(resultUri);
                    update_user_image();
                }else {
                    Toast.makeText(ProfileActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                //Exception error = result.getError();
            }
        }
    }

    private void update_user_image() {

        mProgressDialog.setTitle("تحديث الصورة الشخصية");
        mProgressDialog.setMessage("من فضلك إنتظر حتى يتم تحديث الصورة الشخصية...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        AndroidNetworking.upload(HelperClass.update_user_info_url)
                .addQueryParameter("token", token)
                .addMultipartFile("img",mImageFile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "تم تحديث الصورة بنجاح !!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "حدث خطأ فى تحديث الصورة !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
