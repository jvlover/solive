package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRegistPostReq {

    private Long userId;
    private String title;
    private String content;
}