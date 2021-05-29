package com.pms.pmsmodel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportHeader {
    final String headerType = "60";
    final String destination = "0000";
    final String source = "0000";

    public byte[] getData() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            bos.write(headerType.getBytes());
            bos.write(destination.getBytes());
            bos.write(source.getBytes());
        } catch (IOException e) {

        }
        return bos.toByteArray();
    }
}
