package life.qbic;

import com.vaadin.client.ui.Icon;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * View that will display different barcode request cases for
 * the UKT diagnostics scenario:
 *
 *  1) Request new patientID/sampleID pair
 *     Use case: A new patient will be added to CentraXX and a new
 *     ID tuple is needed.
 *
 *  2) Request new sampleID for already existing patientID
 *     Use case: Repeated NGS run on the same tissue.
 *
 */
public class BarcodeRequestViewImpl implements BarcodeRequestView {

    private OptionGroup taskSelection;

    private VerticalLayout fullView;

    private Button patientIdsampleIdButton;

    private Button createSampleForPatientButton;

    private Panel patientIdField;

    private Panel sampleIdPanel;

    private Label patientIdLabel;

    private Label sampleIdLabel;

    private ProgressBar spinner;

    private Label loadingLabel;

    private HorizontalLayout spinnerContainer;

    private VerticalLayout taskCreatePatientContainer;

    private VerticalLayout taskCreateSampleContainer;

    private ComboBox patientIdInputField;

    private Label newSampleIdLabel;

    private Panel newPatientIdPanel;

    private Panel newSampleIdPanel;

    public BarcodeRequestViewImpl() {
        initView();
    }

    private void initView() {
        // Init components
        createTaskSelectionView();
        patientIdInputField = new ComboBox();
        patientIdInputField.setValue("Enter patient ID here");
        newSampleIdLabel = new Label("<i>ID will be displayed after request.</i>", ContentMode.HTML);
        newPatientIdPanel = new Panel("Patient ID");
        newSampleIdPanel = new Panel("Sample ID");
        spinner = new ProgressBar();
        loadingLabel = new Label();
        taskCreatePatientContainer = new VerticalLayout();
        taskCreateSampleContainer = new VerticalLayout();

        spinnerContainer = new HorizontalLayout();
        spinnerContainer.addComponents(spinner, loadingLabel);
        spinnerContainer.setSpacing(true);

        patientIdsampleIdButton = new Button("Create new Patient/Sample ID pair");
        createSampleForPatientButton = new Button("Create new Sample ID for patient");
        patientIdField = new Panel();
        sampleIdPanel = new Panel();
        fullView = new VerticalLayout();
        patientIdLabel = new Label("<i>ID will be displayed after request.</i>", ContentMode.HTML);
        sampleIdLabel = new Label("<i>ID will be displayed after request.</i>", ContentMode.HTML);
        HorizontalLayout panelContainer = new HorizontalLayout();
        HorizontalLayout panelContainerTask2 = new HorizontalLayout();

        // Add components
        panelContainer.addComponents(patientIdField, sampleIdPanel);
        panelContainerTask2.addComponents(newPatientIdPanel, newSampleIdPanel);
        fullView.addComponent(new Label("<h1>UKT diagnostics ID request sheet</h1>", ContentMode.HTML));

        // Compose new patient request layout form
        taskCreatePatientContainer.addComponents(patientIdsampleIdButton, panelContainer);
        taskCreatePatientContainer.setSpacing(true);
        taskCreatePatientContainer.setVisible(false);

        // Compose new sample request layout form;
        taskCreateSampleContainer.addComponents(createSampleForPatientButton, panelContainerTask2);
        taskCreateSampleContainer.setSpacing(true);
        taskCreateSampleContainer.setVisible(false);

        // Compose new sample request layout form
        fullView.addComponents(taskSelection, taskCreatePatientContainer, taskCreateSampleContainer, spinnerContainer);
        fullView.setSpacing(true);

        // we want a spinner not a progress bar
        spinner.setIndeterminate(true);
        spinner.setImmediate(true);
        spinnerContainer.setVisible(false);

        patientIdField.setCaption("Patient ID");
        patientIdField.setWidth("300px");
        sampleIdPanel.setCaption("Sample ID");
        sampleIdPanel.setWidth("300px");
        panelContainer.setSpacing(true);

        newPatientIdPanel.setWidth("300px");
        newSampleIdPanel.setWidth("300px");
        panelContainerTask2.setSpacing(true);

        VerticalLayout innerPatientIdLayout = new VerticalLayout();
        innerPatientIdLayout.addComponent(patientIdLabel);
        innerPatientIdLayout.setMargin(true);
        patientIdField.setContent(innerPatientIdLayout);
        patientIdField.setIcon(FontAwesome.USER);


        VerticalLayout innerSampleIdLayout = new VerticalLayout();
        innerSampleIdLayout.addComponent(sampleIdLabel);
        innerSampleIdLayout.setMargin(true);
        sampleIdPanel.setContent(innerSampleIdLayout);
        sampleIdPanel.setIcon(FontAwesome.FILE_O);

        VerticalLayout innerPatientIdLayoutTask2 = new VerticalLayout();
        innerPatientIdLayoutTask2.addComponent(patientIdInputField);
        innerPatientIdLayoutTask2.setMargin(true);
        newPatientIdPanel.setContent(innerPatientIdLayoutTask2);
        newPatientIdPanel.setIcon(FontAwesome.USER);

        VerticalLayout innerSampleIdLayoutTask2 = new VerticalLayout();
        innerSampleIdLayoutTask2.addComponent(newSampleIdLabel);
        innerSampleIdLayoutTask2.setMargin(true);
        innerSampleIdLayoutTask2.setComponentAlignment(newSampleIdLabel, Alignment.MIDDLE_LEFT);
        innerPatientIdLayoutTask2.setHeight(100, Sizeable.Unit.PERCENTAGE);
        newSampleIdPanel.setContent(innerSampleIdLayoutTask2);
        newSampleIdPanel.setHeight(100, Sizeable.Unit.PERCENTAGE);
        newSampleIdPanel.setIcon(FontAwesome.FILE_O);

    }

    private void createTaskSelectionView() {
        taskSelection = new OptionGroup("Choose what you want to do");
        taskSelection.addItems("Request new patient/sample ID pair (Creates new patient ID!)",
                "Create new DNA sample for an existing patient");
    }

    @Override
    public Label getPatientIdField() {
        return this.patientIdLabel;
    }

    @Override
    public Label getSampleIdField() {
        return this.sampleIdLabel;
    }

    @Override
    public OptionGroup getTaskSelectionGroup() {
        return this.taskSelection;
    }

    @Override
    public VerticalLayout getFullView() {
        return fullView;
    }

    @Override
    public Button getPatentIdSampleIdButton() {
        return this.patientIdsampleIdButton;
    }

    @Override
    public ProgressBar getSpinner() {
        return this.spinner;
    }

    @Override
    public Label getLoadingLabel() {
        return this.loadingLabel;
    }

    @Override
    public HorizontalLayout getSpinnerContainer() {
        return this.spinnerContainer;
    }

    @Override
    public VerticalLayout getCreatePatientContainer() {
        return this.taskCreatePatientContainer;
    }

    @Override
    public VerticalLayout getCreateSampleContainer() {
        return this.taskCreateSampleContainer;
    }

    @Override
    public Button getCreateSampleButton() {
        return this.createSampleForPatientButton;
    }

    @Override
    public ComboBox getPatientIdInputField() {
        return this.patientIdInputField;
    }

    @Override
    public Label getNewSampleIdField() {
        return this.newSampleIdLabel;
    }

}
