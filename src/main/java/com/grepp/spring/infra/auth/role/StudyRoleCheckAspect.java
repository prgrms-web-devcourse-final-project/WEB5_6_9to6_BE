package com.grepp.spring.infra.auth.role;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.infra.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

@Aspect
@Component
@RequiredArgsConstructor
public class StudyRoleCheckAspect {

    private final StudyMemberRepository studyMemberRepository;

    @Before("@annotation(com.grepp.spring.infra.auth.role.HasRoleInThisStudy)")
    public void checkRole(JoinPoint joinPoint) throws AccessDeniedException {
        Long memberId = SecurityUtil.getCurrentMemberId();

        // HttpServletRequest 에서 studyId 획득
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // PathVariable로 넘어온 값 획득
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
            HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long studyId = Long.parseLong(pathVariables.get("studyId"));

        // 허가된 역할
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasRoleInThisStudy checkStudyRole = signature.getMethod().getAnnotation(HasRoleInThisStudy.class);
        StudyRole[] requiredRoles = checkStudyRole.value();

        // 실제 역할
        StudyRole userRoleInStudy = studyMemberRepository.findRoleByStudyAndMember(studyId, memberId)
            .orElseThrow(() -> new AccessDeniedException("해당 스터디에 가입되지않았습니다."));

        boolean isAuthorized = Arrays.stream(requiredRoles)
            .anyMatch(role -> role.equals(userRoleInStudy));

        if (!isAuthorized) {
            throw new AccessDeniedException("해당 역할은 접근 권한이 없습니다.");
        }
    }

}