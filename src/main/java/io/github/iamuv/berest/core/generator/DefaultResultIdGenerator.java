package io.github.iamuv.berest.core.generator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class DefaultResultIdGenerator implements ResultIdGenerator {

    private static final char HYPHEN = '-';

    private String localIp;

    {
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateId(Object... seeds) {
        StringBuilder id = new StringBuilder(localIp.replace(".", ""));
        Arrays.stream(seeds).forEach(seed -> {
            if (seed != null) id.append(HYPHEN);
            int hashCode = seed.hashCode();
            if (hashCode < 0) {
                hashCode = -hashCode;
                id.append('0').append(hashCode);
            } else {
                id.append(hashCode);
            }
        });
        return id.toString();
    }
}
