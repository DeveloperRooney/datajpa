package com.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"team"})
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_age")
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String memberName) {
        this.memberName = memberName;
    }

    public Member(String name, int age, Team team) {
        this.memberName = name;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        if(this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);
    }
}
