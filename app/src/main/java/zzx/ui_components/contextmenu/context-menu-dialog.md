### ContextMenuDialog
   an menu dialog project expanded with project [Context-Menu.Android](https://github.com/Yalantis/Context-Menu.Android)
   <img src="./context-menu-dialog.gif" width="300" alt="ContextMenuDialog" />
   
### Usage:
   first step, create ContextMenuDialog
```
contextMenuDialog = ContextMenuDialog.newInstance()
```
   and then, add menu items, each item contains a title and image, 
   only just image can also valid, certainly you can add menu items at the same time with method apply
```
contextMenuDialog.add(R.drawable.ic_close_primary_48dp)
contextMenuDialog.add("Send message", R.drawable.ic_message_primary_48dp)
contextMenuDialog.add("Add to favorites", R.drawable.ic_favorite_primary_48dp)
contextMenuDialog.add("Add to friends", R.drawable.ic_person_add_primary_48dp)
contextMenuDialog.add("Block user", R.drawable.ic_block_primary_48dp)
```
   the last step is to show the dialog
```
supportFragmentManager.findFragmentByTag(ContextMenuDialog.TAG)
            ?: contextMenuDialog.show(supportFragmentManager, ContextMenuDialog.TAG)
```
   further, you can set the position of context-menu-dialog where to display, 
    just set the gravity before you show the dialog, like
```
contextMenuDialog.gravity = MenuGravity.END
```
   also, you can set animation delay, duration..., please view 
   the sample [ContextMenuDialogActivity](./ContextMenuDialogActivity.kt)
   
### what did i do on [Context-Menu.Android](https://github.com/Yalantis/Context-Menu.Android) basics ?
- add two gravities, TOP and BOTTOM
- simplified access cost, use add an item instead of add item list
- almost all the code has been changed(This is not to say that the original code is not good, 
but that I want to write according to my own ideas.The original code was very good, and I learned a lot from it.)

### in the end
thanks for [Context-Menu.Android](https://github.com/Yalantis/Context-Menu.Android), i've learned a lot from it