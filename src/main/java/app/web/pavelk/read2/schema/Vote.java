package app.web.pavelk.read2.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vote", schema = "post")
public class Vote {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long voteId;

    private VoteType voteType;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
