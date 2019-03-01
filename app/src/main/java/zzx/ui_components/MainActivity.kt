package zzx.ui_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flowLayout.setList(createList())
        btn.setOnClickListener {
            flowLayout.add("add click ssss sdasaffau")
        }
    }

    private fun createList(): List<String> {
        val list = mutableListOf<String>()
        list.add("first")
        list.add("second")
        list.add("third")
        list.add("forth")
//        for(i in 0 until 20) {
//            var text = ""
//            for(j in 0 until i) {
//                text += j
//            }
//            list.add(text)
//        }
        return list
    }
}
