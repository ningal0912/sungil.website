package com.example.communityboard.service;

import com.example.communityboard.entity.Post;
import com.example.communityboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /** 카테고리별 게시글 조회 */
    public List<Post> getPostsByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    /** 댓글을 함께 로드하는 게시글 조회 */
    public Optional<Post> getPostByIdWithComments(Long id) {
        return postRepository.findByIdWithComments(id);
    }

    /** ID로 게시글 조회 (댓글 미포함) */
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /** 게시글 저장 */
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    /** 게시글 수정 */
    public void updatePost(Long id, Post updatedPost) {
        postRepository.findById(id).ifPresent(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setCategory(updatedPost.getCategory());
            postRepository.save(post);
        });
    }

    /** 게시글 삭제 */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
