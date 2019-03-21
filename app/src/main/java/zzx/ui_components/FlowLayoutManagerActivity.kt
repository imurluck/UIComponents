package zzx.ui_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.library.BaseAdapter
import kotlinx.android.synthetic.main.activity_flow_layout_manager.*
import zzx.components.layout.FlowLayout
import zzx.components.layout.FlowLayoutManager
import zzx.util.ListProvider

class FlowLayoutManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout_manager)
        recycler.layoutManager = CustomLinearLayoutManager(this, VERTICAL, false)
//        recycler.layoutManager = FlowLayoutManager()
        val flowHeader = FlowLayout(this)
        flowHeader.setList(ListProvider.fruitList())
        val adapter = BaseAdapter.Builder()
            .addHeader(flowHeader)
            .setDataList(ListProvider.fruitEntityList())
            .build()
        recycler.adapter = adapter
    }
}
