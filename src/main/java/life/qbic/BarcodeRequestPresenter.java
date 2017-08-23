package life.qbic;

import com.vaadin.ui.Notification;
import life.qbic.helpers.Utils;

public class BarcodeRequestPresenter {

    private BarcodeRequestView barcodeRequestView;

    private BarcodeRequestModel barcodeRequestModel;

    public BarcodeRequestPresenter(BarcodeRequestView barcodeRequestView, BarcodeRequestModel barcodeRequestModel){
        this.barcodeRequestView = barcodeRequestView;
        this.barcodeRequestModel = barcodeRequestModel;
        giveLifeToElements();
    }

    private void giveLifeToElements(){

        barcodeRequestView.getPatentIdSampleIdButton().addClickListener(clickEvent -> {
            String[] patientSampleIdPair = barcodeRequestModel.getNewPatientSampleIdPair();

            if (patientSampleIdPair == null){
                Utils.Notification("Something went horribly wrong", "No barcodes created", "error");
            }

            barcodeRequestView.getPatientIdField().setValue(patientSampleIdPair[0]);
            barcodeRequestView.getSampleIdField().setValue(patientSampleIdPair[1]);
        });


    }




}
