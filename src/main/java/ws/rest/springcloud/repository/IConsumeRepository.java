package ws.rest.springcloud.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import ws.rest.springcloud.model.Consume;

public interface IConsumeRepository extends ReactiveMongoRepository<Consume, String> {
	Flux<Consume> findByProductidAndPayedOrderByMonthAsc(String productid, Boolean payed);
	Flux<Consume> findByTitularAndPayed(String titular, Boolean payed);

}
