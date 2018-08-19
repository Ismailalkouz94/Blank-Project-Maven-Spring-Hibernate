package com.mycompany.spring.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.mycompany"})
@PropertySource(value= {"classpath:application.properties"})
@EnableTransactionManagement
public class HibernateConfig {

//    // Change the below based on the DBMS you choose
//    //Oracle DataBase
//	private final static String DATABASE_URL = "jdbc:oracle:thin:@127.0.0.1:1521/EDGE";
//	private final static String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";
//	private final static String DATABASE_DIALECT = "org.hibernate.dialect.OracleDialect";
//	private final static String DATABASE_USERNAME = "edge";
//	private final static String DATABASE_PASSWORD = "edge";
//

    // MySql DataBase
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/BillerDB";
    private final static String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private final static String DATABASE_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private final static String DATABASE_USERNAME = "root";
    private final static String DATABASE_PASSWORD = "";

    // dataSource bean will be available
    @Bean("dataSource")
    public DataSource getDataSource() {

        BasicDataSource dataSource = new BasicDataSource();

        // Providing the database connection information
        dataSource.setDriverClassName(DATABASE_DRIVER);
        dataSource.setUrl(DATABASE_URL);
        dataSource.setUsername(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);

        return dataSource;

    }

    // sessionFactory bean will be available
    @Bean
    public SessionFactory getSessionFactory(DataSource dataSource) {

        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource);

        builder.addProperties(getHibernateProperties());
        builder.scanPackages("com.mycompany.spring.dto");

        return builder.buildSessionFactory();

    }

    // All the hibernate properties will be returned in this method	
    private Properties getHibernateProperties() {

        Properties properties = new Properties();
        properties.put("hibernate.dialect", DATABASE_DIALECT);
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "false");
        properties.put("hibernate.hbm2ddl.auto", "update");

        return properties;
    }

    // transactionManager bean
    @Bean
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
