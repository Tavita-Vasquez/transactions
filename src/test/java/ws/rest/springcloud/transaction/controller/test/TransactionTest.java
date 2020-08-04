package ws.rest.springcloud.transaction.controller.test;

import org.junit.jupiter.api.Test;

public class TransactionTest {

	
	@Test
	public void givenValue_whenFindAllByValue_thenFindAccount() {
		/*
		 * repository.save(new Account(null, "Bill", 12.3)).block(); Flux<Account>
		 * accountFlux = repository.findAllByValue(12.3);
		 * 
		 * StepVerifier .create(accountFlux) .assertNext(account -> {
		 * assertEquals("Bill", account.getOwner()); assertEquals(Double.valueOf(12.3) ,
		 * account.getValue()); assertNotNull(account.getId()); }) .expectComplete()
		 * .verify();
		 */
	}
	 
	@Test
	public void givenOwner_whenFindFirstByOwner_thenFindAccount() {
		/*
		 * repository.save(new Account(null, "Bill", 12.3)).block(); Mono<Account>
		 * accountMono = repository .findFirstByOwner(Mono.just("Bill"));
		 * 
		 * StepVerifier .create(accountMono) .assertNext(account -> {
		 * assertEquals("Bill", account.getOwner()); assertEquals(Double.valueOf(12.3) ,
		 * account.getValue()); assertNotNull(account.getId()); }) .expectComplete()
		 * .verify();
		 */
	}
	 
	@Test
	public void givenAccount_whenSave_thenSaveAccount() {
		/*
		 * Mono<Account> accountMono = repository.save(new Account(null, "Bill", 12.3));
		 * 
		 * StepVerifier .create(accountMono) .assertNext(account ->
		 * assertNotNull(account.getId())) .expectComplete() .verify();
		 */
	}
	
	
}
