package com.grepp.spring.infra.auth.role;

import com.grepp.spring.app.model.member.code.StudyRole;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyThisStudyRole {
    StudyRole value();
}
