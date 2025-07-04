package com.grepp.studium.quiz_record.service;

import com.grepp.studium.quiz_record.domain.QuizRecord;
import com.grepp.studium.quiz_record.model.QuizRecordDTO;
import com.grepp.studium.quiz_record.repos.QuizRecordRepository;
import com.grepp.studium.quiz_set.domain.QuizSet;
import com.grepp.studium.quiz_set.repos.QuizSetRepository;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuizRecordService {

    private final QuizRecordRepository quizRecordRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final QuizSetRepository quizSetRepository;

    public QuizRecordService(final QuizRecordRepository quizRecordRepository,
            final StudyMemberRepository studyMemberRepository,
            final QuizSetRepository quizSetRepository) {
        this.quizRecordRepository = quizRecordRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.quizSetRepository = quizSetRepository;
    }

    public List<QuizRecordDTO> findAll() {
        final List<QuizRecord> quizRecords = quizRecordRepository.findAll(Sort.by("quizRecordId"));
        return quizRecords.stream()
                .map(quizRecord -> mapToDTO(quizRecord, new QuizRecordDTO()))
                .toList();
    }

    public QuizRecordDTO get(final Integer quizRecordId) {
        return quizRecordRepository.findById(quizRecordId)
                .map(quizRecord -> mapToDTO(quizRecord, new QuizRecordDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final QuizRecordDTO quizRecordDTO) {
        final QuizRecord quizRecord = new QuizRecord();
        mapToEntity(quizRecordDTO, quizRecord);
        return quizRecordRepository.save(quizRecord).getQuizRecordId();
    }

    public void update(final Integer quizRecordId, final QuizRecordDTO quizRecordDTO) {
        final QuizRecord quizRecord = quizRecordRepository.findById(quizRecordId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(quizRecordDTO, quizRecord);
        quizRecordRepository.save(quizRecord);
    }

    public void delete(final Integer quizRecordId) {
        quizRecordRepository.deleteById(quizRecordId);
    }

    private QuizRecordDTO mapToDTO(final QuizRecord quizRecord, final QuizRecordDTO quizRecordDTO) {
        quizRecordDTO.setQuizRecordId(quizRecord.getQuizRecordId());
        quizRecordDTO.setIsPassed(quizRecord.getIsPassed());
        quizRecordDTO.setActivated(quizRecord.getActivated());
        quizRecordDTO.setStudyMember(quizRecord.getStudyMember() == null ? null : quizRecord.getStudyMember().getStudyMemberId());
        quizRecordDTO.setQuizSet(quizRecord.getQuizSet() == null ? null : quizRecord.getQuizSet().getQuizSetId());
        return quizRecordDTO;
    }

    private QuizRecord mapToEntity(final QuizRecordDTO quizRecordDTO, final QuizRecord quizRecord) {
        quizRecord.setIsPassed(quizRecordDTO.getIsPassed());
        quizRecord.setActivated(quizRecordDTO.getActivated());
        final StudyMember studyMember = quizRecordDTO.getStudyMember() == null ? null : studyMemberRepository.findById(quizRecordDTO.getStudyMember())
                .orElseThrow(() -> new NotFoundException("studyMember not found"));
        quizRecord.setStudyMember(studyMember);
        final QuizSet quizSet = quizRecordDTO.getQuizSet() == null ? null : quizSetRepository.findById(quizRecordDTO.getQuizSet())
                .orElseThrow(() -> new NotFoundException("quizSet not found"));
        quizRecord.setQuizSet(quizSet);
        return quizRecord;
    }

}
