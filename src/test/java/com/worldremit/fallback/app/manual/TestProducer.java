package com.worldremit.fallback.app.manual;

import com.worldremit.avro.transfer.TransferCreated;
import com.worldremit.avro.transfer.UserTransferDataWithSnapshotId;
import com.worldremit.fallback.app.UuidUtils;
import com.worldremit.payment.domain.event.PaymentAuthorised;
import com.worldremit.payment.domain.event.PaymentCaptured;
import com.worldremit.payment.domain.event.PaymentCapturedFailed;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

public class TestProducer {

    public static void main(String[] args) {
        String brokerUrl = "localhost:9093";

        String schemaRegistryUrl = "http://localhost:8085";

        String transferCreatedTopicName = "transfer.create.events";

        String paymentEventsTopicName = "payment.events";

        // Configure the Producer
        Map<String, Object> props = new HashMap<>();
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY,
                io.confluent.kafka.serializers.subject.RecordNameStrategy.class);

        DefaultKafkaProducerFactory pf = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate template = new KafkaTemplate<>(pf, true);

        String trasnsferId = java.util.UUID.randomUUID().toString();
        String amountDueId = java.util.UUID.randomUUID().toString();

        UserTransferDataWithSnapshotId recipient = UserTransferDataWithSnapshotId.newBuilder().setCountryCode("NG")
                .setUserId(UuidUtils.randomUUID()).setUserSnapshotId(UuidUtils.randomUUID()).build();

        UserTransferDataWithSnapshotId sender = UserTransferDataWithSnapshotId.newBuilder().setCountryCode("AU")
                .setUserId(UuidUtils.randomUUID()).setUserSnapshotId(UuidUtils.randomUUID()).build();

        TransferCreated transferCreated = TransferCreated.newBuilder().setTransferId(UuidUtils.fromString(trasnsferId))
                .setAmountDueId(UuidUtils.fromString(amountDueId)).setPricingId(UuidUtils.randomUUID())
                .setRecipient(recipient).setSender(sender).build();

        ProducerRecord<String, TransferCreated> transferCreatedRecord = new ProducerRecord<>(transferCreatedTopicName,
                transferCreated.getTransferId().getValue(), transferCreated);

        PaymentAuthorised paymentAuthorised = PaymentAuthorised.newBuilder()
                .setAmountDueId(UuidUtils.fromString(amountDueId)).setFraudRiskScore(100).build();
        ProducerRecord<String, PaymentAuthorised> paymentAuthorisedRecord = new ProducerRecord<>(paymentEventsTopicName,
                paymentAuthorised.getAmountDueId().getValue(), paymentAuthorised);

        PaymentCaptured paymentCaptured = PaymentCaptured.newBuilder().setAmountDueId(UuidUtils.fromString(amountDueId)).setEid(UuidUtils.randomUUID()).build();
        ProducerRecord<String, PaymentCaptured> paymentCapturedRecord = new ProducerRecord<>(paymentEventsTopicName, paymentCaptured.getAmountDueId().getValue(), paymentCaptured);

        PaymentCapturedFailed paymentCapturedFailed = PaymentCapturedFailed.newBuilder().setAmountDueId(UuidUtils.fromString(amountDueId)).setEid(UuidUtils.randomUUID()).build();
        ProducerRecord<String, PaymentCapturedFailed> paymentCapturedFailedRecord = new ProducerRecord<>(paymentEventsTopicName, paymentCaptured.getAmountDueId().getValue(), paymentCapturedFailed);

        template.send(transferCreatedRecord);
        template.send(paymentAuthorisedRecord);
        template.send(paymentCapturedRecord);
        template.send(paymentCapturedFailedRecord);

    }

}
