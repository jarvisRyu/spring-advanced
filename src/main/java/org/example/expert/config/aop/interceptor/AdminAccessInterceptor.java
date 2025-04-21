package org.example.expert.config.aop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;


//권한을 check하는 인터페이스
//1.인터셉터 등록 WebConfig
@Slf4j
public class AdminAccessInterceptor implements HandlerInterceptor {

    /*preHandle = 컨트롤러 요청 처리 전에 실행
    * postHandle = 컨트롤러 이후 뷰 렌더링 전실행
    * afterCompletion = 뷰렌더링 끝난 후 실행*/
    @Override //상속받을 메서드 선택
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));
        //요청에 userRole 을 Enum 타입으로 변경 ,of=맞는 타입 없을 시 InvalidRequestException 발생
        if(!UserRole.ADMIN.equals(userRole)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"관리자 권한이 필요합니다.");
            return false;


        }
        log.info("Interceptor - Admin API Access: Timestamp={},URL={}",System.currentTimeMillis(),request.getRequestURI());
        return true;
    }
}

