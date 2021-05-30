package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClientsBill {

    SimpleStringProperty clientName;
    SimpleIntegerProperty allbills;
    SimpleIntegerProperty highestbill;

    public ClientsBill(String clientName, Integer allbills, Integer highestbill) {
        this.clientName = new SimpleStringProperty(clientName);
        this.allbills = new SimpleIntegerProperty(allbills);
        this.highestbill = new SimpleIntegerProperty(highestbill);
    }

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public int getAllbills() {
        return allbills.get();
    }

    public SimpleIntegerProperty allbillsProperty() {
        return allbills;
    }

    public void setAllbills(int allbills) {
        this.allbills.set(allbills);
    }

    public int getHighestbill() {
        return highestbill.get();
    }

    public SimpleIntegerProperty highestbillProperty() {
        return highestbill;
    }

    public void setHighestbill(int highestbill) {
        this.highestbill.set(highestbill);
    }
}
