package com.app;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.github.javafaker.Faker;

@SpringBootApplication
@RestController
@EnableScheduling
public class Application {
	 private final Queue<DeferredResult<ResponseEntity < String >>> responseBodyQueue = new ConcurrentLinkedQueue<>();

	 public static void main(String[] args) {
	        SpringApplication.run(Application.class, args);
	    }
	   @GetMapping("/show")
	    public DeferredResult < ResponseEntity < String >> show() {
		    DeferredResult < ResponseEntity < String >> deferredResult = new DeferredResult < > ();
		   responseBodyQueue.add(deferredResult);
		   /*
		 * ForkJoinPool forkJoinPool = new ForkJoinPool(); forkJoinPool.submit(() -> {
		 * try { Thread.sleep(10000);
		 * 
		 * } catch (InterruptedException e) { }
		 * deferredResult.setResult(ResponseEntity.ok("OK")); });
		 */ 	return deferredResult;
	    }
	   
	   
	   @Scheduled(fixedRate = 10000)
	   public void processQueues() {
	    System.out.println("patar timotius ganteng");
		   for (DeferredResult<ResponseEntity<String>> result : responseBodyQueue) {
	          
				Faker faker = new Faker();
		    	String name = faker.name().fullName();
			   result.setResult(ResponseEntity.ok(name));
	           responseBodyQueue.remove(result);
	       }
	   }
}
