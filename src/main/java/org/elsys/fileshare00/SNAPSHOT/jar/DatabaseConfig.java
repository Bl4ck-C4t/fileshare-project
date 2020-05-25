package org.elsys.fileshare00.SNAPSHOT.jar;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class DatabaseConfig {
    @Value("${spring.datasource.url}")
    private String dbURL;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbURL);
        return new HikariDataSource(config);
    }
}
