package com.uiloalxise.pojo.entity.payload;

import java.security.PublicKey;
import java.util.Arrays;

public class CustomPublicKey implements PublicKey {

    private byte[] encoded;

    public CustomPublicKey(byte[] encoded) {
        this.encoded = Arrays.copyOf(encoded, encoded.length);
    }

    @Override
    public String getAlgorithm() {
        return "Ed25519";
    }

    @Override
    public String getFormat() {
        return "X.509";
    }

    @Override
    public byte[] getEncoded() {
        return Arrays.copyOf(encoded, encoded.length);
    }
}
