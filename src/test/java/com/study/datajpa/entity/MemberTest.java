package com.study.datajpa.entity;

import org.junit.jupiter.api.Test;
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

}