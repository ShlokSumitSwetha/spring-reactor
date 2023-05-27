package com.spring.reactive.springreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class TestScheduler {
    public static void main(String[] args) throws InterruptedException {


      // just check the thread its using to perform this action
       Mono.just(1).log()
                .delayElement(Duration.ofMillis(10))
                .map(x-> {
            System.out.println("Inside the thread: "+Thread.currentThread().getName());
            return x;
        }).subscribe(System.out::println);

        Thread.sleep(20000);


/**
 *  *  In this case we are using publishOn which depends on its position
 *  *  first map = main
 *  *  second map = parallel loop
 */
     Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                })
                .publishOn(Schedulers.parallel())
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                   return "value " + i;
                })
                .subscribe(System.out::println);

        /**
         *  *  In this case we are using subscribeOn which does not depends on its position
         *  *  first map = parallel
         *  *  second map = parallel loop
         */

    Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                })
                .subscribeOn(Schedulers.parallel())
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribe(System.out::println);

                Thread.sleep(2000);

        /**
         *  *  In this case we are using subscribeOn which does not depends on its position and we kept it in the end
         *  *  first map = parallel
         *  *  second map = parallel loop
         */

        Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                })
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe(System.out::println);
        Thread.sleep(2000);

        /**
         *  *  In this case we are using publishOn & subscribeOn
         *  *  first map = elastic
         *  *  second map = parallel loop
         */

       Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                }).publishOn(Schedulers.boundedElastic())
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe(System.out::println);
        Thread.sleep(2000);

        /**
         *  *  In this case we are using publishOn to check its positon
         *  *  first map = parallel
         *  *  second map = main
         */

        Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                }).publishOn(Schedulers.parallel())
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribe(System.out::println);
        Thread.sleep(2000);

        /**
         *  *  In this case we are using subscribeOn twice, then first subscribeOn takes preference
         *  *  first map = elastic
         *  *  second map = elastic
         */

        Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                }).subscribeOn(Schedulers.boundedElastic()) // first in subscribe on takes preference
                  .subscribeOn(Schedulers.parallel())
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribe(System.out::println);
        Thread.sleep(2000);

        /**
         *  *  In this case using subscribeOn twice, then first subscribeOn takes preference
         *  *  first map = parallel
         *  *  second map = main
         */
        Flux.range(1, 2).log()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                }).subscribeOn(Schedulers.parallel()) // first in subscribe on takes preference
                .subscribeOn(Schedulers.boundedElastic())
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribe(System.out::println);
        Thread.sleep(2000);

        /**
         *  *  In this case using subscribeOn with parallel flux
         *  *  first map = parallel
         *  *  second map = main
         */
        Flux.range(1, 10).log().parallel()
                .map(x-> {
                    System.out.println("Inside the first map: "+Thread.currentThread().getName());
                    return x+10;
                }).runOn(Schedulers.parallel()) // first in subscribe on takes preference
                .map(i -> {
                    System.out.println("Inside the second map: "+Thread.currentThread().getName());
                    return "value " + i;
                })
                .subscribe(System.out::println);
        Thread.sleep(2000);

        // In this example, the flatMap operator is used to process each element of
        // the parallel flux individually using a custom Scheduler specified by
        // subscribeOn(Schedulers.single()). This allows each element to be executed on a separate thread.
        Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(i -> Mono.fromCallable(() -> {
                    System.out.println("Processing: " + i + " on thread: " + Thread.currentThread().getName());
                    // Perform some processing on each element
                    return i;
                }).subscribeOn(Schedulers.single()))
                .sequential()
                .subscribe();



    }
}
