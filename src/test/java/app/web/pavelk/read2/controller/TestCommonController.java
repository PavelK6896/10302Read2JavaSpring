package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.PostgresContainer;
import app.web.pavelk.read2.Read2;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(classes = Read2.class)
@AutoConfigureMockMvc(addFilters = false)
abstract class TestCommonController extends PostgresContainer {

    final static String username = "usernameTest";
    final static String password = "as!@AS123Test";
    final static String email = "sa837@test.com";
    final static String username1 = "createPost1Right";
    final static String username2 = "getAllPosts1Right";
    final static String username3 = "getPost1Right";
    final static String username5 = "getPostsBySubreddit1Right";
    final static String username6 = "getPostsByUsername1Right";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    MailService mailService;


    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    SubReadRepository subReadRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    @Transactional
    void clearBase() {

        voteRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        subReadRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        userDetailsService.getUserMap().clear();

    }

}
