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

        barcodeRequestView.getTaskSelectionGroup().addValueChangeListener(value -> {
            if(barcodeRequestView.getTaskSelectionGroup().getValue().toString().contains("patient/sample")) {
                barcodeRequestView.getCreatePatientContainer().setVisible(true);
                barcodeRequestView.getCreateSampleContainer().setVisible(false);
            } else{
                barcodeRequestView.getCreatePatientContainer().setVisible(false);
                barcodeRequestView.getCreateSampleContainer().setVisible(true);
            }
        });

        barcodeRequestView.getPatentIdSampleIdButton().addClickListener(clickEvent -> {
            Thread request = new RequestThread();
            request.start();
            UI.getCurrent().setPollInterval(50);
        });


        barcodeRequestView.getCreateSampleButton().addClickListener(clickEvent -> {
            Thread request = new NewSampleRequestThread();
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
            UI.getCurrent().getSession().lock();

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

            UI.getCurrent().getSession().unlock();
        }
    }


    class NewSampleRequestThread extends Thread {
        ProgressBar bar;
        HorizontalLayout loadingInfoContainer;
        Label loadingLabel;

        public NewSampleRequestThread(){
            this.bar = barcodeRequestView.getSpinner();
            this.loadingLabel = barcodeRequestView.getLoadingLabel();
            this.loadingInfoContainer = barcodeRequestView.getSpinnerContainer();
        }

        @Override
        public void run() {

            String patientID =
                    barcodeRequestView.getPatientIdInputField().getValue().trim();
            UI.getCurrent().getSession().lock();

            UI.getCurrent().access(() -> {
               loadingInfoContainer.setVisible(true);
               loadingLabel.setValue("Check if patient ID is valid ...");
               barcodeRequestView.getCreateSampleButton().setEnabled(false);
            });

            if (barcodeRequestView.getPatientIdField().getValue().isEmpty() ||
                    patientID.contains(" ")){
                Utils.Notification("Wrong/missing patient ID",
                        "Please enter a valid patient ID (like Q****ENTITY-1", "error");
                log.error("Wrong or empty patient ID " + patientID);
                barcodeRequestView.getPatientIdInputField().addStyleName("textfield-red");
            } else {
                if(!barcodeRequestModel.checkIfPatientExists(patientID)){
                    Utils.Notification("Patient ID does not exist yet.",
                            "Please request a new patient/sample pair (Selection 1)", "error");
                    log.error("Patient with ID " + patientID + " could not be found!");
                    barcodeRequestView.getPatientIdInputField().addStyleName("textfield-red");
                } else {
                    barcodeRequestView.getPatientIdInputField().removeStyleName("textfield-red");
                    barcodeRequestView.getPatientIdInputField().addStyleName("textfield-green");
                }
            }



            UI.getCurrent().access(() -> {
                loadingInfoContainer.setVisible(false);
                barcodeRequestView.getCreateSampleButton().setEnabled(true);
            });

            UI.getCurrent().setPollInterval(-1);

            UI.getCurrent().getSession().unlock();
        }


    }
}