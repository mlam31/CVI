package com.carnetdevoyageintelligent.cvi

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EqualSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // position de l'élément dans l'adaptateur
        val column = position % spanCount // colonne de l'élément

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount // espacement gauche
            outRect.right = (column + 1) * spacing / spanCount // espacement droit
            if (position < spanCount) { // premier rang
                outRect.top = spacing
            }
            outRect.bottom = spacing // espacement en dessous de l'élément
        } else {
            outRect.left = column * spacing / spanCount // espacement gauche
            outRect.right = spacing - (column + 1) * spacing / spanCount // espacement droit
            if (position >= spanCount) {
                outRect.top = spacing // espacement au-dessus de l'élément
            }
        }
    }
}

