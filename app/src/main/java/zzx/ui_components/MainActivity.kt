package zzx.ui_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val fruitList = arrayOf("Apple", "Apricot",
        "Arbutus", "Banana", "Bennett", "Barbados", "Casaba", "Gooseberry",
        "Grapefruit", "Kernel", "Tangerine", "Walnut", "Watermelon")

    private val TEST_STRING: String = "this is a test String used by add click"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flowLayout.setList(fruitList.asList())
        btn.setOnClickListener {
            Random.nextInt(TEST_STRING.length).apply {
                val endIndex = if (this > 0) this else 1
                flowLayout.add(TEST_STRING.substring(0, endIndex))
            }
        }
    }

}
