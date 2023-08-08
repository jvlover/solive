package com.ssafy.solive.api.matching.controller;

import com.ssafy.solive.api.matching.request.NotificationDeletePutReq;
import com.ssafy.solive.api.matching.request.NotificationModifyPutReq;
import com.ssafy.solive.api.matching.response.NotificationFindRes;
import com.ssafy.solive.api.matching.service.NotificationService;
import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.exception.matching.MatchingPossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 매칭 상황에서 유저가 서버로부터 알림을 받기 위한 API를 모은 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/notification")
@CrossOrigin("*")
public class NotificationController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService,
        NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * 클라이언트가 알림을 구독하기 위한 기능
     *
     * @param request : userID를 access-token에서 가져와야 함
     * @return sseEmitter : 프론트에서 알림을 받을 Emitter
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletRequest request,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        log.info("NotificationController_subscribe_start: " + request.toString());

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);

        SseEmitter sseEmitter = notificationService.subscribe(userId, lastEventId);

        log.info("NotificationController_subscribe_end: userId = " + userId.toString()
            + "\n lastEventId = "
            + lastEventId);

        return sseEmitter;
    }

    /**
     * 유저 별 알림 목록을 조회
     *
     * @param request : 헤더에 access-token
     * @return findResList : 알림 조회 결과 리스트
     */
    @GetMapping()
    public CommonResponse<?> find(HttpServletRequest request) {

        log.info("NotificationController_find_start: ");

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);

        List<NotificationFindRes> findResList = notificationService.findNotification(userId);

        if (findResList.size() == 0) {
            log.info("NotificationController_find_end: No Result");
        } else {
            log.info("NotificationController_find_end: " + findResList);
        }
        return CommonResponse.success(findResList);
    }

    /**
     * 유저 알림 읽음 처리
     *
     * @param modifyInfo : 수정할 알림 id
     */
    @PutMapping()
    public CommonResponse<?> modify(NotificationModifyPutReq modifyInfo) {

        log.info("NotificationController_modify_start: " + modifyInfo.toString());

        notificationService.modifyNotification(modifyInfo);

        log.info("NotificationController_modify_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 알림 삭제
     *
     * @param deleteInfo : 수정할 알림 id
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(NotificationDeletePutReq deleteInfo,
        HttpServletRequest request) {

        log.info("NotificationController_delete_start: " + deleteInfo.toString());

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);

        // http 헤더에서 유저 아이디 꺼내서 setting
        deleteInfo.setUserId(userId);

        boolean isDeleted = notificationService.deleteNotification(deleteInfo); // 삭제 실패하면 false
        if (isDeleted) {
            log.info("NotificationController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 유저가 자신의 소유가 아닌 알림을 삭제하려고 하는 경우
            throw new MatchingPossessionFailException();
        }
    }
}
