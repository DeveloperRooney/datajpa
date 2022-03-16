package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "memberName");

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<Member> map = page.map(o -> new Member(o.getMemberName(), o.getAge(), null));

        List<Member> content = page.getContent();
//        long totalCount = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("memberA", 10));
        memberRepository.save(new Member("memberB", 19));
        memberRepository.save(new Member("memberC", 20));
        memberRepository.save(new Member("memberD", 21));
        memberRepository.save(new Member("memberE", 30));

        int resultCount = memberRepository.bulkAgePlus(20);

        Member result = memberRepository.findByMemberName("memberE");

        System.out.println("memberE = " + result);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        // given
        // memberA -> teamA
        // memberB -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member memberA = new Member("memberA", 20, teamA);
        Member memberB = new Member("memberB", 30, teamB);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByMemberName("memberA");

        for (Member member : members) {
            System.out.println("member = " + member.getMemberName());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }
}