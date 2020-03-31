package com.worldremit.fallback.app.fallback.saga;

import com.worldremit.avro.transfer.TransferCreated;
import com.worldremit.fallback.app.config.kafka.KafkaTopics;
import com.worldremit.payment.domain.event.PaymentAuthorised;
import com.worldremit.payment.domain.event.PaymentCaptured;
import com.worldremit.payment.domain.event.PaymentCapturedFailed;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.BiConsumer;

@Configuration
public class FallbackSagaKstreams {

    private final JoinWindows joinWindow = JoinWindows.of(Duration.of(5, ChronoUnit.MINUTES));

    @Bean
    public BiConsumer<KStream<String, SpecificRecord>, KStream<String, TransferCreated>> processPaymentEvents() {
        return (paymentsEvents, transferCreatedKStream) -> {

            var transferCreatedRekey = transferCreatedKStream
                    .filter((key, value) -> value instanceof TransferCreated)
                    .selectKey((key, value) -> value.getAmountDueId().getValue())
                    .through(KafkaTopics.TRANSFER_CREATED_BY_AMOUNT_DUE_ID_REKEY);

            var paymentAuthorisedStream = paymentsEvents.filter((key1, record) -> record instanceof PaymentAuthorised || record instanceof PaymentCaptured || record instanceof PaymentCapturedFailed)
                    .join(transferCreatedRekey,
                            (paymentAuthorised, transferCreated) -> KeyValue
                                    .pair(transferCreated.getTransferId().getValue(), paymentAuthorised),
                            joinWindow)
                    .map((key, value) -> value);
            paymentAuthorisedStream.through(KafkaTopics.FALLBACK_PAYMENTS_SAGA_EVENTS);
        };
    }

}
