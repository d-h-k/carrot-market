package com.board.attachfile.entity;

import com.board.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "attach_file")
@Getter
@Entity
@NoArgsConstructor
public class AttachFile {

    @Id
    @GeneratedValue
    @Column(name = "attach_file_id")
    private Long id;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "verified_file_name")
    private String VerifiedFileName;

    @ManyToOne(fetch = FetchType.LAZY) //N-1 단방향 관계로 맵핑
    @JoinColumn(name = "post_id")
    private Post post;

    public String getImgUrl() {
        return "not yet";
        //스프링설정에서 읽어온 url에서 스테틱 제공
    }

}