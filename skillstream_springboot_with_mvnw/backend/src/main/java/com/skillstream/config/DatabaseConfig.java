package com.skillstream.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_USERNAME:}")
    private String dbUsername;

    @Value("${DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        logger.info("Initializing DataSource...");
        logger.info("DATABASE_URL is set: {}", databaseUrl != null && !databaseUrl.isEmpty());
        logger.info("DB_USERNAME is set: {}", dbUsername != null && !dbUsername.isEmpty());
        logger.info("DB_PASSWORD is set: {}", dbPassword != null && !dbPassword.isEmpty());
        
        String jdbcUrl;
        String username;
        String password;
        
        // If DATABASE_URL is provided and doesn't start with jdbc, parse it
        if (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.startsWith("jdbc:")) {
            try {
                logger.info("Parsing DATABASE_URL: {}", databaseUrl.replaceAll(":[^:@]+@", ":****@"));
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
                logger.info("Constructed JDBC URL: jdbc:{}://{}:{}/{}", scheme, host, port, database);
            } catch (Exception e) {
                logger.error("Error parsing DATABASE_URL: {}", e.getMessage());
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
            logger.info("Using DATABASE_URL as-is (JDBC format)");
            jdbcUrl = databaseUrl;
            username = dbUsername;
            password = dbPassword;
        } else {
            // Try to construct from individual components
            logger.warn("DATABASE_URL is not set. Attempting to use DB_USERNAME/DB_PASSWORD with default host.");
            // This will likely fail, but let's try
            throw new IllegalStateException("DATABASE_URL environment variable is required but not set. Please configure it in Render dashboard.");
        }
        
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            throw new IllegalStateException("JDBC URL cannot be empty. Check DATABASE_URL environment variable.");
        }
        
        logger.info("Creating DataSource with URL: {}", jdbcUrl.replaceAll(":[^:@]+@", ":****@"));
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

