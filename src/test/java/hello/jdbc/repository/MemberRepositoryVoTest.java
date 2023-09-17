package hello.jdbc.repository;

import static org.junit.jupiter.api.Assertions.*;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class MemberRepositoryVoTest {

    MemberRepositoryVo repositoryVo = new MemberRepositoryVo();

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberVo", 10000);
        repositoryVo.save(member);
    }
}