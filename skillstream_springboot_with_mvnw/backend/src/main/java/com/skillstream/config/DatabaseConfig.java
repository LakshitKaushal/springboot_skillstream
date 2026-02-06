package com.skillstream.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
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
                
                // Extract username and password from userInfo
                String username = dbUsername;
                String password = dbPassword;
                
                if (userInfo != null && !userInfo.isEmpty()) {
                    String[] userPass = userInfo.split(":");
                    if (userPass.length >= 1) {
                        username = userPass[0];
                    }
                    if (userPass.length >= 2) {
                        password = userPass[1];
                    }
                }
                
                // Construct JDBC URL
                String jdbcUrl = String.format("jdbc:%s://%s:%d/%s", scheme, host, port, database);
                
                properties.setUrl(jdbcUrl);
                properties.setUsername(username);
                properties.setPassword(password);
            } catch (Exception e) {
                // Fallback: use DATABASE_URL as-is (might already be JDBC format)
                properties.setUrl(databaseUrl.startsWith("jdbc:") ? databaseUrl : "jdbc:" + databaseUrl);
                if (!dbUsername.isEmpty()) {
                    properties.setUsername(dbUsername);
                }
                if (!dbPassword.isEmpty()) {
                    properties.setPassword(dbPassword);
                }
            }
        } else if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Already in JDBC format
            properties.setUrl(databaseUrl);
            if (!dbUsername.isEmpty()) {
                properties.setUsername(dbUsername);
            }
            if (!dbPassword.isEmpty()) {
                properties.setPassword(dbPassword);
            }
        }
        
        return properties;
    }
}

