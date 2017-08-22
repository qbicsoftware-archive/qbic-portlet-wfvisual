package life.qbic;

public interface BarcodeRequestModel {

    /**
     * This method will request a new patient
     * and sample ID pair from openBIS and
     * return it in a String array with
     *     array[0] => patient ID
     *     array[1] => sample ID
     * @return
     */
    String[] getNewPatientSampleIdPair();

}
