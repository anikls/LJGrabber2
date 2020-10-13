package com.grabber.ljgrabber.service.impl;

import com.grabber.ljgrabber.entity.db.Post;
import com.grabber.ljgrabber.exception.PostExistsException;
import com.grabber.ljgrabber.repository.PostRepository;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.exception.PostNotFoundException;
import com.grabber.ljgrabber.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
        if (postRepository.getPostByAuthorAndItemId(post.getAuthor(), post.getItemId()).isPresent()) {
            throw new PostExistsException(post.getId());
        } else {
            postRepository.save(modelMapper.map(post, Post.class));
        }
    }

    @Override
    public List<PostDto> findAll(String author) {
        Assert.notNull(author, "Author must not be null!");
        return postRepository.findAllByAuthor(author)
                .stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostDto> findAllByYear(String author, int year) {
        Assert.notNull(author, "Author must not be null!");
        return postRepository.findPostsByAuthorAndEventTimeBetween(
                    author,
                    LocalDateTime.of(year,1,1,0,0),
                    LocalDateTime.of(year,12,31,23,59))
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getById(Long id) {
        return postRepository.findById(id)
                .map(post -> modelMapper.map(post, PostDto.class))
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public PostDto getByItemId(String author, Long id) {
        return postRepository.getPostByAuthorAndItemId(author, id)
                .map(post -> modelMapper.map(post, PostDto.class))
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public Optional<PostDto> getFirstPost(String author) {
        return postRepository.findFirstByAuthorOrderByEventTimeAsc(author)
                .map(post -> modelMapper.map(post, PostDto.class));
    }

    @Override
    public Optional<PostDto> getLastPost(String author) {
        return postRepository.findFirstByAuthorOrderByEventTimeDesc(author)
                .map(post -> modelMapper.map(post, PostDto.class));
    }

}
