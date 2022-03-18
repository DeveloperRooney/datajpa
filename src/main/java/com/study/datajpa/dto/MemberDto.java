package com.study.datajpa.dto;

import com.study.datajpa.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDto {

    private Long id;
    private String memberName;
    private String teamName;

    public MemberDto(Long id, String memberName, String teamName) {
        this.id = id;
        this.memberName = memberName;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.memberName = member.getMemberName();
    }
}
