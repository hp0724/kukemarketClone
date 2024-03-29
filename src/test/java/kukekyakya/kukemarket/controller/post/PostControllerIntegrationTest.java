package kukekyakya.kukemarket.controller.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.post.PostReadCondition;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.init.TestInitDB;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import kukekyakya.kukemarket.repository.post.PostRepositoryTest;
import kukekyakya.kukemarket.service.post.PostService;
import kukekyakya.kukemarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Objects;

import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static kukekyakya.kukemarket.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static kukekyakya.kukemarket.factory.dto.SignInRequestFactory.createSignInRequest;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value="test")
@Transactional
public class PostControllerIntegrationTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestInitDB initDB;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    SignService signService;
    @Autowired
    PostService postService;

    Member member1,member2,admin;
    Category category;
    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
        member1=memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        member2=memberRepository.findByEmail(initDB.getMember2Email()).orElseThrow(MemberNotFoundException::new);
        admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(MemberNotFoundException::new);
        category = categoryRepository.findAll().get(0);
    }

    @Test
    void createTest() throws Exception{
        SignInResponse signInRes = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()));
        PostCreateRequest req = createPostCreateRequest("title", "content", 1000L, member1.getId(), category.getId(), List.of());

        mockMvc.perform(
                multipart("/api/posts")
                        .param("title",req.getTitle())
                        .param("content",req.getContent())
                        .param("price",String.valueOf(req.getPrice()))
                        .param("categoryId",String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization",signInRes.getAccessToken()))
                .andExpect(status().isCreated());

        Post post =postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getMember().getId()).isEqualTo(member1.getId());
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception{
        PostCreateRequest req = createPostCreateRequest("title","content",1000L,member1.getId(),category.getId(),List.of());
        mockMvc.perform(
                multipart("/api/posts")
                        .param("title",req.getTitle())
                        .param("content",req.getContent())
                        .param("price",String.valueOf(req.getPrice()))
                        .param("categoryId",String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor->{
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));

    }
    @Test
    void readTest() throws Exception {
        Post post =postRepository.save(createPost(member1,category));
        mockMvc.perform(
                get("/api/posts/{id}",post.getId())
          ).andExpect(status().isOk());
    }

    @Test
    void deleteByResourceOwnerTest() throws Exception {
        // given
        Post post = postRepository.save(createPost(member1, category));
        SignInResponse signInRes = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/posts/{id}", post.getId())
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> postService.read(post.getId())).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void deleteByAdminTest() throws Exception {
        Post post =postRepository.save(createPost(member1,category));
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(admin.getEmail(),initDB.getPassword()));

        mockMvc.perform(
                delete("/api/posts/{id}",post.getId())
                        .header("Authorization",adminSignInRes.getAccessToken())
               ).andExpect(status().isOk());

        assertThatThrownBy(()->postService.read(post.getId())).isInstanceOf(PostNotFoundException.class);
    }
    @Test
    void deleteAccessDeniedByNotResourceOwnerTest()throws Exception{
        Post post =postRepository.save(createPost(member1,category));
        SignInResponse notOwnerSignInRes = signService.signIn(createSignInRequest(member2.getEmail(),initDB.getPassword()));

        mockMvc.perform(
                delete("/api/posts/{id}",post.getId())
                        .header("Authorization",notOwnerSignInRes.getAccessToken())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest()throws Exception{
        Post post = postRepository.save(createPost(member1,category));
        mockMvc.perform(
                delete("/api/posts/{id}",post.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void updateByResourceOwnerTest()throws Exception{
        SignInResponse signInRes = signService.signIn(createSignInRequest(member1.getEmail(),initDB.getPassword()));
        Post post =postRepository.save(createPost(member1,category));
        String updatedTitle ="updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L ;

        mockMvc.perform(
                multipart("/api/posts/{id}",post.getId())
                        .param("title",updatedTitle)
                        .param("content",updatedContent)
                        .param("price",String.valueOf(updatedPrice))
                        .with(requestPostProcessor->{
                            requestPostProcessor.setMethod("PUT");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization",signInRes.getAccessToken()))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        assertThat(updatedPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedPost.getContent()).isEqualTo(updatedContent);
        assertThat(updatedPost.getPrice()).isEqualTo(updatedPrice);
    }

    @Test
    void updateByAdminTest()throws Exception{
        SignInResponse signInRes = signService.signIn(createSignInRequest(admin.getEmail(),initDB.getPassword()));
        Post post =postRepository.save(createPost(member1,category));
        String updatedTitle ="updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L ;

        mockMvc.perform(
                        multipart("/api/posts/{id}",post.getId())
                                .param("title",updatedTitle)
                                .param("content",updatedContent)
                                .param("price",String.valueOf(updatedPrice))
                                .with(requestPostProcessor->{
                                    requestPostProcessor.setMethod("PUT");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Authorization",signInRes.getAccessToken()))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        assertThat(updatedPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedPost.getContent()).isEqualTo(updatedContent);
        assertThat(updatedPost.getPrice()).isEqualTo(updatedPrice);
    }

    @Test
    void updateByUnauthorizedByNoneTokenTest()throws Exception{
        Post post =postRepository.save(createPost(member1,category));
        String updatedTitle ="updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L ;

        mockMvc.perform(
                        multipart("/api/posts/{id}",post.getId())
                                .param("title",updatedTitle)
                                .param("content",updatedContent)
                                .param("price",String.valueOf(updatedPrice))
                                .with(requestPostProcessor->{
                                    requestPostProcessor.setMethod("PUT");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));


    }
    @Test
    void readAllTest()throws Exception{
        PostReadCondition cond = createPostReadCondition(0,1);

        mockMvc.perform(
                get("/api/posts")
                        .param("page",String.valueOf(cond.getPage())).param("size",String.valueOf(cond.getSize()))
                        .param("categoryId",String.valueOf(1),String.valueOf(2))
                        .param("memberId",String.valueOf(1),String.valueOf(2))
        ).andExpect(status().isOk());

    }




}
