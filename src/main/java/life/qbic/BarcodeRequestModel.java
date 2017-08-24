package life.qbic;

public interface BarcodeRequestModel {

    /**
     * This method will request a new patient
     * and sample ID pair from openBIS and
     * return it in a String array with
     */
    void requestNewPatientSampleIdPair();

    /**
     * This method will return you the ID pair, after you submit the request
     * in the first place.
     * @return An array with the patient ID and sample ID
     *     array[0] => patient ID
     *     array[1] => sample ID
     */
    String[] getNewPatientSampleIdPair();

}
