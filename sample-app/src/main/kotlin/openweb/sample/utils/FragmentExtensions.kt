package openweb.sample.utils

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import openweb.sample.R

/**
 * Extension function to hide toolbar icons (settings and auth) in fragments.
 * This is useful when navigating to settings or auth screens to avoid showing
 * those icons redundantly.
 */
fun Fragment.hideToolbarIcons() {
    requireActivity().addMenuProvider(
        object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) = Unit

            override fun onPrepareMenu(menu: Menu) {
                menu.findItem(R.id.settings)?.isVisible = false
                menu.findItem(R.id.auth)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        },
        viewLifecycleOwner,
        Lifecycle.State.RESUMED
    )
}
