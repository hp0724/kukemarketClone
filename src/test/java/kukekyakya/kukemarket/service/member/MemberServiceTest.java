package kukekyakya.kukemarket.service.member;

import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks MemberService memberService;
    @Mock
    MemberRepository memberRepository;

    @Test
    void readTest(){
        Member member =createMember();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        MemberDto result =memberService.read(1L);

        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void readExceptionByMemberNotFoundTests(){
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(()-> memberService.read(1L)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteTest() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(createMember()));
        memberService.delete(1L);
        verify(memberRepository).delete(any());
    }

    @Test

    void deleteExceptionByMemberNotFoundTest(){
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThatThrownBy(()->memberService.delete(1L)).isInstanceOf(MemberNotFoundException.class);
    }

}
