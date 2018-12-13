# KES - Kafka Event Sourcing #

Main aim of this PoC is to check how to prepare backend for SPA frontend applications with reach business logic and not just CRUDs. Second aim is to be able to store all user actions to be easily auditable and to be able to change functionality of application without tricky migrations between versions of model. 

Due to previous requirements this PoC attempts to create functional Event Sourcing example with not most trivial scenario which is word counter you can find everywhere. For main persistence platform I chose Apache Kafka with Spring Cloud Streams on top of it. 

## Flows of data ##

There is basically 4 flows of data in this PoC:
 1. create / update single object (aggregate) with events
 1. transform events into aggregate and to listable snapshot objects
 1. get single object
 1. get list of listable objects

![Idea](idea.png)

### Create and Update operation ### 

This flow starts with FrontApp or Curl calling POST / PUT / PATCH / DELETE controller methods, and finishes with storage of result event in Kafka persistent topic. 

### Transform event into aggregate and listable snapshot ### 

This flow starts where previous finished. Topic is being listened by consumer and transforms events into materialized aggregate to kafka KTable and to SQL database. 

### Get single object ### 

This flow searches for object in kafka KTable and returns it to FronApp. 

### Get list of objects ### 

Because of usual requirements for lists like filtering, sorting and such, this flow calls SQL db for lists of objects. 

## Persistence ## 

There is 3 locations where data is being stored: 
 * Kafka Topic / KStream 
 * Kafka table / Ktable
 * SQL database
 
Topic persistence is the most important of them all, because if other models change (aggregate and listable) then they get regenerated from events stored in topic. Due to this topic retention has to be set to something awfully long like 1000 years. Also it's worth to be mentioned that is't intention of this PoC not to compact this persistent topics in any way. Thanks to this topic remains unchangeable source of truth for further transformations. 

Kafka KTable is place where we can store materialized / flattened main aggregate object. If we were in CQRS world it would be main Query model part. KTable seams to be perfect storage for this kind of object because it is near to Topic and materialization feels almost inseparable part of first flow - event persistence into topic. Also if there is need to change main aggregate object it's matter of reset topic consumer (easiest way would be change of a consumer group) and watch topic being materialized into new aggregate. 

SQL database seams to best place to serve lists with all blows and whistles they require - sorting, filtering, etc. It can easily be replaced with another solution like Elasticseach or MongDB if anothere features are required. Like KTable it can be easily regenerated. 

## Run and use ##

briefly:
```bash
brew install confluent-oss
confluent start
mvn spring-boot:run
curl -v -X POST http://localhost:8080/projects/
date && for i in `seq 1 1000`; do curl -d "name=test$i" -X PATCH http://localhost:8080/projects/UUID-returned-in-Location-Header; done && date
```

## Code description ##

TODO

## Future work ## 
 * Microservices and multiple instances of application
 * Rolling updates of infrastructure
 * CQRS?