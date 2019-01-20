package com.hansalhalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private Button mButtonForgetPassword;

    private ProgressDialog mProgressDialog;

    // save data into share SharePreference
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private String token = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.log_in);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextEmail = findViewById(R.id.edit_text_email);
        mEditTextPassword = findViewById(R.id.edit_text_password);
        mButtonLogin = findViewById(R.id.button_log_in);
        mButtonForgetPassword = findViewById(R.id.button_forget_password);

        mProgressDialog = new ProgressDialog(this);

        // save data into share SharePreference
         mSharedPreference = getSharedPreferences("token_file", MODE_PRIVATE);
         mSharedPreferenceEditor = mSharedPreference.edit();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();

                if (HelperClass.isOnline(LoginActivity.this)){
                    login_user(email, password);
                }else {
                    Toast.makeText(LoginActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login_user(String email, String password){

        if ( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgressDialog.setTitle("تسجيل الدخول");
            mProgressDialog.setMessage("من فضلك إنتظر حتى نتحقق من بيانتك");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            AndroidNetworking.post(HelperClass.login_url)
                    .addQueryParameter("password", password)
                    .addQueryParameter("email", email)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            mProgressDialog.dismiss();
                            String key = response.keys().next();

                            switch (key){
                                case "token":
                                    try {
                                        token = response.getString("token");
                                    } catch (JSONException e) {
                                        Toast.makeText(LoginActivity.this, "هناك مشكلة فى السيرفر", Toast.LENGTH_SHORT).show();
                                    }

                                    // save data into share SharePreference
                                    mSharedPreferenceEditor.putString("token",token);
                                    mSharedPreferenceEditor.apply();

                                    Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(loginIntent);
                                    finish();
                                    break;
                                case "email":
                                    Toast.makeText(LoginActivity.this, "يجب أن يكون البريد الالكتروني عنوان بريد إلكتروني صحيح البُنية", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            mProgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "هناك خطأ في البريد الإلكتروني أو الباسورد.!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this,"من فضلك أدخل البريد الإلكتروني و الباسورد !!!", Toast.LENGTH_SHORT).show();
        }
    }

}
