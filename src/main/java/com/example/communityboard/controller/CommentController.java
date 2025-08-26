package com.example.communityboard.controller;

import com.example.communityboard.entity.Comment;
import com.example.communityboard.entity.Post;
import com.example.communityboard.service.CommentService;
import com.example.communityboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostRepository postRepository;

    @Autowired
    public CommentController(CommentService commentService, PostRepository postRepository) {
        this.commentService = commentService;
        this.postRepository = postRepository;
    }

    /** 댓글 작성 */
    @PostMapping("/write")
    public String writeComment(@PathVariable Long postId,
                               @RequestParam String nickname,
                               @RequestParam String content) {  // password 제거
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setNickname(nickname);
        comment.setContent(content);
        comment.setPost(post);

        commentService.saveComment(postId, comment);

        return "redirect:/posts/view/" + postId;
    }

    /** 댓글 수정 폼 */
    @GetMapping("/{commentId}/edit")
    public String showEditForm(@PathVariable Long postId,
                               @PathVariable Long commentId,
                               Model model) {
        Comment comment = commentService.findById(commentId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);
        return "commentEdit";
    }

    /** 댓글 수정 */
    @PostMapping("/{commentId}/edit")
    public String editComment(@PathVariable Long postId,
                              @PathVariable Long commentId,
                              @RequestParam String content) {  // password 제거
        commentService.updateComment(commentId, content);  // 인증 없이 바로 수정
        return "redirect:/posts/view/" + postId;
    }

    /** 댓글 삭제 */
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId) {  // password 제거
        commentService.deleteComment(commentId);  // 인증 없이 바로 삭제
        return "redirect:/posts/view/" + postId;
    }
}
