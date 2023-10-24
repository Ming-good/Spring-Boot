package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/*
* 트랜잭션 - 트랜잭션 템플릿
* */
public class MemberServiceV3_2 {

//    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionTemplate transactionTemplate;
    private final MemberRepositoryV3 memberRepositoryV3;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager,
            MemberRepositoryV3 memberRepositoryV3) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.memberRepositoryV3 = memberRepositoryV3;
    }

    public void accountTransfer(String formId, String toId, int money) throws Exception {
        transactionTemplate.executeWithoutResult((status) -> { // 언체크(런타임) 예외의 경우에 롤백한다.
            try {
                bizLogic(formId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV3.findById(fromId);
        Member toMemeber = memberRepositoryV3.findById(toId);

        memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
        memberRepositoryV3.update(toId, toMemeber.getMoney() + money);
    }
}
