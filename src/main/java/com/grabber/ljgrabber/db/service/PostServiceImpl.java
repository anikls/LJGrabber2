package com.grabber.ljgrabber.db.service;

import com.grabber.ljgrabber.db.entity.Post;
import com.grabber.ljgrabber.db.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public void save(Post post) { postRepository.save(post); }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getById(Long id) {
        return postRepository.readById(id);
    }
}
