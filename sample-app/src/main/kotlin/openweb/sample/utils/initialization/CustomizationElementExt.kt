package openweb.sample.utils.initialization

import spotIm.common.api.model.customizations.OWCustomizationCustomTextElement
import spotIm.common.api.model.customizations.OWCustomizationElement
import spotIm.common.api.model.customizations.OWCustomizationTextElement
import spotIm.common.api.model.customizations.OWCustomizationViewElement

internal fun OWCustomizationCustomTextElement.reset() {
    color = null; fontFamily = null; fontWeight = null; customizeView = null
}

internal fun OWCustomizationViewElement<*>.reset() {
    customizeView = null
}

internal fun OWCustomizationTextElement.reset() {
    color = null; fontFamily = null; fontWeight = null
}

internal fun OWCustomizationElement.reset() {
    color = null
}
