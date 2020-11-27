package com.vivek.discussion.repository;

import com.vivek.discussion.model.Comment;
import com.vivek.discussion.model.Post;
import com.vivek.discussion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
