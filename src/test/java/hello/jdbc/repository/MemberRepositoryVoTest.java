package hello.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
class MemberRepositoryVoTest {

    MemberRepositoryVo repositoryVo = new MemberRepositoryVo();

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV3", 10000);
        repositoryVo.save(member);
        Member findMember = repositoryVo.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        assertThat(findMember).isEqualTo(member);
    }

}