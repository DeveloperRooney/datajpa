package com.study.datajpa.entity;

import com.study.datajpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("memberA", 10, teamA);
        Member memberB = new Member("memberA", 20, teamA);
        Member memberC = new Member("memberB", 30, teamB);
        Member memberD = new Member("memberB", 40, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        em.flush();
        em.clear();

        List<Member> resultList = em.createQuery(
                        "select m from Member m", Member.class)
                .getResultList();

        for (Member member : resultList) {
            System.out.println("name : " + member.getMemberName());
            System.out.println("team : " + member.getTeam().getName());
        }
    }

    @Test
    public void JpaEventBaseEntity() throws Exception{
        Member member = new Member("memberA");
        memberRepository.save(member); // @PrePersist

        Thread.sleep(100);
        member.setMemberName("memberAA");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("member created = " + findMember.getCreatedDate());
        System.out.println("member updated = " + findMember.getLastModifiedDate());

    }


}