package com.grabber.ljgrabber.db.service;

import com.grabber.ljgrabber.db.entity.Post;
import com.grabber.ljgrabber.db.repository.PostRepository;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    /**
     * Репозиторный слой для хранения информации о публикациях.
     */
    private final PostRepository postRepository;

    /**
     * Маппер для преобразования сущностей.
     */
    private final ModelMapper modelMapper;

    @Override
    public void save(PostDto post) {
        Assert.notNull(post, "Post must not be null!");
        postRepository.save(modelMapper.map(post, Post.class));
    }

    @Override
    public List<PostDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getById(Long id){
        return postRepository.readById(id)
                .map(post -> modelMapper.map(post, PostDto.class))
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public Optional<PostDto> getLastPost() {
        return postRepository.findFirstByOrderByEventTimeDesc()
                .map(post -> modelMapper.map(post, PostDto.class));
    }

}
