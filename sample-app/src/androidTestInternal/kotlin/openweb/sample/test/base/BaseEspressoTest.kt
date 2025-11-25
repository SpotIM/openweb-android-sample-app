package openweb.sample.test.base

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Base class for all Espresso regression tests.
 * Provides common setup and utility methods.
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseEspressoTest {

    protected val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    open fun setUp() {
        waitForSdkReady()
    }

    /**
     * Waits for the SDK to be ready before running tests.
     * Currently uses a simple delay — can be replaced with IdlingResource later.
     */
    protected fun waitForSdkReady() {
        Thread.sleep(SDK_READY_DELAY_MS)
    }

    /**
     * Waits for an SDK screen to finish loading after navigation.
     */
    protected fun waitForSdkScreen() {
        Thread.sleep(SDK_SCREEN_LOAD_DELAY_MS)
    }

    companion object {
        const val SDK_READY_DELAY_MS = 1000L
        const val SDK_SCREEN_LOAD_DELAY_MS = 3000L
    }
}
