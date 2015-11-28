package ru.thevhod.picquest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.thevhod.picquest.R;
import ru.thevhod.picquest.data.GridItem;
import ru.thevhod.picquest.draghelper.ItemTouchHelperViewHolder;

public class GridViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    public ImageView imageView;

    public GridViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.grid_image);
    }

    public void bind(GridItem item, Context context, int itemHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = itemHeight;
        itemView.setLayoutParams(params);
        int drawableResourceId = context.getResources().getIdentifier(item.getResourceName(), "drawable", context.getPackageName());
        imageView.setImageResource(drawableResourceId);
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }


    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
