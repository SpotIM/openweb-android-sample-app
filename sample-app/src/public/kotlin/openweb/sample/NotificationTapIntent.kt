package openweb.sample

import android.app.Application
import android.content.Intent
import openweb.sample.ui.VerticalHomeActivity

fun notificationTapIntent(app: Application): Intent = Intent(app, VerticalHomeActivity::class.java)
