package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("hancoding");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        assertThat(findMember.getId())
                .isEqualTo(member.getId());

        assertThat(findMember.getMemberName())
                .isEqualTo(member.getMemberName());

    }

    @Test
    public void basicCRUD() throws Exception {
        // given
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        Optional<Member> findMemberA = memberRepository.findById(memberA.getId());
        Optional<Member> findMemberB = memberRepository.findById(memberB.getId());

        // then
        assertThat(findMemberA
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 데이터입니다."))
                .getMemberName())
                .isEqualTo(memberA.getMemberName());
    }
    
    @Test
    public void findByMemberNameAndAge() throws Exception {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberA", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByMemberNameAndAgeGreaterThan("memberA", 15);

        assertThat(result.get(0).getMemberName())
                .isEqualTo("memberA");
        assertThat(result.get(0).getAge())
                .isEqualTo(20);
    }

    @Test
    public void testQuery() {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> findMember = memberRepository.findMember("memberA", 20);

        assertThat(findMember.get(0)).isEqualTo(memberB);
    }

    @Test
    public void findByNames() {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        List<Member> memberNameList = memberRepository.findByMemberNames(Arrays.asList("memberA", "memberB"));

        for (Member member : memberNameList) {
            System.out.println("member : " + member.getMemberName());
        }

    }

    @Test
    public void dtoTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);
        memberA.changeTeam(teamA);
        memberB.changeTeam(teamB);
        memberRepository.save(memberA);
        memberRepository.save(memberB);


        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("=== name : " + dto.getMemberName() + " / team : " + dto.getTeamName());
        }
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        int offset = 1;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(6);

    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("memberA", 10));
        memberRepository.save(new Member("memberB", 19));
        memberRepository.save(new Member("memberC", 20));
        memberRepository.save(new Member("memberD", 21));
        memberRepository.save(new Member("memberE", 30));

        int resultCount = memberJpaRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);
    }
}