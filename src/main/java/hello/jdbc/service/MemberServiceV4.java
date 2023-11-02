package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

/*
*  예외 누수 문제 해결
*  SQL Exception 제거
*
*  MemberRepository 인터페이스에 의존
* */
public class MemberServiceV4 {

//    private final PlatformTransactionManager platformTransactionManager;
    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String formId, String toId, int money) throws Exception {
        bizLogic(formId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
        Member fromMember = memberRepository.findById(fromId);
        Member toMemeber = memberRepository.findById(toId);

        validation(toMemeber);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        memberRepository.update(toId, toMemeber.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("memberEX")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
