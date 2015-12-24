package ru.thevhod.picquest.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

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

    private Button firstEndButton;
    private Button secondEndButton;
    private Button repeatButton;

    public static void startActivity(Context context, String ip) {
        Intent starter = new Intent(context, GridActivity.class);
        addIpExtra(starter, ip);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        initView();
        initListeners();
        showButtons(false);
    }

    private void initView() {
        firstEndButton = (Button) findViewById(R.id.first_end_button);
        secondEndButton = (Button) findViewById(R.id.second_end_button);
        repeatButton = (Button) findViewById(R.id.repeat_button);
        initGrid();
    }

    private void initGrid() {
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
        list.add(new GridItem("pic_2", 1));
        list.add(new GridItem("pic_1", 0));
        list.add(new GridItem("pic_3", 2));
        list.add(new GridItem("pic_4", 3));
        list.add(new GridItem("pic_5", 4));
        list.add(new GridItem("pic_6", 5));
        list.add(new GridItem("pic_7", 6));
        list.add(new GridItem("pic_8", 7));
        list.add(new GridItem("pic_9", 8));
        return list;
    }

    private void initListeners() {
        firstEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(Command.COMMAND_PLAY, Command.VALUE_PLAY_FIRST_END_VIDEO);
                showButtons(false);
            }
        });
        secondEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(Command.COMMAND_PLAY, Command.VALUE_PLAY_SECOND_END_VIDEO);
                showButtons(false);
            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(Command.COMMAND_PLAY, Command.VALUE_PLAY_INTRO_VIDEO);
                showButtons(false);
            }
        });
    }

    private void showGrid(boolean showGrid) {
        if (showGrid) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.animate().alpha(1).scaleX(1).scaleY(1).translationYBy(100);
        } else {
            recyclerView.animate().alpha(0).scaleX(0.75f).scaleY(0.75f).translationYBy(-100).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

    }

    private void showButtons(boolean showAllButtons) {
        if (showAllButtons) {
            firstEndButton.animate().scaleX(1).scaleY(1).start();
            secondEndButton.animate().scaleX(1).scaleY(1).start();
            repeatButton.animate().scaleX(1).scaleY(1).start();
        } else {
            firstEndButton.animate().scaleX(0).scaleY(0).start();
            secondEndButton.animate().scaleX(0).scaleY(0).start();
            repeatButton.animate().scaleX(0).scaleY(0).start();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEndDrag() {
        if (isRightOrder(adapter.getGridItems())) {
            showGrid(false);
            sendCommand(Command.COMMAND_PLAY, Command.VALUE_PLAY_INTRO_VIDEO);
        }
    }

    @Override
    protected void onMessageRecieved(String message) {
        if (message.contains(Command.COMMAND_FINISHED)) {
            showButtons(true);
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
