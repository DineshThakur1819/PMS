package com.pms.pmsmodel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PMSMessage {

    ArrayList<PMSField> fields = null;

    PMSConstants.PMSMessageType messageType;

    TransportHeader transportHeader = new TransportHeader();
    PresentationHeader presentationHeader = new PresentationHeader();

    public PMSMessage(PMSConstants.PMSMessageType messageType, PMSConstants.RequestResponseType requestResponseType, String responseCode, boolean moreDataAvailable) {
        this.messageType = messageType;
        presentationHeader.setRequestResponseType(requestResponseType);
        presentationHeader.setMessageType(messageType);
        presentationHeader.setMoreIndicator(moreDataAvailable ? "1" : "0");
        presentationHeader.setResponseCode(responseCode);

        fields = new ArrayList<>();
    }

    public void addField(PMSField field) {
        fields.add(field);
    }

    public byte[] getMessage() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            //bos.write(PMSConstants.STX);
            bos.write(transportHeader.getData());
            bos.write(presentationHeader.getData());
            for (PMSField field : fields) {
                bos.write(field.getData());
            }
            bos.write(PMSConstants.ETX);

            bos.write(PMSUtil.calculateLRC(bos.toByteArray()));

            byte[] data = bos.toByteArray();
            bos.reset();

            bos.write(PMSConstants.STX);
            bos.write(data);


        } catch (IOException e) {

        }
        return bos.toByteArray();
    }


}
