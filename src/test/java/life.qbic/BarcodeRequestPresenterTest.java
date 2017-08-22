package life.qbic;

import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

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