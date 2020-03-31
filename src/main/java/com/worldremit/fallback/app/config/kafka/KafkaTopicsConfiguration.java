package com.worldremit.fallback.app.config.kafka;

import com.worldremit.fallback.app.config.Profiles;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({Profiles.LOCAL, Profiles.TEST, Profiles.ACCEPTANCE_TEST})
public class KafkaTopicsConfiguration {

    @Bean
    public NewTopic authFallbackPaymentAuthorised(@Value("${app.kafka.partitionCount}") Integer DEFAULT_NUM_PARTITIONS) {
        return new NewTopic(KafkaTopics.FALLBACK_PAYMENTS_SAGA_EVENTS, DEFAULT_NUM_PARTITIONS, (short) 1);
    }

    @Bean
    public NewTopic transferCreatedByAmountDueIdTopic(@Value("${app.kafka.partitionCount}") Integer DEFAULT_NUM_PARTITIONS) {
        return new NewTopic(KafkaTopics.TRANSFER_CREATED_BY_AMOUNT_DUE_ID_REKEY, DEFAULT_NUM_PARTITIONS, (short) 1);
    }

}
