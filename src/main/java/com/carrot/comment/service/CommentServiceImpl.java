package com.carrot.comment.service;

import com.carrot.account.domain.Account;
import com.carrot.comment.domain.Comment;
import com.carrot.comment.repository.CommentRepository;
import com.carrot.comment.exception.CommentDeleteUnauthorized;
import com.carrot.account.exception.GuestForbiddenException;
import com.carrot.article.domain.Article;
import com.carrot.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static com.carrot.auth.UnknownAccount.guestAuth;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleService articleService;

    @Override
    public Comment create(Long articleId, Account account, Comment comment) {

        if (account == guestAuth()) {
            throw new GuestForbiddenException();
        }
        comment.setRelation(articleService.read(articleId), account);
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long articleId, Long commentId, Account account) {

        Article article = articleService.read(articleId);
        Comment comment = commentRepository.findByIdAndArticle(commentId, article).orElseThrow(EntityNotFoundException::new);

        if (comment.getAccount().getId().equals(account.getId())) {
            commentRepository.delete(comment);
        }
        throw new CommentDeleteUnauthorized();
    }

    @Override
    public Comment read(Long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Comment> readCommentsList(Long articleId, Pageable pageable) {

        return commentRepository.findAllByArticleId(articleId, pageable);
    }
}
