package kukekyakya.kukemarket.controller.member;

import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.init.TestInitDB;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//통합 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// local 과 충돌 방지를 위한 test 설정
@ActiveProfiles(value="test")
// 데이터베이스를 이용
@Transactional
public class MemberControllerIntegrationTest {
    @Autowired
    //MockMvc를 빌드하기 위해 WebApplicationContext를 주입
    WebApplicationContext context;
    @Autowired
    //API 요청을 보내고 테스트하기 위해 주입받습니다.
    MockMvc mockMvc;

    @Autowired
    TestInitDB initDB;
    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach(){
        //Spring Security를 활성화하기 위해, apply(springSecurity())를 호출
        mockMvc= MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
    }

    @Test
    //Get 모든 요청은 permitAll
    void readTest() throws Exception {
        Member member =memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        mockMvc.perform(
                get("/api/members/{id}",member.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception{
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(new SignInRequest(initDB.getMember1Email(),initDB.getPassword()));
        //로그인하여 발급받은 액세스 토큰을, Authorization 헤더에 포함해서 요청을 보냄
        mockMvc.perform(
                delete("/api/members/{id}",member.getId()).header("Authorization",signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByAdminTest() throws Exception {
        //관리자에 의한 삭제 요청
        Member member =memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse adminSignInRes = signService.signIn(new SignInRequest(initDB.getAdminEmail(),initDB.getPassword()));

        mockMvc.perform(
                delete("/api/members/{id}",member.getId()).header("Authorization",adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        //요청자를 인증할 수 있는 액세스 토큰이 Authorization 헤더에 포함되어있지 않습니다
        mockMvc.perform(
                delete("/api/members/{id}",member.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));

    }

    @Test
    void  deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        //member1 꺼를 member2 가 삭제하려는 상황
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse attackSignInRes = signService.signIn(new SignInRequest(initDB.getMember2Email(),initDB.getPassword()));
        //인증된 사용자가 자신의 자원이 아닌, 남의 자원에 접근하는 것을 요청
        mockMvc.perform(
                delete("/api/members/{id}",member.getId()).header("Authorization",attackSignInRes.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }
    //수정된 방식에서는 리프레시 토큰으로 요청하더라도, 인증할 수 있는 사용자로 판단하지 않기 때문에
    // Security가 관리해주는 컨텍스트에 사용자의 정보를 등록하지 않습니다




}
