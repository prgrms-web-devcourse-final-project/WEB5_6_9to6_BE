package com.grepp.studium.quiz_set.service;

import com.grepp.studium.quiz.domain.Quiz;
import com.grepp.studium.quiz.repos.QuizRepository;
import com.grepp.studium.quiz_record.domain.QuizRecord;
import com.grepp.studium.quiz_record.repos.QuizRecordRepository;
import com.grepp.studium.quiz_set.domain.QuizSet;
import com.grepp.studium.quiz_set.model.QuizSetDTO;
import com.grepp.studium.quiz_set.repos.QuizSetRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuizSetService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final QuizRepository quizRepository;

    public QuizSetService(final QuizSetRepository quizSetRepository,
            final QuizRecordRepository quizRecordRepository, final QuizRepository quizRepository) {
        this.quizSetRepository = quizSetRepository;
        this.quizRecordRepository = quizRecordRepository;
        this.quizRepository = quizRepository;
    }

    public List<QuizSetDTO> findAll() {
        final List<QuizSet> quizSets = quizSetRepository.findAll(Sort.by("quizSetId"));
        return quizSets.stream()
                .map(quizSet -> mapToDTO(quizSet, new QuizSetDTO()))
                .toList();
    }

    public QuizSetDTO get(final Integer quizSetId) {
        return quizSetRepository.findById(quizSetId)
                .map(quizSet -> mapToDTO(quizSet, new QuizSetDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final QuizSetDTO quizSetDTO) {
        final QuizSet quizSet = new QuizSet();
        mapToEntity(quizSetDTO, quizSet);
        return quizSetRepository.save(quizSet).getQuizSetId();
    }

    public void update(final Integer quizSetId, final QuizSetDTO quizSetDTO) {
        final QuizSet quizSet = quizSetRepository.findById(quizSetId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(quizSetDTO, quizSet);
        quizSetRepository.save(quizSet);
    }

    public void delete(final Integer quizSetId) {
        quizSetRepository.deleteById(quizSetId);
    }

    private QuizSetDTO mapToDTO(final QuizSet quizSet, final QuizSetDTO quizSetDTO) {
        quizSetDTO.setQuizSetId(quizSet.getQuizSetId());
        quizSetDTO.setWeek(quizSet.getWeek());
        quizSetDTO.setActivated(quizSet.getActivated());
        return quizSetDTO;
    }

    private QuizSet mapToEntity(final QuizSetDTO quizSetDTO, final QuizSet quizSet) {
        quizSet.setWeek(quizSetDTO.getWeek());
        quizSet.setActivated(quizSetDTO.getActivated());
        return quizSet;
    }

    public ReferencedWarning getReferencedWarning(final Integer quizSetId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final QuizSet quizSet = quizSetRepository.findById(quizSetId)
                .orElseThrow(NotFoundException::new);
        final QuizRecord quizSetQuizRecord = quizRecordRepository.findFirstByQuizSet(quizSet);
        if (quizSetQuizRecord != null) {
            referencedWarning.setKey("quizSet.quizRecord.quizSet.referenced");
            referencedWarning.addParam(quizSetQuizRecord.getQuizRecordId());
            return referencedWarning;
        }
        final Quiz quizSetQuiz = quizRepository.findFirstByQuizSet(quizSet);
        if (quizSetQuiz != null) {
            referencedWarning.setKey("quizSet.quiz.quizSet.referenced");
            referencedWarning.addParam(quizSetQuiz.getQuizId());
            return referencedWarning;
        }
        return null;
    }

}
