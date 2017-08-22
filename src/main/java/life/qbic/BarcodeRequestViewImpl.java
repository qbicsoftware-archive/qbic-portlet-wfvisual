package life.qbic;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * View that will display different barcode request cases for
 * the UKT diagnostics scenario:
 *
 *  1) Request new patientID/extractID pair
 *     Use case: A new patient will be added to CentraXX and a new
 *     ID tuple is needed.
 *
 *  2) Request new extractID for already existing patientID
 *     Use case: Repeated NGS run on the same tissue.
 *
 */
public class BarcodeRequestViewImpl implements BarcodeRequestView{

    private OptionGroup taskSelection;

    private VerticalLayout fullView;

    private Button patientIdsampleIdButton;

    private Panel patientIdField;

    private Panel sampleIdPanel;

    private Label patientIdLabel;

    private Label sampleIdLabel;

    public BarcodeRequestViewImpl(){
        initView();
    }

    private void initView(){
        // Init components
        patientIdsampleIdButton = new Button("Get new Patient/Sample ID pair");
        patientIdField = new Panel();
        sampleIdPanel = new Panel();
        fullView = new VerticalLayout();
        patientIdLabel = new Label("<i>ID will be displayed after request.</i>", ContentMode.HTML);
        sampleIdLabel = new Label("<i>ID will be displayed after request.</i>", ContentMode.HTML);
        HorizontalLayout panelContainer = new HorizontalLayout();

        // Add components
        panelContainer.addComponents(patientIdField, sampleIdPanel);
        fullView.addComponent(new Label("<h1>UKT diagnostics ID request sheet</h1>", ContentMode.HTML));
        fullView.addComponent(patientIdsampleIdButton);
        fullView.addComponent(panelContainer);
        fullView.setSpacing(true);


        patientIdField.setCaption("Patient ID");
        patientIdField.setWidth("300px");
        sampleIdPanel.setCaption("Sample ID");
        sampleIdPanel.setWidth("300px");
        panelContainer.setSpacing(true);

        VerticalLayout innerPatientIdLayout = new VerticalLayout();
        innerPatientIdLayout.addComponent(patientIdLabel);
        innerPatientIdLayout.setMargin(true);
        patientIdField.setContent(innerPatientIdLayout);


        VerticalLayout innerSampleIdLayout = new VerticalLayout();
        innerSampleIdLayout.addComponent(sampleIdLabel);
        innerSampleIdLayout.setMargin(true);
        sampleIdPanel.setContent(innerSampleIdLayout);


    }

    @Override
    public Panel getPatientIdField() {
        return this.patientIdField;
    }

    @Override
    public Panel getSampleIdField() {
        return null;
    }

    @Override
    public OptionGroup getTaskSelectionGroup() {
        return null;
    }

    @Override
    public VerticalLayout getFullView() {
        return fullView;
    }

    @Override
    public Button getPatentIdSampleIdButton() {
        return this.patientIdsampleIdButton;
    }
}
