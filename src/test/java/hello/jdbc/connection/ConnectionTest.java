package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection connection =  DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection1={}, class={}", connection, connection, getClass());
        log.info("connection22={}, class={}", connection2, connection2, getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        //DriverMangerDataSource - 항상 새로운 커넥션을 획득
        // DriverManagerDataSource는 설정과 사용을 분리
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        log.info("connection={}, class={}", connection1, connection1, getClass());
        log.info("connection2={}, class={}", connection2, connection2, getClass());
    }
}