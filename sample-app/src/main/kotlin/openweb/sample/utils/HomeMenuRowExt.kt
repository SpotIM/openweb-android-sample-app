package openweb.sample.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import openweb.sample.R

/**
 * Populates the icon, title, and subtitle of a menu row included from `item_home_menu_row.xml`.
 * The receiver is the row's root LinearLayout (i.e. `binding.btnXyz.root`).
 */
fun View.bindHomeMenuRow(
    @DrawableRes iconRes: Int,
    title: CharSequence,
    subtitle: CharSequence,
) {
    findViewById<ImageView>(R.id.home_menu_row_icon).setImageResource(iconRes)
    findViewById<TextView>(R.id.home_menu_row_title).text = title
    findViewById<TextView>(R.id.home_menu_row_subtitle).text = subtitle
}
