package com.pms.pmsmodel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationHeader {
    final String formatVersion = "1";
    PMSConstants.RequestResponseType requestResponseType = null;
    PMSConstants.PMSMessageType messageType = null;
    String responseCode = null;
    String moreIndicator = null;

    public byte[] getData() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            bos.write(formatVersion.getBytes());

            if(requestResponseType !=null)
                bos.write(requestResponseType.getType().getBytes());

            if(messageType !=null)
                bos.write(messageType.getType().getBytes());

            if(responseCode !=null)
                bos.write(responseCode.getBytes());

            if(moreIndicator !=null)
                bos.write(moreIndicator.getBytes());

            bos.write(PMSConstants.FS);


        } catch (IOException e) {

        }
        return bos.toByteArray();
    }
}
