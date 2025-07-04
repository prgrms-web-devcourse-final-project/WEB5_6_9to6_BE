package com.grepp.studium.study_notice.service;

import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study.repos.StudyRepository;
import com.grepp.studium.study_notice.domain.StudyNotice;
import com.grepp.studium.study_notice.model.StudyNoticeDTO;
import com.grepp.studium.study_notice.repos.StudyNoticeRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudyNoticeService {

    private final StudyNoticeRepository studyNoticeRepository;
    private final StudyRepository studyRepository;

    public StudyNoticeService(final StudyNoticeRepository studyNoticeRepository,
            final StudyRepository studyRepository) {
        this.studyNoticeRepository = studyNoticeRepository;
        this.studyRepository = studyRepository;
    }

    public List<StudyNoticeDTO> findAll() {
        final List<StudyNotice> studyNotices = studyNoticeRepository.findAll(Sort.by("noticeId"));
        return studyNotices.stream()
                .map(studyNotice -> mapToDTO(studyNotice, new StudyNoticeDTO()))
                .toList();
    }

    public StudyNoticeDTO get(final Integer noticeId) {
        return studyNoticeRepository.findById(noticeId)
                .map(studyNotice -> mapToDTO(studyNotice, new StudyNoticeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudyNoticeDTO studyNoticeDTO) {
        final StudyNotice studyNotice = new StudyNotice();
        mapToEntity(studyNoticeDTO, studyNotice);
        return studyNoticeRepository.save(studyNotice).getNoticeId();
    }

    public void update(final Integer noticeId, final StudyNoticeDTO studyNoticeDTO) {
        final StudyNotice studyNotice = studyNoticeRepository.findById(noticeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studyNoticeDTO, studyNotice);
        studyNoticeRepository.save(studyNotice);
    }

    public void delete(final Integer noticeId) {
        studyNoticeRepository.deleteById(noticeId);
    }

    private StudyNoticeDTO mapToDTO(final StudyNotice studyNotice,
            final StudyNoticeDTO studyNoticeDTO) {
        studyNoticeDTO.setNoticeId(studyNotice.getNoticeId());
        studyNoticeDTO.setContent(studyNotice.getContent());
        studyNoticeDTO.setCreatedAt(studyNotice.getCreatedAt());
        studyNoticeDTO.setModifiedAt(studyNotice.getModifiedAt());
        studyNoticeDTO.setActivated(studyNotice.getActivated());
        studyNoticeDTO.setStudy(studyNotice.getStudy() == null ? null : studyNotice.getStudy().getStudyId());
        return studyNoticeDTO;
    }

    private StudyNotice mapToEntity(final StudyNoticeDTO studyNoticeDTO,
            final StudyNotice studyNotice) {
        studyNotice.setContent(studyNoticeDTO.getContent());
        studyNotice.setCreatedAt(studyNoticeDTO.getCreatedAt());
        studyNotice.setModifiedAt(studyNoticeDTO.getModifiedAt());
        studyNotice.setActivated(studyNoticeDTO.getActivated());
        final Study study = studyNoticeDTO.getStudy() == null ? null : studyRepository.findById(studyNoticeDTO.getStudy())
                .orElseThrow(() -> new NotFoundException("study not found"));
        studyNotice.setStudy(study);
        return studyNotice;
    }

    public boolean studyExists(final Integer studyId) {
        return studyNoticeRepository.existsByStudyStudyId(studyId);
    }

}
