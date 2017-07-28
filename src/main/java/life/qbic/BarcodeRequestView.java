package life.qbic;

import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public interface BarcodeRequestView {

    /**
     * Getter for the patientID text field
     * @return text field
     */
    TextField getPatientIdField();

    /**
     * Getter for the sampleID text field
     * @return text field
     */
    TextField getSampleIdField();


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



}
