package life.qbic;

import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.Sample;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import life.qbic.helpers.Utils;
import life.qbic.openbis.openbisclient.OpenBisClient;

import java.util.ArrayList;
import java.util.List;

import static life.qbic.helpers.BarcodeFunctions.checksum;

public class BarcodeRequestModelImpl implements BarcodeRequestModel{

    private final OpenBisClient openBisClient;

    private final static String PROJECTID = "/UKT_DIAGNOSTICS/QUK17";

    private final static String CODEPREFIX = "QUK17";

    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class);

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVW".toCharArray();

    public BarcodeRequestModelImpl(OpenBisClient openBisClient){
        this.openBisClient = openBisClient;
    }

    @Override
    public String[] getNewPatientSampleIdPair() {
        String[] idPair = new String[2];

        List<Sample> sampleList = openBisClient.getSamplesOfProject(PROJECTID);
        List<Sample> entities = getEntities(sampleList);

        log.info(String.format("Number of non-entity samples: %s", sampleList.size() - entities.size()));

        String barcode = createBarcodeFromInteger(sampleList.size() - entities.size());


        idPair[0] = barcode;
        log.info(String.format("Barcode is %s", barcode));

        return idPair;
    }


    private List<Sample> getEntities(List<Sample> sampleList){
        List<Sample> filteredList = new ArrayList<>();

        for(Sample s : sampleList){
            if (s.getSampleTypeCode().equals("Q_BIOLOGICAL_ENTITY")){
                filteredList.add(s);
            }
        }

        return filteredList;

    }

    private String createBarcodeFromInteger(int number){
        int multiplicator = number / 1000;
        char letter = ALPHABET[multiplicator];

        int remainingCounter = number - multiplicator*1000;

        if (remainingCounter > 999 || remainingCounter < 0){
            return null;
        }

        String preBarcode = CODEPREFIX + Utils.createCountString(remainingCounter, 3) + letter;
        return preBarcode + checksum(preBarcode);

    }

}
