package app.web.pavelk.read2.test;


import app.web.pavelk.read2.exceptions.PostNotFoundException;
import app.web.pavelk.read2.repository.CommentRepository;
import app.web.pavelk.read2.repository.PostRepository;
import app.web.pavelk.read2.repository.SubReadRepository;
import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneOffset;
import java.util.List;

@SpringBootTest
@Disabled("Test manual use.")
class GenerateDataTest {

    @Autowired
    public PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SubReadRepository subReadRepository;

    @Test
    void generateData() {

        Post post = postRepository.findById(1L).orElseThrow(() -> new PostNotFoundException("Test."));

        for (int i = 0; i < 1000; i++) {
            postRepository.save(Post.builder()
                    .postName("post name " + i)
                    .description("post description " + i)
                    .createdDate(post.getCreatedDate())
                    .subRead(post.getSubRead())
                    .user(post.getUser())
                    .build());
        }

        for (int i = 0; i < 1000; i++) {
            commentRepository.save(Comment.builder()
                    .text("comment " + i)
                    .createdDate(post.getCreatedDate().toInstant(ZoneOffset.UTC))
                    .post(post)
                    .user(post.getUser())
                    .build());
        }

        for (int i = 0; i < 1000; i++) {
            subReadRepository.save(SubRead.builder()
                    .name("sub " + i)
                    .description("d " + i)
                    .createdDate(post.getCreatedDate().toInstant(ZoneOffset.UTC))
                    .posts(List.of())
                    .user(post.getUser())
                    .build());
        }

        Assertions.assertTrue(postRepository.count() > 1000);
        Assertions.assertTrue(commentRepository.count() > 1000);
        Assertions.assertTrue(subReadRepository.count() > 1000);

    }

}
