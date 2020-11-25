package com.vivek.discussion.service;

import com.vivek.discussion.dto.SubredditDto;
import com.vivek.discussion.exceptions.SpringDiscussionException;
import com.vivek.discussion.mapper.SubredditMapper;
import com.vivek.discussion.model.Subreddit;
import com.vivek.discussion.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }


    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }


    public SubredditDto getById(long id) {
        Subreddit subreddit=subredditRepository.findById(id)
                .orElseThrow(()-> new SpringDiscussionException("No reddit found"));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}