package com.demo.custom.subscription;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class CustomSubscriptionDemoApplication {

  public static void main(String[] args) throws InterruptedException {

    AtomicReference<Subscription> atomicReference = new AtomicReference<>();
    Flux.range(1, 20).subscribeWith(new Subscriber<Integer>() {
      @Override
      public void onSubscribe(Subscription subscription) {
        System.out.println("Custom Subscription onSubscribe : " + subscription);
        atomicReference.set(subscription);
      }

      @Override
      public void onNext(Integer integer) {
        System.out.println("Custom Subscription onNext : " + integer);
      }

      @Override
      public void onError(Throwable throwable) {
        System.out.println("Custom Subscription onError : " + throwable);
      }

      @Override
      public void onComplete() {
        System.out.println("Custom Subscription onComplete");
      }
    });

    Thread.sleep(3000);
    atomicReference.get().request(3);
    Thread.sleep(3000);
    atomicReference.get().request(1);
    Thread.sleep(3000);
    atomicReference.get().cancel();
    Thread.sleep(3000);
    atomicReference.get().request(5);

  }
}