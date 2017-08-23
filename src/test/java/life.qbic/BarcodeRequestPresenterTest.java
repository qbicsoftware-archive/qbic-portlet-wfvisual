package life.qbic;

import life.qbic.openbis.openbisclient.OpenBisClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BarcodeRequestPresenterTest {

    private BarcodeRequestView barcodeRequestView;

    private BarcodeRequestPresenter barcodeRequestPresenter;

    private BarcodeRequestModel barcodeRequestModel;

    private OpenBisClient openBisClient;

    @Before
    public void setUp() throws Exception {
        this.openBisClient = new OpenBisClient("test", "test", "test");
        this.barcodeRequestView = new BarcodeRequestViewImpl();
        this.barcodeRequestModel = new BarcodeRequestModelImpl(openBisClient);

        this.barcodeRequestPresenter = new BarcodeRequestPresenter(this.barcodeRequestView, this.barcodeRequestModel);
    }

    @Test
    public void registeringActionListenerSuccessfull() throws Exception {
        this.barcodeRequestView.getPatentIdSampleIdButton().click();
        Assert.assertTrue(this.barcodeRequestView.getPatientIdField().getDescription() == "");
    }

}