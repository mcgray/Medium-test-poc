package ua.com.mcgray.test;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.mysql.management.MysqldResource;
import com.mysql.management.MysqldResourceI;
import org.apache.commons.io.FileUtils;
import org.apache.mina.util.AvailablePortFinder;

public class EmbeddedMysqlProvider {

    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    private static final File OUR_APP_DIR = new File(System.getProperty(JAVA_IO_TMPDIR));
    private static final File DATABASE_DIR = new File(OUR_APP_DIR, "mysql-mxj" + new Random().nextInt(1000));

    public static final String TEST_MYSQLD_THREAD_PARAM_NAME = "test-mysqld-thread";

    private static final String LAUNCHING_USER_KEY = "user";

    private String port;
    private String initializeUser;
    private String user;
    private String password;
    private String launchingUser;

    private static final String ERROR_MESSAGE = " property is not specified in mysql-provider.properties file";
    private String mysqlpid = "-1";


    public void start(String port, String initializeUser, String user, String password, String launchingUser) {
        startDatabase(port, initializeUser, user, password, launchingUser);
    }

    public void start(Properties properties) {
        assignProperties(properties);
        startDatabase(port, initializeUser, user, password, launchingUser);
    }

    /**
     * @Deprecated Do not stop DB manually, it will automatically stop with JVM
     * if you need to isolate your tests - use EmbeddedMysqlProvider.getConnection method to create new DB
     */
    public void stop() {
        internalStop();
    }

    void internalStop() {
        try {
            MysqldResource mysqldResource = new MysqldResource(DATABASE_DIR);
            mysqldResource.shutdown();
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                Runtime.getRuntime().exec("taskkill /F /PID " + EmbeddedMysqlProvider.this.mysqlpid);
            } else {
                Runtime.getRuntime().exec("kill -9 " + EmbeddedMysqlProvider.this.mysqlpid);
            }
            FileUtils.deleteDirectory(DATABASE_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(String dbName) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + dbName + "?createDatabaseIfNotExist=true", user, password);
    }

    private void startDatabase(String port, String initializeUser, String user, String password, String launchingUser) {

        if (Integer.valueOf(port).intValue() <= 0) {
            this.port = String.valueOf(AvailablePortFinder.getNextAvailable());
        } else {
            if (!AvailablePortFinder.available(Integer.valueOf(port))) {
                throw new IllegalArgumentException("Port " + port + " is not available to start mysql server!");
            }
            this.port = port;
        }

        this.initializeUser = initializeUser;
        this.user = user;
        this.password = password;
        this.launchingUser = launchingUser;

        MysqldResource mysqldResource = new MysqldResource(DATABASE_DIR);

        Map<String, String> database_options = new HashMap<String, String>();
        database_options.put(MysqldResourceI.PORT, this.port);
        database_options.put(MysqldResourceI.INITIALIZE_USER, this.initializeUser);
        database_options.put(MysqldResourceI.INITIALIZE_USER_NAME, this.user);
        database_options.put(MysqldResourceI.INITIALIZE_PASSWORD, this.password);
        database_options.put(LAUNCHING_USER_KEY, this.launchingUser);

        mysqldResource.start(TEST_MYSQLD_THREAD_PARAM_NAME, database_options);
        DATABASE_DIR.deleteOnExit();
        try {
            Field pid = mysqldResource.getClass().getDeclaredField("pid");
            pid.setAccessible(true);
            mysqlpid = (String) pid.get(mysqldResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    EmbeddedMysqlProvider.this.internalStop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (!mysqldResource.isRunning()) {
            throw new RuntimeException("MySQL not started");
        }
    }

    private void assignProperties(Properties properties) {

        String portKey = "mysql.port";
        port = properties.getProperty(portKey);
        if (port == null) {
            throw new RuntimeException(portKey + ERROR_MESSAGE);
        }

        String initUserKey = "mysql.initializeUser";
        initializeUser = properties.getProperty(initUserKey);
        if (initializeUser == null) {
            throw new RuntimeException(initUserKey + ERROR_MESSAGE);
        }

        String mysqlUserKey = "mysql.user";
        user = properties.getProperty(mysqlUserKey);
        if (user == null) {
            throw new RuntimeException(mysqlUserKey + ERROR_MESSAGE);
        }

        String mysqlPasswordKey =  "mysql.password";
        password = properties.getProperty(mysqlPasswordKey);
        if (password == null) {
            throw new RuntimeException(mysqlPasswordKey + ERROR_MESSAGE);
        }

        String launchingUserKey = "mysql.launchingUser";
        launchingUser = properties.getProperty(launchingUserKey);
        if (launchingUser == null) {
            throw new RuntimeException(launchingUserKey + ERROR_MESSAGE);
        }
    }

    public String getConnectionString() {
        return "jdbc:mysql://localhost:" + port + "/test?createDatabaseIfNotExist=true";
    }

    public int getPort() {
        return Integer.valueOf(port);
    }
}