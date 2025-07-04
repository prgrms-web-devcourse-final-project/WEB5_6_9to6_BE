package com.grepp.studium.study_notice.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.grepp.studium.study_notice.service.StudyNoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the studyId value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = StudyNoticeStudyUnique.StudyNoticeStudyUniqueValidator.class
)
public @interface StudyNoticeStudyUnique {

    String message() default "{Exists.studyNotice.study}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StudyNoticeStudyUniqueValidator implements ConstraintValidator<StudyNoticeStudyUnique, Integer> {

        private final StudyNoticeService studyNoticeService;
        private final HttpServletRequest request;

        public StudyNoticeStudyUniqueValidator(final StudyNoticeService studyNoticeService,
                final HttpServletRequest request) {
            this.studyNoticeService = studyNoticeService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("noticeId");
            if (currentId != null && value.equals(studyNoticeService.get(Integer.parseInt(currentId)).getStudy())) {
                // value hasn't changed
                return true;
            }
            return !studyNoticeService.studyExists(value);
        }

    }

}
