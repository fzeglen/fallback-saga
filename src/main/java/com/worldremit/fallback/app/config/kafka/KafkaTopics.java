package com.worldremit.fallback.app.config.kafka;

public abstract class KafkaTopics {

    private KafkaTopics() {
    }

    public static final String FALLBACK_EVENTS = "fallback.events";

    public static final String TRANSFER_CREATE_EVENTS = "transfer.create.events";

    public static final String FALLBACK_AGGREGATES = "fallback.aggregates.events";

    public static final String FALLBACK_COMMANDS = "fallback.commands";

    public static final String PAYMENT_COMMANDS = "payment.commands";

    public static final String TRANSFER_STATUS_EVENTS = "transfer.status.events";

    public static final String PAYMENT_EVENTS = "payment.events";

    // TODO: find better naming
    public static final String TRANSFER_CREATED_BY_AMOUNT_DUE_ID_REKEY = "fallback.transfer.status.created.by.amountdueid.events";

    public static final String FALLBACK_PAYMENTS_SAGA_EVENTS = "fallback.saga.payments.events";

}
