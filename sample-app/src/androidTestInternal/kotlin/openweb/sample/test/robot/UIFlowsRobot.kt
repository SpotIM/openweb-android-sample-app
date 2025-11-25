package openweb.sample.test.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import openweb.sample.R

/**
 * Robot for interacting with the UI Flows menu screen.
 * View IDs sourced from fragment_ui_flows_menu.xml.
 */
class UIFlowsRobot {

    fun verifyFlowsMenuDisplayed(): UIFlowsRobot {
        onView(withId(R.id.btnPreConversation)).check(matches(isDisplayed()))
        return this
    }

    fun clickPreConversation(): UIFlowsRobot {
        onView(withId(R.id.btnPreConversation)).perform(click())
        return this
    }

    fun clickConversationFragment(): UIFlowsRobot {
        onView(withId(R.id.btnConversationFragment)).perform(click())
        return this
    }

    fun clickConversationIntent(): UIFlowsRobot {
        onView(withId(R.id.btnConversationIntent)).perform(click())
        return this
    }

    fun clickCommentCreation(): UIFlowsRobot {
        onView(withId(R.id.btnCommentCreation)).perform(click())
        return this
    }

    fun clickCommentThread(): UIFlowsRobot {
        onView(withId(R.id.btnCommentThread)).perform(click())
        return this
    }
}
