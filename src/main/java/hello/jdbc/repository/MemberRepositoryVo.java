package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberRepositoryVo {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            return member;
        } catch (SQLException e) {
            throw e;
        }finally {
/*          pstmt.close(); //Exception 오류가 발생한다면 아래가 실행 안됨.
            con.close();*/
            close(con, pstmt, null);

        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("ERROR");
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("ERROR");
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("ERROR");
            }

        }
    }
    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
