package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepositoryV1;
    public void accountTransfer(String formId, String toId, int money) {
        Member formMember = memberRepositoryV1.findById(formId);
        Member byId = memberRepositoryV1.findById(toId);

    }
}
