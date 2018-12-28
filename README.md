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

This flow starts where previous finished. Topic is being listened by two consumers and transforms events into materialized aggregate to kafka KTable and to simple table in SQL database. 

### Get single object ### 

This flow searches for object in kafka KTable and returns it. 

### Get list of objects ### 

Because of usual requirements for lists like filtering, sorting and such, this flow calls SQL db for lists of objects. This PoC doesnt implement sorting and pagination. Just simple "get all" operation.   

## Persistence ## 

There is 3 locations where data is being stored: 
 * Kafka Topic / KStream 
 * Kafka table / Ktable
 * SQL database
 
Topic persistence is the most important of them all, because if other models change (aggregate and listable) then they get regenerated from events stored in topic. Due to this topic retention has to be set to something awfully long like 1000 years. Also it's worth to be mentioned that its intention of this PoC not to compact this persistent topics in any way. Thanks to this topic remains unchangeable source of truth for further transformations. Only reason to use topic compaction would be requirement to delete main object from Kafka, but it's also not scope of this PoC. 

Kafka KTable is place where we can store materialized / flattened main aggregate object. KTable seams to be perfect storage for this kind of object because it is near to Topic and materialization feels almost inseparable part of first flow - event persistence into topic. Also if there is need to change main aggregate object it's matter of reset topic consumer (check **How to regenerate KTable or SQL database** section) and watch topic being materialized into new aggregate. 

SQL database seams to best place to serve lists with all blows and whistles they require - sorting, filtering, etc. It can easily be replaced with another solution like Elasticseach or MongoDB if another features are required. Like KTable it can be easily regenerated. 

### How to regenerate KTable or SQL database ###

Due to setting in kafka `server.properties:log.retention.hours=10000000` topic entries have whole time in the world :D They basically stay there forever. Thanks to this if there is need to regenerate data in derived stores (KTable and SQL DB) it can be done by change of `group` parameter in consumer binding configuration. New consumption group is then instantiated and all events gets consumed again. I would change `v1` part in following code into `v2`.  

```yaml
domain-event-consumer:
  ...
  group: domain-event-consumer-v1
```

## Run and use ##

briefly:
```bash
brew install confluent-oss
confluent start
mvn spring-boot:run
curl -v -X POST http://localhost:8080/projects/
./performance.sh
```

## Code description ##

Code stays in 3 main sections:
 - domain - domain model, events and business service
 - delivery - front controller and data transfer objects 
 - infrastructure - Beans configurations; JPA model, repository and service; Kafka producer, consumers and client
 
 Main meat sits in `it.mltk.kes.infrastructure.streams.client.ProjectClientImpl` where save and find methods are implemented. Save publishes to Kafka topic and find queries Kafka KTable. Second interesting part sits in `it.mltk.kes.infrastructure.streams.consumer.ToProjectConsumerImpl` where transformation of events into stored aggregate happens.  

## Future work ## 
 * Microservices and multiple instances of application
 * Rolling updates of infrastructure
 * separation into command and query front objects. 