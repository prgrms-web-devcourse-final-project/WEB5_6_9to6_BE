package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.study.entity.Study;

import java.util.List;

public interface StudyRepositoryCustom {
    List<Study> searchByFilterWithSchedules(StudySearchRequest request);
}
