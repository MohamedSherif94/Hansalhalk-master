package com.hansalhalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.hansalhalk.departments.Department;
import com.hansalhalk.departments.DepartmentAdapter;
import com.hansalhalk.users_list.UsersListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static JSONObject USER_DATA_JO;
    public static String USER_IMAGE_URL;
    private String TAG = HomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ProgressDialog mProgressDialog;

    // save data into share SharePreference
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private String token = null;

    //NavigationView
    private NavigationView navigationView;
    private View headerView;
    private ImageView mProfileImage;
    private TextView mNameTextView;
    private TextView mEmailTextView;

    private Spinner countrySpinner;
    private EditText mCityEditText;
    private Button mSearchBtn;
    private String mCountryString;
    private String mCityString;

    private RecyclerView mDepartmentsNamesRecyclerView;
    private DepartmentAdapter mDepartmentAdapter;
    private List<Department> departmentsList = new ArrayList<>();
    private ArrayList<Integer> checkedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeComponents();

        if (HelperClass.isOnline(HomeActivity.this)){
            getDepartmentsName();
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                       // menuItem.setCheckable(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        if (HelperClass.isOnline(HomeActivity.this)){
                            sendToUsersListActivity(false, menuItem.getItemId());
                        }else {
                            Toast.makeText(HomeActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                sendToProfileActivity();
            }
        });


        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCountryString = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedList = mDepartmentAdapter.getCheckedList();
                mCityString = mCityEditText.getText().toString().trim();

                if (HelperClass.isOnline(HomeActivity.this)){
                    sendToUsersListActivity(true, 0);
                }else {
                    Toast.makeText(HomeActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (HelperClass.isOnline(HomeActivity.this)){
            getCurrentUserData();
        }else {
            Toast.makeText(HomeActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mProgressDialog = new ProgressDialog(this);

        // get data from share SharePreference
        mSharedPreference = getSharedPreferences("token_file", MODE_PRIVATE);
        mSharedPreferenceEditor = mSharedPreference.edit();
        token = mSharedPreference.getString("token", null);

        //NavigationView
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        mProfileImage = headerView.findViewById(R.id.nav_head_image);
        mNameTextView = headerView.findViewById(R.id.nav_head_name_text);
        mEmailTextView = headerView.findViewById(R.id.nav_head_email_text);

        countrySpinner = findViewById(R.id.country_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, R.layout.my_simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.my_simple_spinner_item);
        // Apply the adapter to the spinner
        countrySpinner.setAdapter(spinnerAdapter);

        mCityEditText = findViewById(R.id.edit_text_city);
        mSearchBtn = findViewById(R.id.button_search);

        mDepartmentsNamesRecyclerView = findViewById(R.id.home_departments_recycler_view);
        mDepartmentAdapter = new DepartmentAdapter(departmentsList);
        mDepartmentsNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDepartmentsNamesRecyclerView.setAdapter(mDepartmentAdapter);

    }

    private void getDepartmentsName() {
        mProgressDialog.setTitle("تحميل الأقسام");
        mProgressDialog.setMessage("من فضلك إنتظر حتى يتم تحميل جميع الأقسام...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        AndroidNetworking.get(HelperClass.departments_categories_url)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Department.class, new ParsedRequestListener<List<Department>>() {
                    @Override
                    public void onResponse(List<Department> departments) {
                        // do anything with response
                        //Log.v(TAG, "departmentList size : " + departments.size());
                        departmentsList.clear();
                        for (Department department : departments) {
                            departmentsList.add(department);
                            //Log.v(TAG, "id : " + department.getId());
                            // Log.v(TAG, "title : " + department.getTitle());
                        }
                        mDepartmentAdapter.notifyDataSetChanged();
                        mProgressDialog.dismiss();
                        addMenuItemsToNavDrawer();
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

    private void addMenuItemsToNavDrawer() {
        Menu menu = navigationView.getMenu();
        menu.clear();
        for (int i = 0; i < departmentsList.size(); i++) {
            Department department = departmentsList.get(i);
            menu.add(R.id.home_group_menu, department.getId(), department.getOrdder(), department.getTitle());
        }
    }

    private void getCurrentUserData() {

        AndroidNetworking.get(HelperClass.user_info_url)
                .addQueryParameter("token", token)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        USER_DATA_JO = response;
                        String email = "";
                        String imageName = "";
                        String name = "";
                        try {
                            imageName = response.getString("avatar");
                            name = response.getString("name");
                            email = response.getString("email");
                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this, "هناك خطأ فى بيانات المستخدم على السيرفر.", Toast.LENGTH_SHORT).show();
                        }

                        USER_IMAGE_URL = HelperClass.profile_images_url + imageName;
                        Picasso.get().load(USER_IMAGE_URL).placeholder(R.drawable.avatar).into(mProfileImage);
                        mNameTextView.setText(name);
                        mEmailTextView.setText(email);
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.v(HomeActivity.class.getSimpleName(), anError.getErrorBody());
                        Toast.makeText(HomeActivity.this, "هناك خطأ فى بيانات المستخدم على السيرفر." + anError.getErrorBody(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void logout_user() {

        mProgressDialog.setTitle("جاري تسجيل الخروج");
        mProgressDialog.setMessage("من فضلك إنتظر حتي يتم تسجيل الخروج بنجاح.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        AndroidNetworking.post(HelperClass.logout_url)
                .addQueryParameter("token", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        mProgressDialog.dismiss();
                        String key = response.keys().next();

                        switch (key) {

                            case "message":
                                // save data into share SharePreference
                                mSharedPreferenceEditor.putString("token", null);
                                mSharedPreferenceEditor.apply();

                                Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(logoutIntent);
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "حدث خطأ في تسجيل الخروج !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                return true;

            case R.id.action_settings:
                sendToProfileActivity();
                return true;

            case R.id.action_logout:
                if (HelperClass.isOnline(HomeActivity.this)){
                    logout_user();
                }else {
                    Toast.makeText(HomeActivity.this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendToProfileActivity() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void sendToUsersListActivity(Boolean isSearch, int department_id) {
        Intent intent = new Intent(HomeActivity.this, UsersListActivity.class);
        intent.putExtra(HelperClass.TOKEN, token);
        intent.putExtra(HelperClass.SEARCH_OR_NAV_ITEM, isSearch);
        if ( isSearch){
            intent.putIntegerArrayListExtra(HelperClass.CHECKED_LIST, checkedList);
            intent.putExtra(HelperClass.COUNTRY, mCountryString);
            intent.putExtra(HelperClass.CITY, mCityString);
            startActivity(intent);
        }else {
            intent.putExtra(HelperClass.DEPARTMENT_ID, department_id);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

}
