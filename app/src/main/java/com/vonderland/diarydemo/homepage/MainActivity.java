package com.vonderland.diarydemo.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.SplashActivity;
import com.vonderland.diarydemo.adapter.HomePagerAdapter;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.editpage.EditActivity;
import com.vonderland.diarydemo.event.RefreshNavEvent;
import com.vonderland.diarydemo.event.RegisterFinishEvent;
import com.vonderland.diarydemo.utils.DateTimeUtil;
import com.vonderland.diarydemo.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DebugMainActivity";

    private HomePagerAdapter adapter;
    private DiaryFragment diaryFragment;
    private MomentFragment momentFragment;
    private TabLayout tabLayout;

    private DiaryPresenter diaryPresenter;
    private MomentPresenter momentPresenter;

    private ImageView navAvatar;
    private TextView navNickName;

    private long lastBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews();
    }

    private void initVariables() {
        diaryFragment = new DiaryFragment();
        momentFragment = new MomentFragment();
        diaryPresenter = new DiaryPresenter(this, diaryFragment);
        momentPresenter = new MomentPresenter(this, momentFragment);

        adapter = new HomePagerAdapter(getSupportFragmentManager(), this, diaryFragment, momentFragment);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_DIARY_CHANGE);
        intentFilter.addAction(Constant.ACTION_MOMENT_CHANGE);
        registerReceiver(receiver, intentFilter);
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra(Constant.KEY_EDIT_FROM, Constant.DIARY_FROM_CREATE);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra(Constant.KEY_EDIT_FROM, Constant.MOMENT_FROM_CREATE);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navAvatar = (ImageView) header.findViewById(R.id.nav_avatar_iv);
        navNickName = (TextView) header.findViewById(R.id.nav_user_nick);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (java.util.Calendar.getInstance().getTimeInMillis() - lastBackPressed > 1000) {
                lastBackPressed = java.util.Calendar.getInstance().getTimeInMillis();
                Toast.makeText(this, R.string.click_again_to_exit, Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), Constant.ACTION_DIARY_CHANGE)) {
                Object data = intent.getSerializableExtra(Constant.DIARY_FROM_BROADCAST);
                if (data != null && data instanceof Diary) {
                    diaryPresenter.onDataChange(data);
                }
            } else if (TextUtils.equals(intent.getAction(), Constant.ACTION_MOMENT_CHANGE)) {
                Object data = intent.getSerializableExtra(Constant.MOMENT_FROM_BROADCAST);
                if (data != null && data instanceof Moment) {
                    momentPresenter.onDataChange(data);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshNavEvent event) {
        String url = event.avatar;
        if (url.startsWith("files/image")) {
            url = Constant.HOST + url;
        }
        Glide.with(this)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(navAvatar);
        navNickName.setText(event.nickName);
        EventBus.getDefault().removeStickyEvent(event);
    }
}
