package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 구현체인 JDBC 드라이버 객체를 가져온다
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }
}
