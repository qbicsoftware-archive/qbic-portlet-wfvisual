package life.qbic;

import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.Sample;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import life.qbic.helpers.BarcodeFunctions;
import life.qbic.helpers.Utils;
import life.qbic.openbis.openbisclient.OpenBisClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static life.qbic.helpers.BarcodeFunctions.checksum;

public class BarcodeRequestModelImpl implements BarcodeRequestModel{

    private final OpenBisClient openBisClient;

    private final static String SPACE = "UKT_DIAGNOSTICS";

    private final static String PROJECTID = "/UKT_DIAGNOSTICS/QUK17";

    private final static String CODE = "QUK17";

    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class);

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVW".toCharArray();

    private static String[] patientSampleIdPair= null;

    public BarcodeRequestModelImpl(OpenBisClient openBisClient){
        this.openBisClient = openBisClient;
    }

    @Override
    public void requestNewPatientSampleIdPair() {
        patientSampleIdPair = new String[2];

        List<Sample> sampleList = openBisClient.getSamplesOfProject(PROJECTID);
        List<Sample> entities = getEntities(sampleList);

        int numberOfNonEntitySamples = sampleList.size() - entities.size();

        // offset is +2, because there is always an attachment sample per project
        String biologicalSampleCode = createBarcodeFromInteger(numberOfNonEntitySamples + 2 );

        // offset is +3, because of the previous created sample and the attachement
        String testSampleCode = createBarcodeFromInteger(numberOfNonEntitySamples + 3);
        String patientId = CODE + "ENTITY-" + (entities.size() + 1);

        patientSampleIdPair[0] = patientId;
        patientSampleIdPair[1] = testSampleCode;

        // Logging code block
        log.debug(String.format("Number of non-entity samples: %s", numberOfNonEntitySamples));
        log.info(String.format("%s: New patient ID created %s", AppInfo.getAppInfo(), patientSampleIdPair[0]));
        log.info(String.format("%s: New sample ID created %s", AppInfo.getAppInfo(), patientSampleIdPair[1]));

        // In case registration fails, return null
        if(!registerNewPatient(patientId, biologicalSampleCode, testSampleCode))
            patientSampleIdPair = null;

    }

    @Override
    public String[] getNewPatientSampleIdPair() {
        return patientSampleIdPair;
    }

    @Override
    public boolean checkIfPatientExists(String sampleID) {
        Sample sample;
        try{
            sample = openBisClient.getSampleByIdentifier("/" + SPACE + "/" + sampleID);
        } catch (Exception exc){
            log.error(exc);
            return false;
        }
        return sample != null;
    }

    /**
     * Registration of a new patient with samples
     * @param patientId A code for the sample type Q_BIOLOGICAL_ENTITY
     * @param biologicalSampleCode A code for the sample type Q_BIOLOGICAL_SAMPLE
     * @param testSampleCode A code for the sample type Q_TEST_SAMPLE
     * @return True, if registration was successful, else false
     */
    private boolean registerNewPatient(String patientId, String biologicalSampleCode, String testSampleCode) {
        if (registerEntity(patientId) &&
                registerBioSample(biologicalSampleCode, "/"+SPACE+"/"+patientId) &&
                registerTestSample(testSampleCode, "/"+SPACE+"/"+biologicalSampleCode)){
            return true;
        }

        return false;
    }

    /**
     * Registration of a new test sample
     * @param testSampleCode A code for the test sample type Q_TEST_SAMPLE
     * @param parent A code for the parent sample type Q_BIOLOGICAL_SAMPLE
     * @return True, if registration was successful, else false
     */
    private boolean registerTestSample(String testSampleCode, String parent) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();
        List<String> parents = new ArrayList<>();

        metadata.put("Q_SAMPLE_TYPE", "DNA");
        parents.add(parent);

        map.put("code", testSampleCode);
        map.put("space", SPACE);
        map.put("project", CODE);
        map.put("experiment", CODE + "E3");
        map.put("type", "Q_TEST_SAMPLE");
        map.put("metadata", metadata);
        map.put("parents", parents);

        params.put(testSampleCode, map);

        try{
            openBisClient.ingest("DSS1", "register-sample-batch", params);
        } catch (Exception exc){
            log.error(exc);
            return false;
        }

        return true;
    }

    /**
     * Registration of a new biological sample
     * @param biologicalSampleCode A code for the sample type Q_BIOLOGICAL_SAMPLE
     * @param parent A code for the parent sample type Q_BIOLOGICAL_ENTITY
     * @return True, if registration was successful, else false
     */
    private boolean registerBioSample(String biologicalSampleCode, String parent) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();
        List<String> parents = new ArrayList<>();

        metadata.put("Q_PRIMARY_TISSUE", "Other");
        metadata.put("Q_TISSUE_DETAILED", "unknown");
        parents.add(parent);

        map.put("code", biologicalSampleCode);
        map.put("space", SPACE);
        map.put("project", CODE);
        map.put("experiment", CODE + "E2");
        map.put("type", "Q_BIOLOGICAL_SAMPLE");
        map.put("metadata", metadata);
        map.put("parents", parents);

        params.put(biologicalSampleCode, map);

        try{
            openBisClient.ingest("DSS1", "register-sample-batch", params);
        } catch (Exception exc){
            log.error(exc);
            return false;
        }

        return true;
    }

    /**
     * Registration of a new test sample
     * @param patientId A code for the sample type Q_BIOLOGICAL_ENTITY
     * @return True, if registration was successful, else false
     */
    private boolean registerEntity(String patientId){
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();

        metadata.put("Q_NCBI_ORGANISM", "9606");

        map.put("code", patientId);
        map.put("space", SPACE);
        map.put("project", CODE);
        map.put("experiment", CODE + "E1");
        map.put("type", "Q_BIOLOGICAL_ENTITY");
        map.put("metadata", metadata);


        params.put(patientId, map);

        try{
            openBisClient.ingest("DSS1", "register-sample-batch", params);
        } catch (Exception exc){
            log.error(exc);
            return false;
        }

        return true;
    }


    /**
     * Get a sample list with samples from type
     * 'Q_BIOLOGICAL_ENTITY' from a list of samples
     * @param sampleList The sample list to be filtered
     * @return The filtered list
     */
    private List<Sample> getEntities(List<Sample> sampleList){
        List<Sample> filteredList = new ArrayList<>();

        for(Sample s : sampleList){
            if (s.getSampleTypeCode().equals("Q_BIOLOGICAL_ENTITY")){
                filteredList.add(s);
            }
        }

        return filteredList;

    }

    /**
     * Creates a complete barcode from a given number using
     * the global project code prefix.
     * Checksum calculation and barcode verification is included.
     * @param number An integer number
     * @return A fully formatted valid QBiC barcode
     */
    private String createBarcodeFromInteger(int number){
        int multiplicator = number / 1000;
        char letter = ALPHABET[multiplicator];

        int remainingCounter = number - multiplicator*1000;

        if (remainingCounter > 999 || remainingCounter < 0){
            return null;
        }

        String preBarcode = CODE + Utils.createCountString(remainingCounter, 3) + letter;

        String barcode = preBarcode + checksum(preBarcode);

        if (!BarcodeFunctions.isQbicBarcode(barcode)){
            log.error(String.format("%s: Barcode created from Integer is not a valid barcode: %s", AppInfo.getAppInfo(),
                    barcode));
            barcode = "";
        }

        return barcode;

    }

}
