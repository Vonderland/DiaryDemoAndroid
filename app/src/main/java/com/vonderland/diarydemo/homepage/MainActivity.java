package com.vonderland.diarydemo.homepage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.adapter.HomePagerAdapter;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "VonderlandMainActivity";
    private HomePagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                executeCall();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void executeCall() {
        DiaryDemoService apiService = ServiceGenerator.createService(DiaryDemoService.class);
        //Call<ListResponse<Diary>> call = apiService.loadAllDiaries();
        Map<String, String> options = new HashMap<>();
        //Call<ListResponse<Diary>> call = apiService.loadDiaries(options);
//        options.put(Constant.KEY_DESCRIPTION, "来自变成猫的 android");
//        options.put(Constant.KEY_EVENT_TIME, System.currentTimeMillis()+ "");
//        options.put(Constant.KEY_TITLE, "android 无图测试修改");
        //options.put(Constant.KEY_ID, "14");
        Call<ListResponse<Diary>> call = apiService.deleteDiary(14);
        call.enqueue(new Callback<ListResponse<Diary>>() {
            @Override
            public void onResponse(Call<ListResponse<Diary>> call, Response<ListResponse<Diary>> response) {
                if (response.isSuccessful()) {
                    if (response.body() instanceof ListResponse) {
                        ListResponse resp = (ListResponse)response.body();
                        List<Diary> data = (List<Diary>)resp.getData();
                        Log.d(TAG, "status code = " + resp.getCode());
                        Log.d(TAG, "size = " + resp.getSize());
                        if (data != null) {
                            Log.d(TAG, "list size = " + data.size());
                            Diary diary = data.get(0);
                            Log.d(TAG, diary.getTitle() + diary.getDescription());
                        }
                    }
                } else {
                    Log.d("Vonderland", "onresponse, failure");
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Diary>> call, Throwable t) {
                Log.d("", "onFailure");
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
