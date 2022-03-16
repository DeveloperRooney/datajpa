package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Test
    public void testMember() {
        Member member = new Member("hancoding");
        Member savedMember = repository.save(member);

        Member findMember = repository.findById(member.getId())
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
        repository.save(memberA);
        repository.save(memberB);

        // when
        Optional<Member> findMemberA = repository.findById(memberA.getId());
        Optional<Member> findMemberB = repository.findById(memberB.getId());

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
        repository.save(memberA);
        repository.save(memberB);

        List<Member> result = repository.findByMemberNameAndAgeGreaterThan("memberA", 15);

        assertThat(result.get(0).getMemberName())
                .isEqualTo("memberA");
        assertThat(result.get(0).getAge())
                .isEqualTo(20);
    }
}