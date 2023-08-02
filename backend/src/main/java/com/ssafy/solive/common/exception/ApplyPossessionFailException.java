package com.ssafy.solive.common.exception;

/*
 *  권한이 없는 신청에 취소를 요청했을 시 Exception
 */
public class ApplyPossessionFailException extends BaseException {

    public ApplyPossessionFailException() {
        super(ErrorCode.OUT_OF_POSSESSION);
    }
}