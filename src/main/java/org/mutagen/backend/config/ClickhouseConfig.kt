package org.mutagen.backend.config

import cc.blynk.clickhouse.BalancedClickhouseDataSource
import cc.blynk.clickhouse.ClickHouseConnection
import cc.blynk.clickhouse.ClickHouseDataSource
import cc.blynk.clickhouse.settings.ClickHouseProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ClickhouseConfig {

    @Value("\${spring.datasource.url}")
    private lateinit var url: String

    @Value("\${spring.datasource.username}")
    private lateinit var username: String

    @Value("\${spring.datasource.password}")
    private lateinit var password: String

    @Bean
    open fun properties() = ClickHouseProperties().also {
        it.user = username
        it.password = password
    }

    @Bean
    open fun dataSource (properties: ClickHouseProperties) = BalancedClickhouseDataSource(url, properties)

    @Bean
    open fun connection(dataSource: BalancedClickhouseDataSource): ClickHouseConnection = dataSource.connection
}