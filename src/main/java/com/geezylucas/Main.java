package com.geezylucas;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/*
    Project reactor libraries provide two implementations of the Publisher interface:
    
    Mono: Returns 0 or 1 element.
    The Mono API allows producing only one value.

    Flux: Returns 0…N elements.
    The Flux can be endless, it can produce multiple values.
 */
public class Main {

    public static void main(String[] args) {

        Mono<String> stringMono = Mono.just("java guides")
                .log();

        stringMono.subscribe((element) -> {
            System.out.println(element);
        });

        /*
         * Mono<String> stringMono = Mono.just("java guides")
         * .then(Mono.error(new
         * RuntimeException("Exception occured while emitting the data")))
         * .log();
         * stringMono.subscribe((element) -> {
         * System.out.println(element);
         * }, throwable -> System.out.println(throwable.getMessage()));
         */

        Flux<String> stringFlux = Flux.just("Apple", "Banana", "Orange", "Mango")
                .flatMap(s -> Flux.just(s.split("")))
                .log();
        stringFlux.subscribe((element) -> {
            System.out.println(element);
        });

        getItems();

        // Wait for a moment to allow the
        // async processing to complete
        try {
            System.out.println("waiting...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getItems() {
        // Create a Flux of integers from 1 to 100
        Flux<Integer> flux = Flux.range(1, 10000)
                .log();

        // Use reactive operators to
        // transform and process the data

        // Filter out odd numbers
        flux.filter(i -> i % 2 == 0)
                // Double the remaining numbers
                .map(i -> i * 2)
                // Publish on parallel scheduler
                // for concurrent execution
                .publishOn(Schedulers.parallel())
                // Subscribe to the final data
                // stream and print the results
                .subscribe(System.out::println);
    }
}

/*
 * Let's understand the above Reactive stream workflow:
 * 1. The Subscriber will call subscribe() method of the Publisher to subscribe
 * or register with the Publisher.
 * 
 * 2. The Publisher creates an instance of Subscription and sends it to
 * Subscriber saying that your subscription is successful.
 * 
 * 3. Next, the Subscriber will call the request(n) method of Subscription to
 * request data from the Publisher.
 * 
 * 4. Next, Publisher call onNext(data) method to send data to the Subscriber.
 * Publisher call onNext(data) n times. It means if there are 10 items then the
 * Publisher call onNext(data) method 10 times.
 * 
 * 5. Once the Publisher sends all the data to Subscriber, the next Publisher
 * call onComplete() method to notify Subscriber that all the data has been
 * sent. If there are any errors while sending the data then the Publisher call
 * onError() method to send error details to the Subscriber.
 */