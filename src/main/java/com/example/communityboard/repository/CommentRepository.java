package com.example.communityboard.repository;

import com.example.communityboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글 ID(post.id)로 댓글 조회
    List<Comment> findByPost_Id(Long postId);
}
