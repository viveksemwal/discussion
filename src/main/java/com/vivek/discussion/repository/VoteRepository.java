package com.vivek.discussion.repository;

import com.vivek.discussion.model.Post;
import com.vivek.discussion.model.User;
import com.vivek.discussion.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
