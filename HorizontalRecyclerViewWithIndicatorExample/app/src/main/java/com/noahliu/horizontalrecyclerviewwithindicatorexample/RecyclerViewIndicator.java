package com.noahliu.horizontalrecyclerviewwithindicatorexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewIndicator extends FrameLayout {

    private final FrameLayout rootView;
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
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //獲取現在的位置，並更新給「點」
                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                assert manager != null;
                int current = manager.findLastVisibleItemPosition();
                dotIndicator.onPageScrolled(current);
            }
        });
    }
}
