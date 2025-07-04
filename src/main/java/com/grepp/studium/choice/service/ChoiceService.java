package com.grepp.studium.choice.service;

import com.grepp.studium.choice.domain.Choice;
import com.grepp.studium.choice.model.ChoiceDTO;
import com.grepp.studium.choice.repos.ChoiceRepository;
import com.grepp.studium.quiz.domain.Quiz;
import com.grepp.studium.quiz.repos.QuizRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ChoiceService {

    private final ChoiceRepository choiceRepository;
    private final QuizRepository quizRepository;

    public ChoiceService(final ChoiceRepository choiceRepository,
            final QuizRepository quizRepository) {
        this.choiceRepository = choiceRepository;
        this.quizRepository = quizRepository;
    }

    public List<ChoiceDTO> findAll() {
        final List<Choice> choices = choiceRepository.findAll(Sort.by("choiceId"));
        return choices.stream()
                .map(choice -> mapToDTO(choice, new ChoiceDTO()))
                .toList();
    }

    public ChoiceDTO get(final Integer choiceId) {
        return choiceRepository.findById(choiceId)
                .map(choice -> mapToDTO(choice, new ChoiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ChoiceDTO choiceDTO) {
        final Choice choice = new Choice();
        mapToEntity(choiceDTO, choice);
        return choiceRepository.save(choice).getChoiceId();
    }

    public void update(final Integer choiceId, final ChoiceDTO choiceDTO) {
        final Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(choiceDTO, choice);
        choiceRepository.save(choice);
    }

    public void delete(final Integer choiceId) {
        choiceRepository.deleteById(choiceId);
    }

    private ChoiceDTO mapToDTO(final Choice choice, final ChoiceDTO choiceDTO) {
        choiceDTO.setChoiceId(choice.getChoiceId());
        choiceDTO.setChoice1(choice.getChoice1());
        choiceDTO.setChoice2(choice.getChoice2());
        choiceDTO.setChoice3(choice.getChoice3());
        choiceDTO.setChoice4(choice.getChoice4());
        choiceDTO.setActivated(choice.getActivated());
        choiceDTO.setQuiz(choice.getQuiz() == null ? null : choice.getQuiz().getQuizId());
        return choiceDTO;
    }

    private Choice mapToEntity(final ChoiceDTO choiceDTO, final Choice choice) {
        choice.setChoice1(choiceDTO.getChoice1());
        choice.setChoice2(choiceDTO.getChoice2());
        choice.setChoice3(choiceDTO.getChoice3());
        choice.setChoice4(choiceDTO.getChoice4());
        choice.setActivated(choiceDTO.getActivated());
        final Quiz quiz = choiceDTO.getQuiz() == null ? null : quizRepository.findById(choiceDTO.getQuiz())
                .orElseThrow(() -> new NotFoundException("quiz not found"));
        choice.setQuiz(quiz);
        return choice;
    }

}
