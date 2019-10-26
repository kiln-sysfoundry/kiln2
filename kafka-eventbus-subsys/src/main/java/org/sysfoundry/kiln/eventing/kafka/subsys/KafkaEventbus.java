package org.sysfoundry.kiln.eventing.kafka.subsys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.sysfoundry.kiln.eventing.Event;
import org.sysfoundry.kiln.eventing.EventHandler;
import org.sysfoundry.kiln.eventing.Eventbus;
import org.sysfoundry.kiln.health.Log;

import java.time.Duration;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.sysfoundry.kiln.eventing.kafka.subsys.KafkaEventbusSubsys.NAME;
import static org.sysfoundry.kiln.formats.data.JsonUtil.toJSON;

class KafkaEventbus implements Eventbus {

    private Properties kafkaProducerConfig;
    private Properties kafkaConsumerConfig;

    private KafkaProducer<String,String> kafkaProducer;
    private KafkaConsumer<String,String> kafkaConsumer;
    private KafkaConsumerRunner kafkaConsumerRunner;
    private Thread consumerThread;

    private Multimap<String,EventHandler> eventHandlerMultiMap = HashMultimap.create();
    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private static final Logger log = Log.get(NAME);

    KafkaEventbus(Properties kafkaProducerConfig,Properties kafkaConsumerConfig){
        this.kafkaProducerConfig = kafkaProducerConfig;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
    }

    @Override
    public void publish(String topic, Event event) {
        try {
            kafkaProducer.send(new ProducerRecord<>(topic,event.getUuid().toString(),toJSON(event,Event.class)));
        } catch (JsonProcessingException e) {
            log.warn("Failed to send event {} to topic {} due to error {}",event,topic,e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(String topic, EventHandler handler) {
        if(isStarted.get()){
            throw new RuntimeException("Eventbus already started no more subscriptions allowed!");
        }
        eventHandlerMultiMap.put(topic,handler);
    }

    @Override
    public void start() {
        this.kafkaProducer = new KafkaProducer<String, String>(kafkaProducerConfig);
        log.info("Created instance of KafkaProducer {}",kafkaProducer);

        this.kafkaConsumer = new KafkaConsumer<String, String>(kafkaConsumerConfig);
        log.info("Created instance of KafkaConsumer {}",kafkaConsumer);


        kafkaConsumerRunner = new KafkaConsumerRunner(kafkaConsumer,
                new ImmutableMultimap.Builder<String,EventHandler>().putAll(eventHandlerMultiMap).build());

        consumerThread = new Thread(kafkaConsumerRunner);

        consumerThread.start();
        isStarted.compareAndSet(false,true);
    }

    @Override
    public void stop() {
        if(this.kafkaProducer != null){
            this.kafkaProducer.close();
            log.info("Closed KafkaProducer instance {}",kafkaProducer);
            this.kafkaProducer = null;
        }

        if(this.kafkaConsumerRunner != null){
            this.kafkaConsumerRunner.shutdown();
            this.kafkaConsumerRunner = null;
            consumerThread = null;
        }

        isStarted.compareAndSet(true,false);
    }

    static class KafkaConsumerRunner implements Runnable {
        private final AtomicBoolean closed = new AtomicBoolean(false);
        private final KafkaConsumer consumer;
        private final ImmutableSet<String> topics;
        private final ImmutableMultimap<String,EventHandler> topicEventHandlers;

        public KafkaConsumerRunner(KafkaConsumer consumer,
                                   ImmutableMultimap<String,EventHandler> topicEventHandlers) {
            this.consumer = consumer;
            this.topics = topicEventHandlers.keySet();
            this.topicEventHandlers = topicEventHandlers;
        }

        public void run() {
            try {
                consumer.subscribe(topics);
                while (!closed.get()) {
                    ConsumerRecords records = consumer.poll(Duration.ofMillis(10000));
                    log.info("Received Consumer Record {}",records);
                    Iterator messageIterator = records.iterator();

                    while(messageIterator.hasNext()){
                        ConsumerRecord nextMessage = (ConsumerRecord)messageIterator.next();
                        Object value = nextMessage.value();
                        log.info("Received Message {}",value);

                    }
                    // Handle new records
                    //
                }
            } catch (WakeupException e) {
                // Ignore exception if closing
                if (!closed.get()) throw e;
            } finally {
                consumer.close();
            }
        }

        // Shutdown hook which can be called from a separate thread
        public void shutdown() {
            closed.set(true);
            consumer.wakeup();
        }
    }

}
