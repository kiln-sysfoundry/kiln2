package org.sysfoundry.kiln.eventing.kafka.subsys;

import org.slf4j.Logger;
import org.sysfoundry.kiln.eventing.Eventbus;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;
import org.sysfoundry.kiln.health.Log;

import java.util.Map;
import java.util.Properties;

import static org.sysfoundry.kiln.eventing.kafka.subsys.KafkaEventbusSubsys.NAME;
import static org.sysfoundry.kiln.factory.Util.imMap;
import static org.sysfoundry.kiln.factory.Util.singleton;

public class KafkaEventbusInstanceFactory extends MapBaseInstanceFactory {

    private static final Logger log = Log.get(NAME);

    public KafkaEventbusInstanceFactory(InstanceFactory parent) {
        super(parent, imMap(
                singleton(parent, Eventbus.class,instanceFactory -> {
                    try {
                        KafkaEventbusSubsysConfig kafkaEventbusSubsysConfig = instanceFactory.get(KafkaEventbusSubsysConfig.class);
                        Properties kafkaProducerConfigProperties = new Properties();
                        Properties kafkaConsumerConfigProperties = new Properties();

                        Map<String, Object> producerConfig = kafkaEventbusSubsysConfig.getProducerConfig();
                        Map<String,Object> consumerConfig = kafkaEventbusSubsysConfig.getConsumerConfig();

                        producerConfig.forEach((k,v)->{
                            kafkaProducerConfigProperties.put(k,v);
                        });

                        consumerConfig.forEach((k,v)->{
                            kafkaConsumerConfigProperties.put(k,v);
                        });

                        log.debug("Kafka Producer Configuration Properties {}",kafkaProducerConfigProperties);
                        log.debug("Kafka Consumer Configuration Properties {}",kafkaConsumerConfigProperties);


                        Eventbus kafkaEventbus = new KafkaEventbus(kafkaProducerConfigProperties,
                                kafkaConsumerConfigProperties,kafkaEventbusSubsysConfig.getConsumerPollMillis());


                        return kafkaEventbus;
                    } catch (FactoryException e) {
                        e.printStackTrace();
                        throw new RuntimeException(String.format("Unabled to load kafka eventbus configuration due to {}",e.getMessage()));
                    }


                }),
                singleton(parent,EventHandlerRegistrationFilter.class,instanceFactory -> {
                    return new EventHandlerRegistrationFilter(instanceFactory);
                })
        ));
    }
}
