package com.grepp.spring.app.controller.api.study.payload;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.infra.util.page.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudySearchRequest extends PageParam {
    private Category category;
    private Region region;
    private Status status;
    private String name;
    private StudyType studyType;
}
