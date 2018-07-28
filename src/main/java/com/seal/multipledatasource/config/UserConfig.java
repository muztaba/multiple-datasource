package com.seal.multipledatasource.config;

import com.seal.multipledatasource.repo.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackageClasses = UserRepo.class,
        entityManagerFactoryRef = "userEntityManager",
        transactionManagerRef = "userTransactionManager"
)
@PropertySource("classpath:application.properties")
public class UserConfig {

    @Autowired
    Environment env;

    @Bean
    @Primary
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("user.datasource.class"));
        dataSource.setUrl(env.getProperty("user.datasource.url"));
        dataSource.setUsername(env.getProperty("user.datasource.username"));
        dataSource.setPassword(env.getProperty("user.datasource.password"));
        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean getEntityManager() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(getDataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.seal.multipledatasource.entity.user");

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("user.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("user.jpa.hibernate.dialect"));
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    @Primary
    public PlatformTransactionManager getTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getEntityManager().getObject());
        return transactionManager;
    }
}
