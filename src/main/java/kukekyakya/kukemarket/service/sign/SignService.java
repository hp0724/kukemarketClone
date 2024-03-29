package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.config.token.TokenHelper;
import kukekyakya.kukemarket.dto.sign.RefreshTokenResponse;
import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.dto.sign.SignUpRequest;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.exception.*;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        memberRepository.save(SignUpRequest.
                toEntity(req,
                        roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                        passwordEncoder));
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        //member 없으면 LoginFailureException
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        //비밀번호 검사
        validatePassword(req, member);

        //id를 subject 저장
        String subject = createSubject(member);
        //id를 통해서 토큰 생성이고
        String accessToken = accessTokenHelper.createToken(subject);
        String refreshToken = refreshTokenHelper.createToken(subject);
        return new SignInResponse(accessToken, refreshToken);
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            //비밀번호 다르면 exception
            throw new LoginFailureException();
        }
    }
    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    public RefreshTokenResponse refreshToken(String rToken){
        validateRefreshToken(rToken);
        String subject =refreshTokenHelper.extractSubject(rToken);
        String accessToken = accessTokenHelper.createToken(subject);
        return new RefreshTokenResponse(accessToken);

    }

    private void validateRefreshToken(String rToken) {
        if(!refreshTokenHelper.validate(rToken)){
            throw new AuthenticationEntryPointException();
        }
    }


}
