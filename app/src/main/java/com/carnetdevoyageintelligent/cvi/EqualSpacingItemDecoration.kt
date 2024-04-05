package com.carnetdevoyageintelligent.cvi

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EqualSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // Calcul de l'espacement gauche et droit
        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount

        // Si c'est la première ligne, appliquer l'espacement en haut
        if (position < spanCount) {
            outRect.top = spacing
        }

        // Application de l'espacement en bas
        outRect.bottom = spacing
    }


}

