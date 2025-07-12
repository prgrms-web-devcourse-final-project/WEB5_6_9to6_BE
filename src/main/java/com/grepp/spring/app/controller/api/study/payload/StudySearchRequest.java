package com.grepp.spring.app.controller.api.study.payload;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import lombok.Data;

@Data
public class StudySearchRequest {
    private Category category;
    private Region region;
    private Status status;
    private String name;
}
