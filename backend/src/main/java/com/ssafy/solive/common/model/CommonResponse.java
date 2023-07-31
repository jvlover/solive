package com.ssafy.solive.common.model;

import com.ssafy.solive.common.exception.ErrorCode;
import lombok.Getter;

/*
    임시로 구성한 공통 Response Body 입니다.
    API 요청에 대하여 성공적인 결과를 얻어냈을 경우 success 함수에 data를 넣어 리턴해주면 됩니다.

    현재 형식에 따른 예시) 게시판 목록 조회 API 요청의 경우
    <-------
    public CommonResponse<List<BoardView>> list(HttpServletRequest request) {
        List<BoardView> result = new ArrayList<>();
        if (jwtService.checkToken(request.getHeader("access-token"))) {
            result = BoardService.getBoardList();
        }
        return CommonResponse.success(result);
    }
    ------->

    코드는 앞으로 변경될 수 있습니다 (2023-07-20)
*/

@Getter
public final class CommonResponse<T> {

    // 성공 여부를 나타내는 boolean 변수
    private final boolean success;
    // 클라이언트가 요청한 데이터. 요청 실패했을 경우 null
    private final T data;
    // 실패했을 경우 어떤 에러인지 클라이언트에게 전달. 요청 성공했을 경우 null
    private final Error error;

    public CommonResponse(boolean success, T data, Error error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // API 요청 성공 시 컨트롤러에서 success 함수에 적절한 data를 넣어 리턴하면 됩니다.
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, data, null);
    }

    // API 요청 실패 시 fail 함수에 ErrorCode를 직접 넣을 수 있습니다.
    public static <T> CommonResponse<T> fail(ErrorCode errorCode) {
        return new CommonResponse<T>(
            false, null, new Error(errorCode.name(), errorCode.getMessage())
        );
    }

    // API 요청 실패 시 fail 함수에 ErrorCode 대신 코드와 메세지를 직접 넣어 리턴할 수 있습니다.
    public static <T> CommonResponse<T> fail(String code, String message) {
        return new CommonResponse<>(false, null, new Error(code, message));
    }

    // Response에 넣기 위한 Error 클래스입니다.
    static class Error {

        //에러 타입
        private String code;
        //에러 메시지
        private String message;

        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

}