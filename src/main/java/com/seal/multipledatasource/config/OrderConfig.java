package com.seal.multipledatasource.config;

import com.seal.multipledatasource.repo.order.OrderRepo;
import lombok.AllArgsConstructor;
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
        basePackageClasses = OrderRepo.class,
        entityManagerFactoryRef = "orderEntityManager",
        transactionManagerRef = "orderTransactionManager"
)
@PropertySource("classpath:application.properties")
@AllArgsConstructor
public class OrderConfig {

    private Environment env;

    @Bean("orderDataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("order.datasource.class"));
        dataSource.setUrl(env.getProperty("order.datasource.url"));
        dataSource.setUsername(env.getProperty("order.datasource.username"));
        dataSource.setPassword(env.getProperty("order.datasource.password"));
        return dataSource;
    }

    @Bean(name = "orderEntityManager")
    public LocalContainerEntityManagerFactoryBean getEntityManager(@Qualifier("orderDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.seal.multipledatasource.entity.order");

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("order.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("order.jpa.hibernate.dialect"));
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "orderTransactionManager")
    public PlatformTransactionManager getTransactionManager(@Qualifier("orderEntityManager")
                                                                        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        return transactionManager;
    }

}
