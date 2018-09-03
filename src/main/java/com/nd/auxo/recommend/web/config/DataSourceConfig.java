package com.nd.auxo.recommend.web.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.nd.gaea.core.config.ConfigConstant;
import com.nd.gaea.core.utils.PropertiesLoaderUtils;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * <p>数据源配置</p>
 * 从${classpath}/config/database.properties中取得相应的参数
 * <p/>
 * C3P0未在database.properties中配置的参数可以通过/config/c3p0.properties来配置
 *
 * @author bifeng.liu
 * @since 2015/9/30.
 */
@Configuration
public class DataSourceConfig {

    @Value("${profile.mysql.jdbc.driver}")
    private String driverClass;
    @Value("${profile.mysql.jdbc.url}")
    private String jdbcUrl;
    @Value("${profile.mysql.jdbc.username}")
    private String userName;
    @Value("${profile.mysql.jdbc.password}")
    private String password;

    @Value("${c3p0.initialPoolSize}")
    private int initialPoolSize;
    @Value("${c3p0.minPoolSize}")
    private int minPoolSize;
    @Value("${c3p0.maxPoolSize}")
    private int maxPoolSize;
    @Value("${c3p0.acquireIncrement}")
    private int acquireIncrement;
    @Value("${c3p0.maxIdleTime}")
    private int maxIdleTime;
    @Value("${c3p0.idleConnectionTestPeriod}")
    private int idleConnectionTestPeriod;
    @Value("${c3p0.checkoutTimeout}")
    private int checkoutTimeout;
    @Value("${c3p0.maxStatements}")
    private int maxStatements;
    @Value("${c3p0.numHelperThreads}")
    private int numHelperThreads;
    @Value("${c3p0.testConnectionOnCheckout}")
    private boolean testConnectionOnCheckout;
    @Value("${c3p0.preferredTestQuery}")
    private String preferredTestQuery;

    @Bean(name = "dataSource", destroyMethod = "close")
    public DataSource ComboPooledDataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClass);

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(userName);
        dataSource.setPassword(password);

        dataSource.setInitialPoolSize(initialPoolSize);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        dataSource.setCheckoutTimeout(checkoutTimeout);
        dataSource.setMaxStatements(maxStatements);
        dataSource.setNumHelperThreads(numHelperThreads);
        dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
        dataSource.setPreferredTestQuery(preferredTestQuery);
        return dataSource;
    }
}
