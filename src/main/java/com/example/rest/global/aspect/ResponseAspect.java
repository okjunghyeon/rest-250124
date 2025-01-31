package com.example.rest.global.aspect;

import com.example.rest.global.dto.RsData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ResponseAspect {

    private final HttpServletResponse response;

    @Around("""
            (
                within
                (
                    @org.springframework.web.bind.annotation.RestController *
                )
                &&
                (
                    @annotation(org.springframework.web.bind.annotation.GetMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.PostMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.PutMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.DeleteMapping)
                )
            )
            ||
            @annotation(org.springframework.web.bind.annotation.ResponseBody)
            """)
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("pre"); // 전처리

        Object rst = joinPoint.proceed(); // 실제 수행 메서드

        if(rst instanceof RsData rsData) {
            String msg = rsData.getMsg();

            //응답 코드를 설정
            // 응답 헤더의 값을 수정하거나 꺼내올 수 있는 즉, 모아져있는 객체
            response.setStatus(201);

        }

        System.out.println("post"); // 후처리

        return rst;
    }

}