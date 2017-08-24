package life.qbic;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import life.qbic.helpers.Utils;

public class BarcodeRequestPresenter {
    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class);

    private BarcodeRequestView barcodeRequestView;

    private BarcodeRequestModel barcodeRequestModel;

    private String[] patientSampleIdPair;

    public BarcodeRequestPresenter(BarcodeRequestView barcodeRequestView, BarcodeRequestModel barcodeRequestModel) {
        this.barcodeRequestView = barcodeRequestView;
        this.barcodeRequestModel = barcodeRequestModel;
        giveLifeToElements();
    }

    private void giveLifeToElements() {

        barcodeRequestView.getPatentIdSampleIdButton().addClickListener(clickEvent -> {
            Thread request = new RequestThread();
            request.start();
            UI.getCurrent().setPollInterval(50);
        });


    }

    /**
     * An own thread for the heavy task of the barcode request and
     * sample registration
     */
    class RequestThread extends Thread {

        ProgressBar bar;
        HorizontalLayout container;
        Label loadingLabel;

        public RequestThread() {
            this.bar = barcodeRequestView.getSpinner();
            this.loadingLabel = barcodeRequestView.getLoadingLabel();
            this.container = barcodeRequestView.getSpinnerContainer();
            bar.setVisible(true);
        }

        @Override
        public void run() {
            // Thread-safe UI access
            UI.getCurrent().access(() -> {
                container.setVisible(true);
                loadingLabel.setValue("Patient and Sample IDs are requested ...");
                barcodeRequestView.getPatentIdSampleIdButton().setEnabled(false);
            });

            barcodeRequestModel.requestNewPatientSampleIdPair();

            patientSampleIdPair = barcodeRequestModel.getNewPatientSampleIdPair();

            if (patientSampleIdPair == null) {
                Utils.Notification("Something went horribly wrong", "No barcodes created", "error");
            } else {
                barcodeRequestView.getPatientIdField().setValue(patientSampleIdPair[0]);
                barcodeRequestView.getSampleIdField().setValue(patientSampleIdPair[1]);
            }

            UI.getCurrent().access(() -> {
                container.setVisible(false);
                barcodeRequestView.getPatentIdSampleIdButton().setEnabled(true);
            });

            // Stop polling
            UI.getCurrent().setPollInterval(-1);
        }
    }
}