package com.megvii.configuration.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.SQLException;
/**
 * 此类注入数据源
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jdbc")
public class DataSourceConfig {

  @Value("${input.db.url}")
  private String url;

  @Value("${input.db.username}")
  private String username;

  @Value("${input.db.password}")
  private String password;

  @Value("${input.db.driver}")
  private String jdbcDriver;

  @Bean(name = "photoDataSource", destroyMethod = "close")
  @Qualifier("photoDataSource")
  public DataSource dataSource() throws SQLException {

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(jdbcDriver);
    hikariConfig.setJdbcUrl(url);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);

    hikariConfig.setMaximumPoolSize(5);
//    hikariConfig.setConnectionTestQuery("SELECT 1");
    hikariConfig.setPoolName("springHikariCP");

    hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
    hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

    HikariDataSource dataSource = new HikariDataSource(hikariConfig);

    return dataSource;
  }

}
