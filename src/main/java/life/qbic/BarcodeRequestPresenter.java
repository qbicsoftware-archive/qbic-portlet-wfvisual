package life.qbic;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import life.qbic.helpers.Utils;

public class BarcodeRequestPresenter {
    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class);

    private BarcodeRequestView barcodeRequestView;

    private BarcodeRequestModel barcodeRequestModel;

    private String[] patientSampleIdPair;

    public BarcodeRequestPresenter(BarcodeRequestView barcodeRequestView, BarcodeRequestModel barcodeRequestModel){
        this.barcodeRequestView = barcodeRequestView;
        this.barcodeRequestModel = barcodeRequestModel;
        giveLifeToElements();
    }

    private void giveLifeToElements(){

        barcodeRequestView.getPatentIdSampleIdButton().addClickListener(clickEvent -> {
            Thread request = new RequestThread(barcodeRequestView.getSpinner());
            request.start();
        });


    }


    class RequestThread extends Thread {

        ProgressBar bar;

        public RequestThread(ProgressBar bar){
            this.bar = bar;
            bar.setVisible(true);
        }

        @Override
        public void run() {

            UI.getCurrent().access(() -> bar.setVisible(true));

            barcodeRequestModel.requestNewPatientSampleIdPair();

            patientSampleIdPair = barcodeRequestModel.getNewPatientSampleIdPair();

            if (patientSampleIdPair == null){
                Utils.Notification("Something went horribly wrong", "No barcodes created", "error");
            } else {
                barcodeRequestView.getPatientIdField().setValue(patientSampleIdPair[0]);
                barcodeRequestView.getSampleIdField().setValue(patientSampleIdPair[1]);
            }

            UI.getCurrent().access(() -> bar.setVisible(false));
        }
    }


}
