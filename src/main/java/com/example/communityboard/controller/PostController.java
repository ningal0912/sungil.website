package com.example.communityboard.controller;

import com.example.communityboard.entity.Post;
import com.example.communityboard.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /** 카테고리별 게시글 목록 */
    @GetMapping("/posts/category/{category}")
    public String getPostsByCategory(@PathVariable String category, Model model) {
        List<Post> posts = postService.getPostsByCategory(category);
        model.addAttribute("posts", posts);
        model.addAttribute("category", category);

        String label, css;
        switch (category) {
            case "personal":
                label = "개인이야기";      css = "category-personal"; break;
            case "travel":
                label = "여행후기";        css = "category-travel";  break;
            case "idea":
                label = "공익 아이디어 제안하기"; css = "category-idea";    break;
            default:
                label = category;       css = "category-default"; break;
        }
        model.addAttribute("categoryLabel", label);
        model.addAttribute("categoryClass", css);

        return "postList";
    }

    /** 글쓰기 폼 */
    @GetMapping("/posts/new")
    public String showCreateForm(@RequestParam(required = false) String category, Model model) {
        Post post = new Post();
        if (category != null) post.setCategory(category);
        model.addAttribute("post", post);
        return "postForm";
    }

    /** 글 저장 */
    @PostMapping("/posts")
    public String createPost(@ModelAttribute Post post) {
        Post saved = postService.savePost(post);
        return "redirect:/posts/view/" + saved.getId();
    }

    /** 게시글 상세 보기 (댓글 포함) */
    @GetMapping("/posts/view/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostByIdWithComments(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        model.addAttribute("post", post);
        model.addAttribute("comments", post.getComments());
        return "postDetail";
    }

    /** 게시글 수정 폼 */
    @GetMapping("/posts/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        model.addAttribute("post", post);
        return "postEdit";
    }

    /** 게시글 수정 처리 */
    @PostMapping("/posts/update/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post updatedPost) {
        postService.updatePost(id, updatedPost);
        return "redirect:/posts/view/" + id;
    }

    /** 게시글 삭제 처리 */
    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts/category/personal"; // 기본 카테고리로 리다이렉트
    }
}
