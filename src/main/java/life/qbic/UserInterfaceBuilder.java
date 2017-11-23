package life.qbic;

import com.vaadin.ui.VerticalLayout;

import life.qbic.UserInterface;

class UserInterfaceBuilder {

    public UserInterfaceBuilder(){
    }

    /**
     * Build and return the main user interface
     * @return A VerticalLayout object containing all UI elements
     */
    public VerticalLayout build(){
        return new UserInterfaceImpl().build();
    };
    
}