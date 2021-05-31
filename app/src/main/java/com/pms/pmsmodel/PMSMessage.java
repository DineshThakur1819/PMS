package com.pms.pmsmodel;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PMSMessage {

    private static final String TAG = "PMSMessage";

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

            String length = String.format("%04d", bos.toByteArray().length);

            bos.write(PMSUtil.calculateLRC(bos.toByteArray()));


            byte[] data = bos.toByteArray();
            bos.reset();

//            Log.e(TAG, "getMessage: " + data.length);


            bos.write(PMSConstants.STX);

            bos.write(CommonUtils.hexStringToByteArray(length));
//            bos.write(PMSConstants.SP);
//            bos.write(PMSConstants.LL);
////            bos.write(PMSConstants.SP);
//            bos.write(PMSConstants.LL1);
//            bos.write(PMSConstants.SP);
            bos.write(data);


        } catch (IOException e) {

        }
        return bos.toByteArray();
    }


}
