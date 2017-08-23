package life.qbic;

import life.qbic.openbis.openbisclient.OpenBisClient;

public class BarcodeRequestModelImpl implements BarcodeRequestModel{

    private final OpenBisClient openBisClient;

    private final static String PROJECTID = "QUK17";

    public BarcodeRequestModelImpl(OpenBisClient openBisClient){
        this.openBisClient = openBisClient;
    }

    @Override
    public String[] getNewPatientSampleIdPair() {


        return null;

    }
}
