package com.leantech.util;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class SpringLiquibaseUtil {

    private SpringLiquibaseUtil() {
    }

    public static AsyncSpringLiquibase createAsyncSpringLiquibase(Environment env, Executor executor, DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase.setCloseDataSourceOnceMigrated(false);
            liquibase.setDataSource(liquibaseDataSource);
        } else {
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        }

        return liquibase;
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        } else {
            return liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null ? dataSource : null;
        }
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        Objects.requireNonNull(liquibaseProperties);
        Supplier<String> var10000 = liquibaseProperties::getUrl;
        Objects.requireNonNull(dataSourceProperties);
        String url = getProperty(var10000, dataSourceProperties::determineUrl);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getUser;
        Objects.requireNonNull(dataSourceProperties);
        String user = getProperty(var10000, dataSourceProperties::determineUsername);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getPassword;
        Objects.requireNonNull(dataSourceProperties);
        String password = getProperty(var10000, dataSourceProperties::determinePassword);
        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        return Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
    }
}
