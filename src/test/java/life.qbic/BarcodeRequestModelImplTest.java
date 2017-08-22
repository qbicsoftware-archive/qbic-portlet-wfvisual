package life.qbic;

import life.qbic.openbis.openbisclient.OpenBisClient;
import org.junit.Before;

import static org.powermock.api.mockito.PowerMockito.mock;

public class BarcodeRequestModelImplTest {


    private OpenBisClient openBisClient;

    private BarcodeRequestModel model;

    @Before
    public void setUp() throws Exception{
        openBisClient = mock(OpenBisClient.class);
        model = new BarcodeRequestModelImpl(openBisClient);
    }



}