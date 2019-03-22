package zzx.ui_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import zzx.components.menu.contextmenu.ContextMenuDialog
import zzx.components.menu.contextmenu.MenuGravity
import zzx.util.ListProvider
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flowLayout.setList(ListProvider.fruitList())

        btn.setOnClickListener {
            Random.nextInt(TEST_STRING.length).apply {
                val fruitList = ListProvider.fruitList()
                for (fruit in fruitList) {
                    flowLayout.add(fruit)
                }
            }
        }
        layoutBtn.apply {
            setOnClickListener {

            }
        }
    }

    companion object {

        private const val TAG = "MainActivity"

        private const val TEST_STRING: String = "this is a test String used by add click"
    }
}
