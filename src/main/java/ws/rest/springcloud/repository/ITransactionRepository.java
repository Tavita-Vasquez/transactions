package ws.rest.springcloud.repository;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.model.Transaction;

public interface ITransactionRepository extends ReactiveMongoRepository<Transaction, String> {

	Flux<Transaction> findByTitularAndTransactdateBetween(String idHeadLine, LocalDate dateIni, LocalDate dateEnd);
	Mono<Long> countTransacByheadline();
	Mono<Long> countTransacByTitular(String idHeadLine);
	
}