package com.quisofka.questions.infrastructure.drivenAdapters;


import com.quisofka.questions.domain.model.Question;
import com.quisofka.questions.domain.model.gateways.QuestionRepositoryGateway;
import com.quisofka.questions.infrastructure.drivenAdapters.data.QuestionData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static java.util.Collections.shuffle;

@Repository
@RequiredArgsConstructor
public class MongoRepositoryAdapterQuestion implements QuestionRepositoryGateway {

    private final MongoDBRepositoryQuestion questionRepository;
    private final ObjectMapper mapper;

    @Override
    public Flux<Question> getAllQuestions() {
        return this.questionRepository
                .findAll()
                .switchIfEmpty(Mono.error(new Throwable("No questions available")))
                .map(item -> mapper.map(item, Question.class));
    }

    @Override
    public Flux<Question> getFirstLvlQuestions() {

        return this.questionRepository.findByLevelAndKnowledgeArea("INITIAL", "Java", 7)
                .switchIfEmpty(Mono.error(new Throwable("No questions available")))
                .concatWith(
                        this.questionRepository.findByLevelAndKnowledgeArea("INITIAL", "Javascript", 8)
                                .switchIfEmpty(Mono.error(new Throwable("No questions available")))
                )
                .map(question -> mapper.map(question, Question.class));
    }

    @Override
    public Flux<Question> getSecondLvlQuestions() {
        return this.questionRepository.findByLevelAndKnowledgeArea("BASIC", "Java", 7)
                .switchIfEmpty(Mono.error(new Throwable("No questions available")))
                .concatWith(
                        this.questionRepository.findByLevelAndKnowledgeArea("BASIC", "Javascript", 8)
                )
                .map(question -> mapper.map(question, Question.class));
    }

    @Override
    public Flux<Question> getThirdLvlQuestions() {

        return this.questionRepository.findByLevel("INTERMEDIATE", 15)
                .switchIfEmpty(Mono.error(new Throwable("No questions available")))
                .map(question -> mapper.map(question, Question.class));
    }

    @Override
    public Mono<Question> getQuestionById(String id) {
        return this.questionRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable("Question not found")))
                .map(item -> mapper.map(item, Question.class));
    }


    @Override
    public Mono<Question> createQuestion(Question question) {
        return Mono.just(question)
                .flatMap(question1 -> {
                    Question question2=Question.builder()
                            .description(question1.getDescription())
                            .answers(question1.getAnswers())
                            .descriptor(question1.getDescriptor().toUpperCase())
                            .knowledgeArea(question1.getKnowledgeArea().toUpperCase())
                            .type(question1.getType().toUpperCase())
                            .level(question1.getLevel().toUpperCase())
                            .build();
                    return this.questionRepository.save(mapper.map(question2, QuestionData.class));
                }).map(question3 -> mapper.map(question3, Question.class))
                .onErrorResume(Mono::error);
    }


    @Override
    public Mono<Void> deleteAll() {
        return this.questionRepository.deleteAll()
                .onErrorResume(Mono::error);
    }
}
