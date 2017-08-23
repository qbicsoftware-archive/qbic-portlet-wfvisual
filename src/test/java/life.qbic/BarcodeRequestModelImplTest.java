package life.qbic;

import life.qbic.openbis.openbisclient.OpenBisClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.powermock.api.mockito.PowerMockito.mock;

public class BarcodeRequestModelImplTest {

    private OpenBisClient openBisClient;

    private BarcodeRequestModel model;

    private static final String CURRENTPROJECTID = "QUK17";

    @Before
    public void setUp() throws Exception{
        openBisClient = mock(OpenBisClient.class);
        model = new BarcodeRequestModelImpl(openBisClient);
    }

    @Test
    public void request_new_patient_sample_id_pair_successfully() throws Exception{
        Assert.assertTrue("The return value was null, which should have been a String array", model.getNewPatientSampleIdPair() != null);
    }

}