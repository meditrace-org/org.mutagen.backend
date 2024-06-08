package org.mutagen.backend.config

import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import ru.mephi.sno.libs.flow.config.BaseFlowConfiguration

@Component
class FlowConfig(private val applicationContext: ApplicationContext) {

    @PostConstruct
    fun buildFlows() {
        val flowBeans = applicationContext.getBeansOfType(BaseFlowConfiguration::class.java)
        flowBeans.values.forEach {
            it.flowBuilder()
        }
    }
}
