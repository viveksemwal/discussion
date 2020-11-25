package com.vivek.discussion.service;


import com.vivek.discussion.dto.PostRequest;
import com.vivek.discussion.dto.PostResponse;
import com.vivek.discussion.exceptions.SpringDiscussionException;
import com.vivek.discussion.mapper.PostMapper;
import com.vivek.discussion.model.Post;
import com.vivek.discussion.model.Subreddit;
import com.vivek.discussion.model.User;
import com.vivek.discussion.repository.PostRepository;
import com.vivek.discussion.repository.SubredditRepository;
import com.vivek.discussion.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final UserRepository userRepository;


    public Post save(PostRequest postRequest){

        Subreddit subreddit=subredditRepository.findByName(postRequest.getSubredditName()).orElseThrow(()->
                new SpringDiscussionException("No subreddit not found"));
        log.info("hey2");
        User user=authService.getCurrentUser();
        log.info("hey3" + user.getUsername());
        Post post=postRepository.save(postMapper.map(postRequest,subreddit,user));
        log.info("hey4");
        return post;

    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SpringDiscussionException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SpringDiscussionException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringDiscussionException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }



}
