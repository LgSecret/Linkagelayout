package com.example.linkagelayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.tv_left_title)
    TextView tvLeftTitle;
    @BindView(R.id.rv_tab_right)
    RecyclerView rvTabRight;
    @BindView(R.id.ll_top_root)
    LinearLayout llTopRoot;
    @BindView(R.id.recycler_content)
    RecyclerView recyclerContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private List<Entity> mEntities = new ArrayList<>();
    private List<String> rightMoveDatas = new ArrayList<>();
    private List<String> topTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //处理顶部标题部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTabRight.setLayoutManager(linearLayoutManager);
        TopTabAdpater topTabAdpater = new TopTabAdpater(this);
        rvTabRight.setAdapter(topTabAdpater);
        for (int i = 0; i < 50; i++) {
            topTabs.add("类型" + i);
        }
        topTabAdpater.setDatas(topTabs);
        //处理内容部分
        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerContent.setHasFixedSize(true);
        final ContentAdapter contentAdapter = new ContentAdapter(this,rvTabRight);
        recyclerContent.setAdapter(contentAdapter);

        recyclerContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    Entity entity = new Entity();
                    entity.setLeftTitle("贵州茅台" + i);
                    rightMoveDatas.clear();
                    for (int j = 0; j < 50; j++) {
                        rightMoveDatas.add("年份" + j);
                    }
                    entity.setRightDatas(rightMoveDatas);
                    mEntities.add(entity);
                }
                contentAdapter.setDatas(mEntities);
                Toast.makeText(MainActivity.this, "加载完毕,加载了30条数据", Toast.LENGTH_SHORT).show();
            }
        }, 1500);

        //下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerContent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 50; i++) {
                            Entity entity = new Entity();
                            entity.setLeftTitle("贵州茅台" + i);
                            rightMoveDatas.clear();
                            for (int j = 0; j < 50; j++) {
                                rightMoveDatas.add("年份" + j);
                            }
                            entity.setRightDatas(rightMoveDatas);
                            mEntities.add(entity);
                        }
                        contentAdapter.setDatas(mEntities);
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "刷新完毕,刷新了50条数据", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
            }
        });


    }
}
