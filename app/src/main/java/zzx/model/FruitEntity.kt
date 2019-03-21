package zzx.model

import android.util.TypedValue
import android.widget.TextView
import com.example.library.BaseAdapter
import com.example.library.IEntity
import zzx.ui_components.R

data class FruitEntity(val name: String): IEntity<FruitEntity> {

    override fun bindView(
        baseAdapter: BaseAdapter,
        holder: BaseAdapter.ViewHolder,
        data: FruitEntity,
        position: Int
    ) {
        (holder.itemView as TextView).apply {
            text = name
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 31f)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.flow_layout_text_view
    }

}