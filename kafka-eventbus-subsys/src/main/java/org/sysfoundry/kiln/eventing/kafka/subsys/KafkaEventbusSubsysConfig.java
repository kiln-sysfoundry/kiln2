package org.sysfoundry.kiln.eventing.kafka.subsys;

import lombok.Data;

import java.util.Map;

@Data
public class KafkaEventbusSubsysConfig {


    private Map<String,Object> producerConfig;

    private Map<String,Object> consumerConfig;


    private Long consumerPollMillis;
}
