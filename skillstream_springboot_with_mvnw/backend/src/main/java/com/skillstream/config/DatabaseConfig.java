package com.skillstream.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_USERNAME:}")
    private String dbUsername;

    @Value("${DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String jdbcUrl;
        String username;
        String password;
        
        // If DATABASE_URL is provided and doesn't start with jdbc, parse it
        if (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.startsWith("jdbc:")) {
            try {
                // Parse postgresql://user:password@host:port/database
                URI uri = new URI(databaseUrl);
                String scheme = uri.getScheme();
                String userInfo = uri.getUserInfo();
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String database = uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
                
                // Extract username and password from userInfo or use separate variables
                if (userInfo != null && !userInfo.isEmpty()) {
                    String[] userPass = userInfo.split(":");
                    username = userPass.length >= 1 ? userPass[0] : dbUsername;
                    password = userPass.length >= 2 ? userPass[1] : dbPassword;
                } else {
                    username = dbUsername;
                    password = dbPassword;
                }
                
                // Construct JDBC URL
                jdbcUrl = String.format("jdbc:%s://%s:%d/%s", scheme, host, port, database);
            } catch (Exception e) {
                // Fallback: try to convert postgresql:// to jdbc:postgresql://
                if (databaseUrl.startsWith("postgresql://")) {
                    jdbcUrl = "jdbc:" + databaseUrl;
                } else {
                    jdbcUrl = databaseUrl;
                }
                username = dbUsername;
                password = dbPassword;
            }
        } else if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Already in JDBC format
            jdbcUrl = databaseUrl;
            username = dbUsername;
            password = dbPassword;
        } else {
            // No DATABASE_URL, use default (will fail but that's expected)
            jdbcUrl = "";
            username = dbUsername;
            password = dbPassword;
        }
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

