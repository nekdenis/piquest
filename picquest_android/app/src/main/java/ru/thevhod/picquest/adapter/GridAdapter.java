package ru.thevhod.picquest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.thevhod.picquest.R;
import ru.thevhod.picquest.data.GridItem;
import ru.thevhod.picquest.draghelper.ItemTouchHelperAdapter;
import ru.thevhod.picquest.draghelper.OnEndDragListener;
import ru.thevhod.picquest.draghelper.OnStartDragListener;

public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> implements ItemTouchHelperAdapter {

    private static final int ROWS_COUNT = 3;

    private List<GridItem> gridItems;
    private LayoutInflater inflater;
    private Context context;

    private OnStartDragListener dragStartListener;
    private OnEndDragListener dragEndListener;

    private int itemHeight;

    public GridAdapter(Context context, List<GridItem> items, OnStartDragListener dragStartListener, OnEndDragListener dragEndListener) {
        this.context = context;
        gridItems = items;
        this.dragEndListener = dragEndListener;
        inflater = LayoutInflater.from(context);
        this.dragStartListener = dragStartListener;
        itemHeight = getItemHeight(ROWS_COUNT);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(inflater.inflate(R.layout.grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        holder.bind(gridItems.get(position), context, itemHeight);
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(gridItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        dragEndListener.onEndDrag();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        gridItems.remove(position);
        notifyItemRemoved(position);
    }

    public List<GridItem> getGridItems() {
        return gridItems;
    }

    private int getItemHeight(int rowsPerScreen) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels / rowsPerScreen;
    }
}
