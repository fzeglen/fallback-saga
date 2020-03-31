package com.worldremit.fallback.app.config;

public abstract class Profiles {

    private Profiles() {
    }

    public static final String PROD = "prod";

    public static final String DEV = "dev";

    public static final String TST = "tst";

    public static final String PPD = "ppd";

    public static final String LOCAL = "local";

    public static final String TEST = "test";

    public static final String ACCEPTANCE_TEST = "acceptanceTest";

    public static final String NOT_ACCEPTANCE_TEST = "!" + ACCEPTANCE_TEST;

}