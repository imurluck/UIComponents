package zzx.ui_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import zzx.components.menu.contextmenu.ContextMenuDialog
import zzx.components.menu.contextmenu.MenuGravity
import zzx.util.ListProvider
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var contextMenuDialog: ContextMenuDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupContextMenuDialog()
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
                contextMenuDialog.show(supportFragmentManager, ContextMenuDialog.TAG)

            }
        }
    }

    private fun setupContextMenuDialog() {
        contextMenuDialog = ContextMenuDialog.newInstance().apply {
            gravity = MenuGravity.END
            add(R.drawable.icn_close)
            add("Send message", R.drawable.icn_1)
            add("Like profile", R.drawable.icn_2)
            add("Add to friends", R.drawable.icn_3)
            add("Add to favorites", R.drawable.icn_4)
            add("Block user", R.drawable.icn_5)
        }
    }

    companion object {

        private const val TAG = "MainActivity"

        private val TEST_STRING: String = "this is a test String used by add click"
    }
}
