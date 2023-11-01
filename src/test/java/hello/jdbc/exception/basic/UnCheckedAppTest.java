package hello.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/*
*  체크 예외의 경우(SQL, IOE)
*  Exception 클래스를 만들어서  Throwable을 던진다 (unCheck예외로 변환)
*  이후 repository에서 try catch로 잡으면 Service와 Controller에서 오류처리를 할 필요가 없다.
*
*  런타임 예외의 경우에는 문서화를 잘해야한다 @throws ~~ 보통 라이브러리들은 문서화되어있다.
* */
@Slf4j
public class UnCheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(()-> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e);
        }

    }

    static class Controller {

        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() {
            throw new RunTimeConnectException("연결 실패");
        }

    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RunTimeConnectException extends RuntimeException {
        public RunTimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }

    }
}
