package ws.rest.springcloud.service.impl;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.model.Transaction;

public interface ITransactionService extends ReactiveMongoRepository<Transaction, String> {
	Flux<Transaction> findByTitularAndTransactdateBetween(String titular, LocalDate date1, LocalDate date2);
	Mono<Long> countByTitular(String titular);
}
