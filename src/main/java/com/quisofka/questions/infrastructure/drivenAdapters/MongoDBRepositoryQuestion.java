package com.quisofka.questions.infrastructure.drivenAdapters;

import com.quisofka.questions.domain.model.Question;
import com.quisofka.questions.infrastructure.drivenAdapters.data.QuestionData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface MongoDBRepositoryQuestion extends ReactiveMongoRepository<QuestionData, String> {

    //The next two lines are other 2 possible configurations for the queries that are simpler
    //@Query("{'level': ?0, 'knowledgeArea': ?1 }")
    //@Query(value="{ $and: [ { level: ?0 }, { knowledgeArea: ?1 } ] }", fields="{ 'id' : 1, 'description' : 1, 'answers' : 1, 'knowledgeArea' : 1, 'descriptor' : 1, 'type' : 1, 'level' : 1 }")
    //Flux<Question> findByLevelAndKnowledgeArea(String level, String knowledgeArea, Pageable pageable);

    @Aggregation(pipeline = {
            "{$match: { level: { $regex: ?0, $options: 'i' }, knowledgeArea: { $regex: ?1, $options: 'i' } }}",
            "{$sample: {size: ?2}}",
            "{$project: { 'id' : 1, 'description' : 1, 'answers' : 1, 'knowledgeArea' : 1, 'descriptor' : 1, 'type' : 1, 'level' : 1 }}"
    })
    Flux<Question> findByLevelAndKnowledgeArea(String level, String knowledgeArea, int size);


    @Aggregation(pipeline = {
            "{$match: { level: { $regex: ?0, $options: 'i' } }}",
            "{$sample: {size: ?1}}",
            "{$project: { 'id' : 1, 'description' : 1, 'answers' : 1, 'knowledgeArea' : 1, 'descriptor' : 1, 'type' : 1, 'level' : 1 }}"
    })
    Flux<Question> findByLevel(String level, int size);
}
