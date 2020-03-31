package com.worldremit.fallback.app;

import com.worldremit.avro.UUID;

public class UuidUtils {

	public static UUID randomUUID() {
		return new UUID(java.util.UUID.randomUUID().toString());
	}

	public static UUID fromString(String uuid) {
		return new UUID(java.util.UUID.fromString(uuid).toString());
	}

}
