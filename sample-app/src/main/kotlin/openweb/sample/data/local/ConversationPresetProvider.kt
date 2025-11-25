package openweb.sample.data.local

import android.content.Context
import java.io.InputStream

/**
 * Provides access to conversation presets JSON for the current build variant.
 * Each variant has its own JSON file in the assets folder.
 */
object ConversationPresetProvider {

    private const val PRESETS_FILE_NAME = "conversation_presets.json"

    /**
     * Returns an InputStream to read the conversation presets JSON file
     * for the current build variant.
     */
    fun getPresetsJson(context: Context): InputStream = context.assets.open(PRESETS_FILE_NAME)

    /**
     * Returns the raw JSON string of conversation presets.
     */
    fun getPresetsJsonString(context: Context): String = getPresetsJson(context).bufferedReader().use { it.readText() }
}
