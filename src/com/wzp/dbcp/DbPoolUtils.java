package com.wzp.dbcp;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DbPoolUtils {
    private String userName;
    private String password;
    private String jdbcUrl;
    private volatile DataSource dataSource;
    private static DbPoolUtils dbPool;

    /**
     * @param userName
     *            数据库连接用户名
     * @param password
     *            数据库连接密码
     * @param jdbcUrl
     *            mysql的jdbc连接url:jdbc:mysql://localhost:3306/jdbcstudy
     * @return
     */
    public synchronized static DbPoolUtils getInstance(String userName,
                                                       String password,
                                                       String jdbcUrl) {
        if (dbPool == null) {
            dbPool = new DbPoolUtils(userName, password, jdbcUrl);
        }
        return dbPool;
    }

    private DbPoolUtils(String userName, String password, String jdbcUrl) {
        this.userName = userName;
        this.password = password;
        this.jdbcUrl = jdbcUrl;
        DataSource appDataSource = new DataSource();
        try {
            appDataSource.setUsername(userName);
            appDataSource.setPassword(password);
            appDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            appDataSource.setUrl(jdbcUrl);
            appDataSource.setValidationQuery("SELECT 1");
            appDataSource.setInitialSize(5); // waiting after initialized
            appDataSource.setMinIdle(5); // always keep 10
            appDataSource.setMaxIdle(20); // max waiting, control return to
                                          // waiting
            // or close
            appDataSource.setMaxActive(100); // max using (also max holding)
            appDataSource.setMaxWait(10 * 1000); // if maxActive reach, wait 10s
            appDataSource.setTestWhileIdle(true); // when adjusting pool, close
                                                  // if idle 30m, and
            // also is broken
            appDataSource.setTestOnBorrow(false); // to avoid too much SELECT 1
            appDataSource.setNumTestsPerEvictionRun(3); // when adjusting pool,
                                                        // check 3
            // connections per run, so max close or
            // create 3
            appDataSource.setMinEvictableIdleTimeMillis(30 * 60 * 1000); // when
                                                                         // adjusting
                                                                         // pool
            // size, idle 30m
            // will be closed
            appDataSource.setTimeBetweenEvictionRunsMillis(10 * 1000); // adjust
                                                                       // pool
                                                                       // size
                                                                       // every
            // 10s
            appDataSource.setLogAbandoned(true); // to check if app has
                                                 // connection leak, if yes,
            // log it
            appDataSource.setRemoveAbandoned(true); // to check if app has
                                                    // connection leak, if
            // yes, force to return it
            appDataSource.setRemoveAbandonedTimeout(300); // to check if app has
                                                          // connection
            dataSource = appDataSource;
            dataSource.getConnection().close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        try {
            if (dataSource != null) {
                return dataSource.getConnection();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
