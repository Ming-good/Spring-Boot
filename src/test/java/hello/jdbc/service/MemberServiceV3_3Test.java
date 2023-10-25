package hello.jdbc.service;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
//스프링 컨테이너를 쓰겠다.
@SpringBootTest // 스프링 AOP를 적용하려면 스프링 컨테이너가 필요하다.
                // 이 애노테이션이 있으면 테스트시 스프링 부트를 통해 스프링 컨테이너를 생성한다.
                // 그리고 테스트에서 @Autowired 등을 통해 스프링 컨테이너가 관리하는 빈들을 사용할 수 있다.
class MemberServiceV3_3Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "memberEX";

    @Autowired
    private MemberRepositoryV3 memberRepository;

    @Autowired
    private  MemberServiceV3_3 memberServiceV3_2;

    // 스프링 빈을 등록하게 하겠다.
    @TestConfiguration  // 테스트 안에서 내부 설정 클래스를 만들어서 사용하면서 이 애노테이션을 붙이면,
                        // 스프링 부트가 자동으로 만들어주는 빈들에 추가로 필요한 스프링 빈들을 등록하고 테스트를 수행할 수 있다.
    static class TestConfig{

        // 프록시에서 트랜잭션 시작할 때 사용
        @Bean
        DataSource dataSource() {
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }

        // 프록시에서 트랜잭션 시작할 때 사용
        @Bean
        PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        MemberRepositoryV3 memberRepositoryV3() {

            return new MemberRepositoryV3(dataSource());
        }

        @Bean
        MemberServiceV3_3 memberServiceV33() {
            return new MemberServiceV3_3(memberRepositoryV3());
        }
    }


    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws Exception {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberServiceV3_2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //when
        assertThatThrownBy(() -> memberServiceV3_2.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(),2000)).isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberEx = memberRepository.findById(memberEx.getMemberId());

        //memberA의 돈이 롤백 되어야함
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }

    @Test
    void AopCheck() {
        log.info("memberService class={}", memberServiceV3_2.getClass());
        log.info("memberRepository class={}", memberRepository.getClass());

        Assertions.assertThat(AopUtils.isAopProxy(memberServiceV3_2)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }
}