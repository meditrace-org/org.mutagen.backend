package org.mutagen.backend.flow

import org.springframework.context.annotation.Configuration
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

@Configuration
open class SearchFlow: BaseFlowConfiguration(SearchFlow::class) {

    override fun FlowBuilder.buildFlow() {
        sequence {
            // TODO
        }
    }
}