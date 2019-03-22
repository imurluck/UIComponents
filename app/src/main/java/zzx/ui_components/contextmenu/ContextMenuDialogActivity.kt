package zzx.ui_components.contextmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_context_menu_dialog.*
import kotlinx.android.synthetic.main.toolbar.*
import zzx.components.menu.contextmenu.ContextMenuDialog
import zzx.components.menu.contextmenu.MenuGravity
import zzx.ui_components.R

class ContextMenuDialogActivity : AppCompatActivity() {

    private lateinit var contextMenuDialog: ContextMenuDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context_menu_dialog)

        setupToolbar()

        setupContextMenuDialog()

        setupBtnListener()
    }

    private fun setupBtnListener() {
        startBtn.setOnClickListener {
            contextMenuDialog.gravity = MenuGravity.START
            showContextMenuDialog()
        }
        topBtn.setOnClickListener {
            contextMenuDialog.gravity = MenuGravity.TOP
            showContextMenuDialog()
        }
        endBtn.setOnClickListener {
            contextMenuDialog.gravity = MenuGravity.END
            showContextMenuDialog()
        }
        bottomBtn.setOnClickListener {
            contextMenuDialog.gravity = MenuGravity.BOTTOM
            showContextMenuDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showContextMenuDialog() {
        supportFragmentManager.findFragmentByTag(ContextMenuDialog.TAG)
            ?: contextMenuDialog.show(supportFragmentManager, ContextMenuDialog.TAG)
    }

    private fun setupContextMenuDialog() {
        contextMenuDialog = ContextMenuDialog.newInstance().apply {
            duration = 100L
            gravity = MenuGravity.BOTTOM
            contextMenuDialog.add(R.drawable.ic_close_primary_48dp)
            contextMenuDialog.add("Send message", R.drawable.ic_message_primary_48dp)
            contextMenuDialog.add("Add to favorites", R.drawable.ic_favorite_primary_48dp)
            contextMenuDialog.add("Add to friends", R.drawable.ic_person_add_primary_48dp)
            contextMenuDialog.add("Block user", R.drawable.ic_block_primary_48dp)
            onItemClickListener = { menuItem, position ->
                Toast.makeText(this@ContextMenuDialogActivity, "${menuItem.title} $position", Toast.LENGTH_SHORT).show()
            }
            onItemLongClickListener = { menuItem, position ->
                Toast.makeText(this@ContextMenuDialogActivity, "${menuItem.title} $position", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.context_menu -> {
                contextMenuDialog.gravity = MenuGravity.END
                showContextMenuDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar as Toolbar)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        tvToolbarTitle.text = "Samantha"
    }
}
