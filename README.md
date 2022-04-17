# Reactive Programming

## Advantages
1. Asynchronous and Non Blocking
2. Functional Style Code
3. Data FLow as even driven stream
4. Backpressure on data streams

## Core Interface
1. **Publisher** - Provider of sequenced elements according to the demand received from its Subscriber(s). 
It has a subscribe method which takes Subscriber as parameter.

2. **Subscriber** - The consumer of sequenced elements published by Publisher. It has a onSubscribe method
which takes Subscription as parameter and responsible to talk with Publisher. Other methods are onNext,
onError, onComplete.

3. **Subscription** - Both Publisher and Subscriber communicates via this interface. 

4. **Mono** - It is a publisher which allows to emit only zero or single value.

5. **Flux** - It is a publisher which allows to emit only zero or more values.

6. **Sinks** - It acts as both Publisher and Subscriber and has single inbound channel.

6. **Processor** - It acts as both Publisher and Subscriber and has both inbound and outbound channel.

## LifeCycle
1. Subscriber wants to connect to Publisher using subscribe().
2. Publisher calls onSubscribe() to hand over Subscription object to Subscriber.
3. Subscriber request() calls Publisher to request for data.
4. Publisher pushed data via onNext() for every emit.
5. Publisher have no more data to publish then it calls onComplete().
6. In case of any error in this process, onError() is called to notify Subscriber.

## Back Pressure Or Overflow Strategy 
1. buffer = Keep in memory
2. drop = When queue is full, drop new item
3. latest = When queue is full, keep latest 1 item
4. error = throw error on downstream

## Combine Publishers
1. buffer = Keep in memory
2. drop = When queue is full, drop new item
3. latest = When queue is full, keep latest 1 item
4. error = throw error on downstream

## Sinks
1. one - Mono (1:N)
2. unicast - Flux (1:1)
3. multicast - Flux (1:N)
4. replay - Flux (1:N)


