package life.qbic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BarcodeRequestPresenterTest {

    private BarcodeRequestView barcodeRequestView;

    private BarcodeRequestPresenter barcodeRequestPresenter;


    @Before
    public void setUp() throws Exception {
        this.barcodeRequestView = new BarcodeRequestViewImpl();
        this.barcodeRequestPresenter = new BarcodeRequestPresenter(this.barcodeRequestView);
    }

    @Test
    public void registeringActionListenerSuccessfull() throws Exception {
        this.barcodeRequestView.getPatentIdSampleIdButton().click();
        Assert.assertTrue(this.barcodeRequestView.getPatientIdField().getDescription() == "");
    }

}