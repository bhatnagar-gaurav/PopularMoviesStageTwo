package com.udacity.assignment.android.popularmoviesstagetwo.utilities.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*  An item decorator, is responsible for applying effects and light positioning to
    the views. Here it is used to make the spacing uniform for the Grid Items*/

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public ItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // position for the Grid Items
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount; // item column

        if (includeEdge) {
            //Calculating the left and the right offset details for the Outer Rectangle
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            if (position < spanCount) {
                outRect.top = spacing;
            }
            // For the Bottom of the Rectangle
            outRect.bottom = spacing;
        } // Scenario when no edge has to be shown in between the Grid Cells.
        else {
            // Calculating the Left Offset for the Rectangle
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = spacing;
            }
        }
    }
}
