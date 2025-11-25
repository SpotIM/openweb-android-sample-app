package openweb.sample.test.regression

import androidx.test.ext.junit.rules.ActivityScenarioRule
import openweb.sample.test.base.BaseEspressoTest
import openweb.sample.test.robot.HomeRobot
import openweb.sample.test.robot.UIFlowsRobot
import openweb.sample.ui.mainactivity.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * L6 Regression Gate — SDK Conversation smoke tests.
 *
 * These tests verify that SDK screens load without crashing.
 * Ralph Loop runs these as a gate — it does NOT generate Espresso tests.
 *
 * Test config: SpotID = sp_eCIlROSD, PostID = sdk1jkl
 */
class SdkConversationRegressionTest : BaseEspressoTest() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val homeRobot = HomeRobot()
    private val uiFlowsRobot = UIFlowsRobot()

    @Test
    fun homeScreen_displaysCorrectly() {
        homeRobot.verifyHomeScreenDisplayed()
    }

    @Test
    fun conversationFragment_loadsWithoutError() {
        homeRobot
            .setSpotId(SPOT_ID)
            .setPostId(POST_ID)
            .clickUIFlows()

        uiFlowsRobot
            .verifyFlowsMenuDisplayed()
            .clickConversationIntent()

        waitForSdkScreen()
        // If we reach here without crash, the conversation fragment loaded successfully
    }

    @Test
    fun preConversationFragment_loadsWithoutError() {
        homeRobot
            .setSpotId(SPOT_ID)
            .setPostId(POST_ID)
            .clickUIFlows()

        uiFlowsRobot
            .verifyFlowsMenuDisplayed()
            .clickPreConversation()

        waitForSdkScreen()
        // If we reach here without crash, the pre-conversation fragment loaded successfully
    }

    @Test
    fun uiFlowsMenu_displaysAllOptions() {
        homeRobot.clickUIFlows()
        uiFlowsRobot.verifyFlowsMenuDisplayed()
    }

    companion object {
        private const val SPOT_ID = "sp_eCIlROSD"
        private const val POST_ID = "sdk1jkl"
    }
}
