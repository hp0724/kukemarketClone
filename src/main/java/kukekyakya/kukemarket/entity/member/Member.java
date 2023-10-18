package kukekyakya.kukemarket.entity.member;

import jakarta.persistence.*;
import kukekyakya.kukemarket.entity.common.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//3

public class Member  extends EntityDate { //5
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false,length = 30,unique = true) //1
    private String email;
    private String password; //2

    @Column(nullable = false,length = 20)
    private String username;

    @Column(nullable = false,unique = true,length = 20)//1
    private String nickname;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)//4
    private Set<MemberRole> roles;

    public Member(String email, String password, String username, String nickname, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles.stream().map(r -> new MemberRole(this, r)).collect(toSet());
    }

    public void updateNickname(String nickname){// 6
        this.nickname = nickname;
    }

}
