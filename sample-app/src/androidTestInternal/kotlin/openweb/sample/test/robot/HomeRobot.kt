package openweb.sample.test.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import openweb.sample.R

/**
 * Robot for interacting with the Home screen.
 * View IDs sourced from fragment_home.xml.
 */
class HomeRobot {

    fun verifyHomeScreenDisplayed(): HomeRobot {
        onView(withId(R.id.btnUIFlows)).check(matches(isDisplayed()))
        return this
    }

    fun setSpotId(spotId: String): HomeRobot {
        onView(withId(R.id.etSpotId))
            .perform(clearText(), typeText(spotId), closeSoftKeyboard())
        return this
    }

    fun setPostId(postId: String): HomeRobot {
        onView(withId(R.id.etPostid))
            .perform(clearText(), typeText(postId), closeSoftKeyboard())
        return this
    }

    fun clickUIFlows(): HomeRobot {
        onView(withId(R.id.btnUIFlows)).perform(click())
        return this
    }

    fun clickUIViews(): HomeRobot {
        onView(withId(R.id.btnUIViews)).perform(click())
        return this
    }

    fun clickUIComponents(): HomeRobot {
        onView(withId(R.id.btnUIComponents)).perform(click())
        return this
    }
}
