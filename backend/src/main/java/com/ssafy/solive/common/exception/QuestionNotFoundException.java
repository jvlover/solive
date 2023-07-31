package com.ssafy.solive.common.exception;

/*
 *  문제 단건 상세 조회 시 존재하지 않는 문제 요청하여 Exception
 */
public class QuestionNotFoundException extends BaseException {

    public QuestionNotFoundException() {
        super(ErrorCode.QUESTION_NOT_FOUND);
    }
}
