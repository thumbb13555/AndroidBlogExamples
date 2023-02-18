package com.noahliu.horizontalrecyclerviewwithindicatorexample;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewIndicator extends FrameLayout {

    private FrameLayout rootView;
    private DotIndicator dotIndicator;



    public RecyclerViewIndicator(@NonNull Context context) {
        this(context,null);
    }

    public RecyclerViewIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecyclerViewIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = inflate(context, R.layout.dot_root, this);
        rootView = root.findViewById(R.id.root);
    }


    public DotIndicator getDotIndicator() {
        return dotIndicator;
    }

    public void setWithRecyclerView(RecyclerView view) {
        dotIndicator = new DotIndicator(view.getContext());

        rootView.post(() -> {
            int count = view.getAdapter().getItemCount();
            dotIndicator.setPageNum(count);
            rootView.addView(dotIndicator);
        });

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private float mDx;
            private boolean lock;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDx = dx;
                //獲取現在的位置，並更新給「點」
                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int current = manager.findLastVisibleItemPosition();
                dotIndicator.onPageScrolled(current);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                    //獲取現在的item的位置
                    int current = manager.findLastVisibleItemPosition();

                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                        // 覆寫 calculateSpeedPerPixel() 方法來調整滑動速度
                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            // 返回每個像素滑動所需的時間，這裡設置為 200 毫秒
                            return 100f / displayMetrics.densityDpi;
                        }
                    };
                    //如果反向滑動，則將現在的位置-1
                    if (mDx<0 && current>0){
                        if (!lock){
                            lock = true;
                            smoothScroller.setTargetPosition(current-1);
                            manager.startSmoothScroll(smoothScroller);
                        }else{
                            lock = false;
                        }
                    }else{
                        smoothScroller.setTargetPosition(current);
                        manager.startSmoothScroll(smoothScroller);
                    }
                }
            }
        });


    }
}
