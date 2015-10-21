package ru.thevhod.picquest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import ru.thevhod.picquest.R;
import ru.thevhod.picquest.adapter.GridAdapter;
import ru.thevhod.picquest.data.Command;
import ru.thevhod.picquest.data.GridItem;
import ru.thevhod.picquest.draghelper.OnEndDragListener;
import ru.thevhod.picquest.draghelper.OnStartDragListener;
import ru.thevhod.picquest.draghelper.SimpleItemTouchHelperCallback;

public class GridActivity extends SocketActivity implements OnStartDragListener, OnEndDragListener {

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private GridAdapter adapter;

    public static void startActivity(Context context, String ip) {
        Intent starter = new Intent(context, GridActivity.class);
        addIpExtra(starter, ip);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        List<GridItem> gridItemList = getItems();

        adapter = new GridAdapter(this, gridItemList, this, this);

        final ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
    }

    private List<GridItem> getItems() {
        List<GridItem> list = new ArrayList<>();
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 1));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 0));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 2));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 3));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 4));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 5));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 6));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 7));
        list.add(new GridItem("http://lorempixel.com/341/240/cats/", 8));
        return list;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEndDrag() {
        if (isRightOrder(adapter.getGridItems())) {
            sendCommand(Command.COMMAND_PLAY, "0");
        }
    }

    private boolean isRightOrder(List<GridItem> gridItems) {
        for (int i = 0; i < gridItems.size() - 1; i++) {
            GridItem item = gridItems.get(i);
            if (item.getId() > gridItems.get(i + 1).getId()) {
                return false;
            }
        }
        return true;
    }
}
