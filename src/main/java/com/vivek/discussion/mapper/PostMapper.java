package com.vivek.discussion.mapper;


import com.vivek.discussion.dto.PostRequest;
import com.vivek.discussion.dto.PostResponse;
import com.vivek.discussion.model.Post;
import com.vivek.discussion.model.Subreddit;
import com.vivek.discussion.model.User;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    public Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
   // @Mapping(target = "commentCount", expression = "java(commentCount(post))")
  //  @Mapping(target = "duration", expression = "java(getDuration(post))")
    public PostResponse mapToDto(Post post);
}
