package com.example.week2boostcamp;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton layouButton;
    boolean layoutImageButtonFlag;
    ArrayList<Item> list;

    RecyclerView recyclerView;

    // 레이아웃 매니저
    RecyclerView.LayoutManager layoutManager;
    StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    // 리사이클러뷰 어댑터
    ItemAdapter adapter;

    // Comparator
    Comparator<Item> comparatorDist;
    Comparator<Item> comparatorPopular;
    Comparator<Item> comparatorDate;

    // 거리 가까운 순으로(작은거리부터 출력,거리 오름차순) - comparator
    static class compareDistacn implements Comparator<Item> {
        @Override
        public int compare(Item t1, Item t2) {
            return Integer.compare(t1.distanceCount, t2.distanceCount);
        }
    }

    // 인기 높은 순으로(내림차순) - comparator
    static class comparePopular implements Comparator<Item> {
        @Override
        public int compare(Item t1, Item t2) {
            return Integer.compare(t2.popularCount, t1.popularCount);
        }
    }

    // 최신순 (내림차순) - comparator
    static class compareDate implements Comparator<Item> {
        @Override
        public int compare(Item t1, Item t2) {
            return t2.latestCount.compareTo(t1.latestCount);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Initializing the TabLayout
        TabLayout tabLayout;
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_distance_order));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_popular_order));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_lastest_order));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // change Layout button
        layouButton = (ImageButton) findViewById(R.id.viewShapeButton);
        layouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout();     // 레이아웃 매니저 변경
            }
        });

        //initailize comparator
        comparatorDist = new compareDistacn();
        comparatorPopular = new comparePopular();
        comparatorDate = new compareDate();

        // 탭 거리순, 인기순, 최신순의 정렬 사용
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIndex = tab.getPosition();

                // 탭에 따른 정렬 방식 표현
                switch (tabIndex) {
                    case 0: // 거리순 정렬
                        Collections.sort(list, comparatorDist);
                        break;
                    case 1: //인기순 정렬
                        Collections.sort(list, comparatorPopular);
                        break;
                    case 2: // 최신순
                        Collections.sort(list, comparatorDate);
                        break;
                }
                adapter.notifyDataSetChanged();     // 변경된 화면 갱신


            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 초기화 설정 - 리사이클러뷰
        init();

    }

    // 리사이클러뷰 초기화 설정
    public void init(){
        //초기 레이아웃매니저 설정 및 리사이클러뷰 연결
        layoutManager = gridLayoutManager;
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 아이템 리스트를 만들어서 반환
        list = mappingItem();
        Collections.sort(list,comparatorDist);      //초기화면 거리순 표현

        // 리사이클러뷰와 어댑터 생성 및 연결
        adapter = new ItemAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    // 레이아웃 Grid, Linear 간 변경 메소드
    public void changeLayout() {

        if (!layoutImageButtonFlag) {
            layouButton.setImageResource(R.drawable.ic_grid_select);      //이미지 변경
            layoutManager = linearLayoutManager;            //레이아웃 매니저 변경
        } else {
            layouButton.setImageResource(R.drawable.ic_linear_select);
            layoutManager = gridLayoutManager;
        }
        layoutImageButtonFlag = !layoutImageButtonFlag;
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();     // 변경된 화면 갱신

    }

    // 아이템 리스트 만들기
    public ArrayList<Item> mappingItem() {

        ArrayList<Item> list = new ArrayList<>();
        // 첫아이템
        Item item = new Item();
        item.checked = false;
        item.contents = getResources().getString(R.string.item_contents_one);
        item.title = getResources().getString(R.string.item_title_one);
        item.imgId = R.drawable.goodfood_one;
        item.distanceCount = 4;
        item.latestCount = new Date(System.currentTimeMillis());
        item.popularCount = 30;
        list.add(item);

        //두번쨰
        item = new Item();
        item.checked = false;
        item.contents = getResources().getString(R.string.item_contents_two);
        item.title = getResources().getString(R.string.item_title_two);
        item.imgId = R.drawable.goodfood_two;
        item.distanceCount = 9;
        item.latestCount = new Date(System.currentTimeMillis());
        item.popularCount = 23;
        list.add(item);

        //세번째
        item = new Item();
        item.checked = false;
        item.contents = getResources().getString(R.string.item_contents_three);
        item.title = getResources().getString(R.string.item_title_three);
        item.imgId = R.drawable.goodfood_three;
        item.distanceCount = 1235;
        item.latestCount = new Date(System.currentTimeMillis());
        item.popularCount = 432;
        list.add(item);

        //네번쨰
        item = new Item();
        item.checked = false;
        item.contents = getResources().getString(R.string.item_contents_forth);
        item.title = getResources().getString(R.string.item_title_forth);
        item.imgId = R.drawable.goodfood_forth;
        item.distanceCount = 88888;
        item.latestCount = new Date(System.currentTimeMillis());
        item.popularCount = 1;
        list.add(item);

        //다섯번쨰
        item = new Item();
        item.checked = false;
        item.contents = getResources().getString(R.string.item_contents_five);
        item.title = getResources().getString(R.string.item_title_five);
        item.imgId = R.drawable.goodfood_five;
        item.distanceCount = 2;
        item.latestCount = new Date(System.currentTimeMillis());
        item.popularCount = 0;
        list.add(item);

        return list;
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
