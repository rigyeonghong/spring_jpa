package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // spring 사용하니까 component scan에 의해 자동 스프링 빈으로 등록됨
//@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext // jpa 사용하기 때문에 jpa 제공 표준 어노테이션
    private EntityManager em; // spring이 만들어서 주입해줌

    public void save(Member member){
        em.persist(member); // 영속성 객체에 member entity 넣음 = transaction commit 되는 시점에 db에 반영됨 = db에 insert 쿼리 날라감
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // jpa의 find 메서드 사용
    };

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList(); //jpql : entity 객체 대상(from)으로 쿼리 보냄
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
