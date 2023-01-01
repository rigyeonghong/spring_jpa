package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // jpa의 모든 데이터 변경 등 로직들은 transaction 안에서 수행되야함
@RequiredArgsConstructor // final 잡힌 애 생성자 인잭션 해줌
public class MemberService {

    private final MemberRepository memberRepository; // final 넣는 거 추천

//    @Autowired // 생성자 인잭션 : 좋은점) 테케 작성시 직접 주입하는 것들을 안놓치게 도와줌 // 생성자 하나면 자동으로 autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member){

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId(); // 영속성 컨텍스트는 key(id값: db pk), value
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
//    @Transactional(readOnly = true) // jpa가 조회하는 곳에서 좀 더 성능 최적화함. 읽기전용이면 리소스 많이 쓰지마.
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

//    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
