package org.example.expert.config.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //AOP 핵심로직
@Component
@RequiredArgsConstructor
public class AdminAccessLoggingAspect {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;


    @Around("@annotation(org.example.expert.config.aop.annotation.Admin)")//어떤 메서드에서 aop가 실행될지 지정해줌. @Admin 어노테이션이 붙은 메서드만 적용.
    public Object logAdminApiAcess(ProceedingJoinPoint joinPoint)throws Throwable{
        Long userId = (Long) request.getAttribute("userId"); //필터에서 Attribute 에 담긴 userId
        String url = request.getRequestURI();  //요청헤더에 담긴 URI
        long requestTimestamp = System.currentTimeMillis(); //현재시간

        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        //jointPoint.getArgs() = 현재실행되는 메서드의 파라미터 값들을 배열로 가져옴
        //objectMapper.writeValueAsString =자바 객체를 JSON 문자열로 바꿈
        log.info(" userID={}.Timestamp={},URL={},RequestBody={}",
                userId,requestTimestamp,url,requestBody);  //로그찍음

        Object result = joinPoint.proceed();//@Admin 붙은 메서드 실행

        String responseBody = objectMapper.writeValueAsString(result); //실행된 메서드 응답값
        log.info(" userID={}.Timestamp={},URL={},RequestBody={}",
                userId,System.currentTimeMillis(),url,responseBody);

        return result;



    }


}
