package com.grepp.studium.quiz.service;

import com.grepp.studium.choice.domain.Choice;
import com.grepp.studium.choice.repos.ChoiceRepository;
import com.grepp.studium.quiz.domain.Quiz;
import com.grepp.studium.quiz.model.QuizDTO;
import com.grepp.studium.quiz.repos.QuizRepository;
import com.grepp.studium.quiz_set.domain.QuizSet;
import com.grepp.studium.quiz_set.repos.QuizSetRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSetRepository quizSetRepository;
    private final ChoiceRepository choiceRepository;

    public QuizService(final QuizRepository quizRepository,
            final QuizSetRepository quizSetRepository, final ChoiceRepository choiceRepository) {
        this.quizRepository = quizRepository;
        this.quizSetRepository = quizSetRepository;
        this.choiceRepository = choiceRepository;
    }

    public List<QuizDTO> findAll() {
        final List<Quiz> quizzes = quizRepository.findAll(Sort.by("quizId"));
        return quizzes.stream()
                .map(quiz -> mapToDTO(quiz, new QuizDTO()))
                .toList();
    }

    public QuizDTO get(final Integer quizId) {
        return quizRepository.findById(quizId)
                .map(quiz -> mapToDTO(quiz, new QuizDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final QuizDTO quizDTO) {
        final Quiz quiz = new Quiz();
        mapToEntity(quizDTO, quiz);
        return quizRepository.save(quiz).getQuizId();
    }

    public void update(final Integer quizId, final QuizDTO quizDTO) {
        final Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(quizDTO, quiz);
        quizRepository.save(quiz);
    }

    public void delete(final Integer quizId) {
        quizRepository.deleteById(quizId);
    }

    private QuizDTO mapToDTO(final Quiz quiz, final QuizDTO quizDTO) {
        quizDTO.setQuizId(quiz.getQuizId());
        quizDTO.setQuestion(quiz.getQuestion());
        quizDTO.setAnswer(quiz.getAnswer());
        quizDTO.setActivated(quiz.getActivated());
        quizDTO.setQuizSet(quiz.getQuizSet() == null ? null : quiz.getQuizSet().getQuizSetId());
        return quizDTO;
    }

    private Quiz mapToEntity(final QuizDTO quizDTO, final Quiz quiz) {
        quiz.setQuestion(quizDTO.getQuestion());
        quiz.setAnswer(quizDTO.getAnswer());
        quiz.setActivated(quizDTO.getActivated());
        final QuizSet quizSet = quizDTO.getQuizSet() == null ? null : quizSetRepository.findById(quizDTO.getQuizSet())
                .orElseThrow(() -> new NotFoundException("quizSet not found"));
        quiz.setQuizSet(quizSet);
        return quiz;
    }

    public ReferencedWarning getReferencedWarning(final Integer quizId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(NotFoundException::new);
        final Choice quizChoice = choiceRepository.findFirstByQuiz(quiz);
        if (quizChoice != null) {
            referencedWarning.setKey("quiz.choice.quiz.referenced");
            referencedWarning.addParam(quizChoice.getChoiceId());
            return referencedWarning;
        }
        return null;
    }

}
