package org.mutagen.backend.config

import com.clickhouse.client.config.ClickHouseDefaults
import com.clickhouse.jdbc.ClickHouseDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.sql.DataSource

@Configuration
open class ClickhouseConfig {

    @Value("\${ch.url}")
    private lateinit var url: String

    @Value("\${ch.username}")
    private lateinit var username: String

    @Value("\${ch.password}")
    private lateinit var password: String

    @Bean
    open fun dataSource(): DataSource {
        val props = Properties().also {
            it[ClickHouseDefaults.USER.key] = username
            it[ClickHouseDefaults.PASSWORD.key] = password
        }
        return ClickHouseDataSource(url, props)
    }
}