package zzx.ui_components

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean):
    LinearLayoutManager(context, orientation, reverseLayout) {

    private val TAG = "CustomManager"

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        Log.e(TAG, "onLayoutChildren -> ")
    }
}