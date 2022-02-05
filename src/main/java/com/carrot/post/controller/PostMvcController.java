package com.carrot.post.controller;

import com.carrot.post.dto.PostDtoRequest;
import com.carrot.post.dto.PostDtoResponse;
import com.carrot.post.entity.Post;
import com.carrot.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.carrot.post.util.SearchType.SALE;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostMvcController {

    private final PostService postService;
    private final ModelMapper modelMapper;

    @GetMapping
    public String form(Model model) {
        model.addAttribute("postDtoRequest", new PostDtoRequest());
        return "postForm";
    }

    @GetMapping("/{postId}")
    public String read(@PathVariable Long postId, Model model) {
        Post post = postService.get(postId,SALE);
        PostDtoResponse response = modelMapper.map(post, PostDtoResponse.class);
        model.addAttribute(response);
        return "postDetail";
    }


    /*
    @PostMapping("/new")
    public PostDtoRequest postSave(@Valid PostDtoRequest postDtoRequest,
                                   @RequestParam(value = "attachFiles",
                                           required = false) List<MultipartFile> attachFiles) {
        //postService.saveWithAttach(1L, attachFiles);
        return postDtoRequest;
    }
     */
}