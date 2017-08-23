package life.qbic;

import com.vaadin.ui.*;

public interface BarcodeRequestView {

    /**
     * Getter for the patientID text field
     * @return text field
     */
    Label getPatientIdField();

    /**
     * Getter for the sampleID text field
     * @return text field
     */
    Label getSampleIdField();


    /**
     * Getter for the radio button group
     * @return text field
     */
    OptionGroup getTaskSelectionGroup();


    /**
     * Getter for the total view elements
     * container
     * @return vertical layout
     */
    VerticalLayout getFullView();


    /**
     * Getter for the patient Id / sample Id
     * request button
     * @return the button instance
     */
    Button getPatentIdSampleIdButton();



}
