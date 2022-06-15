package com.epam.esm.core.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * The type Test database config.
 */
@Configuration
@ComponentScan({"com.epam.esm.core.service", "com.epam.esm.core.dao"})
@EnableTransactionManagement
@Profile("test")
public class TestDatabaseConfig {
    private static final String DATABASE_SCHEMA = "classpath:db-schema.sql";
    private static final String DATABASE_DATA = "classpath:db-test-data.sql";
    private static final String DATABASE_URL_PARAMETERS= "testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE";

    /**
     * Data source embedded database.
     *
     * @return the embedded database
     */
    @Bean(destroyMethod = "shutdown")
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2).
                setName(DATABASE_URL_PARAMETERS).
                addScript(DATABASE_SCHEMA).
                addScript(DATABASE_DATA).
                build();

    }

    /**
     * Jdbc template jdbc template.
     *
     * @param embeddedDatabase the embedded database
     * @return the jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate(EmbeddedDatabase embeddedDatabase) {
        return new JdbcTemplate(embeddedDatabase);
    }


    /**
     * Data source transaction manager data source transaction manager.
     *
     * @param embeddedDatabase the embedded database
     * @return the data source transaction manager
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(EmbeddedDatabase embeddedDatabase) {
        return new DataSourceTransactionManager(embeddedDatabase);
    }
}
