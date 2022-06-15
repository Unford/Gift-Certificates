package com.epam.esm.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * The type Database config.
 */
@Configuration
@ComponentScan("com.epam.esm.core")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy
@PropertySource("classpath:database.properties")
@Profile("prod")
public class DatabaseConfig {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String JDBC_URL = "jdbcUrl";
    private static final String DRIVER_CLASS = "jdbcDriver";
    private static final String CACHE_PREP_STMTS = "cachePrepStmts";
    private static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    private static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";
    private static final String USE_SERVER_PREP_STMTS = "useServerPrepStmts";
    private static final String USE_LOCAL_SESSION_STATE = "useLocalSessionState";
    private static final String REWRITE_BATCHED_STATEMENTS = "rewriteBatchedStatements";
    private static final String CACHE_RESULT_SET_METADATA = "cacheResultSetMetadata";
    private static final String CACHE_SERVER_CONFIGURATION = "cacheServerConfiguration";
    private static final String ELIDE_SET_AUTO_COMMITS = "elideSetAutoCommits";
    private static final String MAINTAIN_TIME_STATS = "maintainTimeStats";

    private final Environment environment;

    /**
     * Instantiates a new Database config.
     *
     * @param environment the environment
     */
    @Autowired
    public DatabaseConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(environment.getProperty(USERNAME));
        config.setPassword(environment.getProperty(PASSWORD));
        config.setJdbcUrl(environment.getProperty(JDBC_URL));
        config.setDriverClassName(environment.getProperty(DRIVER_CLASS));

        config.addDataSourceProperty(CACHE_PREP_STMTS, environment.getProperty(CACHE_PREP_STMTS));
        config.addDataSourceProperty(PREP_STMT_CACHE_SIZE, environment.getProperty(PREP_STMT_CACHE_SIZE));
        config.addDataSourceProperty(PREP_STMT_CACHE_SQL_LIMIT, environment.getProperty(PREP_STMT_CACHE_SQL_LIMIT));
        config.addDataSourceProperty(USE_SERVER_PREP_STMTS, environment.getProperty(USE_SERVER_PREP_STMTS));
        config.addDataSourceProperty(USE_LOCAL_SESSION_STATE, environment.getProperty(USE_LOCAL_SESSION_STATE));
        config.addDataSourceProperty(REWRITE_BATCHED_STATEMENTS, environment.getProperty(REWRITE_BATCHED_STATEMENTS));
        config.addDataSourceProperty(CACHE_RESULT_SET_METADATA, environment.getProperty(CACHE_RESULT_SET_METADATA));
        config.addDataSourceProperty(CACHE_SERVER_CONFIGURATION, environment.getProperty(CACHE_SERVER_CONFIGURATION));
        config.addDataSourceProperty(ELIDE_SET_AUTO_COMMITS, environment.getProperty(ELIDE_SET_AUTO_COMMITS));
        config.addDataSourceProperty(MAINTAIN_TIME_STATS, environment.getProperty(MAINTAIN_TIME_STATS));
        return new HikariDataSource(config);
    }


    /**
     * Jdbc template jdbc template.
     *
     * @param dataSource the data source
     * @return the jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Data source transaction manager data source transaction manager.
     *
     * @param dataSource the data source
     * @return the data source transaction manager
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
