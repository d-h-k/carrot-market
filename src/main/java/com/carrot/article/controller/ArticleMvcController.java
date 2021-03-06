package com.carrot.article.controller;

import com.carrot.article.dto.ArticleDtoRequest;
import com.carrot.article.dto.ArticleDtoResponse;
import com.carrot.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.carrot.article.util.SearchType.SALE;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class ArticleMvcController {

    private final ArticleService articleService;

    @GetMapping
    public String form(Model model) {
        model.addAttribute("postDtoRequest", new ArticleDtoRequest());
        return "postForm";
    }

    @GetMapping("/{postId}")
    public String read(@PathVariable Long postId, Model model) {
        ArticleDtoResponse response = ArticleDtoResponse.toResponse(articleService.query(postId, SALE));
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
