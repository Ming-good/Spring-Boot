package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/*
* 트랜잭션 - 트랜잭션 매니저
* */
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    private final PlatformTransactionManager platformTransactionManager;
    private final MemberRepositoryV3 memberRepositoryV3;
    public void accountTransfer(String formId, String toId, int money) throws Exception {
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            bizLogic(formId, toId, money);
            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            throw new Exception(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV3.findById(fromId);
        Member toMemeber = memberRepositoryV3.findById(toId);

        memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
        memberRepositoryV3.update(toId, toMemeber.getMoney() + money);
    }
}
