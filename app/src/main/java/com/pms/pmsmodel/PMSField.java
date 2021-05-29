package com.pms.pmsmodel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PMSField {
    String type = null;
    String value = null;

    public PMSField(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public byte[] getData() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {

            if(type !=null)
                bos.write(type.getBytes());

            if(value !=null) {
                String length = String.format("%04d", value.length());
                bos.write(CommonUtils.hexStringToByteArray(length));
                bos.write(value.getBytes());
            }
            bos.write(PMSConstants.FS);
        } catch (IOException e) {

        }
        return bos.toByteArray();
    }
}
