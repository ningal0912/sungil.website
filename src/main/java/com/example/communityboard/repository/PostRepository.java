package com.example.communityboard.repository;

import com.example.communityboard.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(String category);

    @Query("select p from Post p left join fetch p.comments where p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") Long id);
}
