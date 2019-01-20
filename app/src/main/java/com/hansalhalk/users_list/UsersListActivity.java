package com.hansalhalk.users_list;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.hansalhalk.HelperClass;
import com.hansalhalk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity implements UsersAdapter.MyOnClickHandler {

    private String TAG = UsersListActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog;

    private String mTokenSring;
    private ArrayList<Integer> checkedList = new ArrayList<>();
    private String mCountryString;
    private String mCityString;

    private int mDepartment_id;

    private RecyclerView mUsersRecyclerView;
    private UsersAdapter mUserAdapter;
    private List<User> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("الأشخاص");

        initializeComponents();

        mTokenSring = getIntent().getStringExtra(HelperClass.TOKEN);
        boolean isSearch = getIntent().getBooleanExtra(HelperClass.SEARCH_OR_NAV_ITEM, true);

        if (isSearch) {
            checkedList = getIntent().getIntegerArrayListExtra(HelperClass.CHECKED_LIST);
            mCountryString = getIntent().getStringExtra(HelperClass.COUNTRY);
            mCityString = getIntent().getStringExtra(HelperClass.CITY);

            search();
        } else {
            mDepartment_id = getIntent().getIntExtra(HelperClass.DEPARTMENT_ID, 1);
            getSpecificDepUsers();
        }
    }

    private void initializeComponents() {
        mProgressDialog = new ProgressDialog(this);

        mUsersRecyclerView = findViewById(R.id.users_list_recycler_view);
        mUserAdapter = new UsersAdapter(usersList, this);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsersRecyclerView.setAdapter(mUserAdapter);

    }

    private void getSpecificDepUsers() {
        mProgressDialog.setTitle("يتم تحميل البيانات");
        mProgressDialog.setMessage("من فضلك إنتظر حتى يتم تحميل جميع البيانات");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(mDepartment_id);
        //Log.v(TAG, jsonArray.toString());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cat", jsonArray);
        } catch (JSONException e) {
            Toast.makeText(this, "Error in  create json object", Toast.LENGTH_SHORT).show();
        }


        AndroidNetworking.post(HelperClass.search_url)
                .addQueryParameter("token", mTokenSring)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(User.class, new ParsedRequestListener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        // do anything with response

                        for (User user : users) {
                            usersList.add(user);
                        }
                        mUserAdapter.notifyDataSetChanged();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        Toast.makeText(UsersListActivity.this, anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void search() {
        mProgressDialog.setTitle("يتم تحميل البيانات");
        mProgressDialog.setMessage("من فضلك إنتظر حتى يتم تحميل جميع البيانات");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        JSONArray jsonArray = new JSONArray(checkedList);
        //Log.v(TAG, jsonArray.toString());
        JSONObject searchJsonObject = new JSONObject();
        try {
            searchJsonObject.put("cat", jsonArray);
            searchJsonObject.put("country", mCountryString);
            searchJsonObject.put("city", mCityString);
        } catch (JSONException e) {
            Toast.makeText(this, "Error in  create json object", Toast.LENGTH_SHORT).show();
        }
        //Log.v(TAG, "TEstttt :" + testJO.toString());

        AndroidNetworking.post(HelperClass.search_url)
                .addQueryParameter("token", mTokenSring)
                .addJSONObjectBody(searchJsonObject)
                .build()
                .getAsObjectList(User.class, new ParsedRequestListener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        // do anything with response
                        //Log.v(TAG, "departmentList size : " + departments.size());

                        for (User user : users) {
                            usersList.add(user);
                            //Log.v(TAG, "id : " + department.getId());
                            // Log.v(TAG, "title : " + department.getTitle());
                        }
                        mUserAdapter.notifyDataSetChanged();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        Log.v(TAG, "getErrorBody : " + anError.getErrorBody());
                        Log.v(TAG, "getMessage : " + anError.getMessage());
                        Log.v(TAG, "getErrorDetail : " + anError.getErrorDetail());
                        Log.v(TAG, "getErrorCode : " + anError.getErrorCode());
                    }
                });
    }

    @Override
    public void onClick(User user) {
        Intent intent = new Intent(UsersListActivity.this, UserProfileActivity.class);
        intent.putExtra(HelperClass.USER, user);
        startActivity(intent);
    }
}
