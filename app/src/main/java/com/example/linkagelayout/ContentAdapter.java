package com.example.linkagelayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ItemViewHolder> {

    private Context context;
    private List<Entity> datas;
    private HashSet<RecyclerView> observerList = new HashSet<>();
    private int firstPos = -1;
    private int firstOffset = -1;

    public ContentAdapter(Context context,RecyclerView headRecycler) {
        this.context = context;
        //设置头部recycle
        initRecyclerView(headRecycler);
    }

    public void setDatas(List<Entity> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_content, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.tvLeftTitle.setText(datas.get(i).getLeftTitle());
        //右边滑动部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemViewHolder.rvItemRight.setLayoutManager(linearLayoutManager);
        itemViewHolder.rvItemRight.setHasFixedSize(true);
        RightScrollAdapter rightScrollAdapter = new RightScrollAdapter(context);
        rightScrollAdapter.setDatas(datas.get(i).getRightDatas());
        itemViewHolder.rvItemRight.setAdapter(rightScrollAdapter);

        //设置多条recyclerview联动
        initRecyclerView(itemViewHolder.rvItemRight);
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_title)
        TextView tvLeftTitle;
        @BindView(R.id.rv_item_right)
        RecyclerView rvItemRight;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //多条recycleview联动
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null && firstPos > 0 && firstOffset > 0) {
            layoutManager.scrollToPositionWithOffset(firstPos + 1, firstOffset);
        }
        observerList.add(recyclerView);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        for (RecyclerView rv : observerList) {
                            rv.stopScroll();
                        }
                }
                return false;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstPos1 = linearLayoutManager.findFirstVisibleItemPosition();
                View firstVisibleItem = linearLayoutManager.getChildAt(0);
                if (firstVisibleItem != null) {
                    int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
                    for (RecyclerView rv : observerList) {
                        if (recyclerView != rv) {
                            LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                            if (layoutManager != null) {
                                firstPos = firstPos1;
                                firstOffset = firstRight;
                                layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
