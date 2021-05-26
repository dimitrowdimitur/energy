package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import sample.enums.ClientType;
import sample.model.Client;
import sample.model.Employee;
import sample.model.Property;
import sample.utils.EditUtil;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static sample.dbconnection.DBConnection.establishConnection;

public class Controller implements Initializable {

    @FXML
    private TableView<Employee> employTable;
    @FXML
    private TableColumn<Employee, String> name;
    @FXML
    private TableColumn<Employee, Integer> salary;
    @FXML
    private TextField employeeFullName;
    @FXML
    private TextField employeeSalary;

    @FXML
    private TableView<Property> propertyTable;
    @FXML
    private TableColumn<Property, String> address;
    @FXML
    private TableColumn<Property, Integer> maintenance;
    @FXML
    private TextField addressField;
    @FXML
    private TextField maintenanceField;


    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> clientFullName;
    @FXML
    private TableColumn<Client, String> clientType;
    @FXML
    private TextField clientField;
    @FXML
    private ChoiceBox typeDropdown;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        loadPropertyData();
        loadClientData();

        name.setCellFactory(TextFieldTableCell.forTableColumn());
        salary.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        salary.setOnEditCommit(
                event -> {
                    ((Employee) event.getTableView().getItems().get(event.getTablePosition().getRow())).setSalary(event.getNewValue());
                    int newSalary = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateSalary(newSalary, uniqueIdentifier);
                    employTable.setEditable(false);
                    employTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                }
        );
        name.setOnEditCommit(
                event -> {
                    ((Employee) event.getTableView().getItems().get(event.getTablePosition().getRow())).setFullName(event.getNewValue());
                    String newName = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateName(newName, uniqueIdentifier);
                    employTable.setEditable(false);
                    employTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                }
        );


        address.setCellFactory(TextFieldTableCell.forTableColumn());
        maintenance.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        maintenance.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaintenance(event.getNewValue());
                    int newMaintenance = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateMaintenance(newMaintenance, uniqueIdentifier);
                    propertyTable.setEditable(false);
                    propertyTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                }
        );
        address.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setAddress(event.getNewValue());
                    String newAddress = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateAddress(newAddress, uniqueIdentifier);
                    propertyTable.setEditable(false);
                    propertyTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                }
        );

        clientFullName.setCellFactory(TextFieldTableCell.forTableColumn());
        clientType.setCellFactory(
                new Callback<TableColumn<Client, String>, TableCell<Client, String>>() {
                    @Override
                    public TableCell<Client, String> call(TableColumn<Client, String> param) {
                        ObservableList<String> testlist = FXCollections
                                .observableArrayList(ClientType.FIRM.toString(), ClientType.PRIVATE.toString());
                        return new ChoiceBoxTableCell(testlist);
                    }
                });
        clientType.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setClientType(event.getNewValue());
                    String newClientType = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateClientType(newClientType, uniqueIdentifier);
                    clientTable.setEditable(false);
                    clientTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                }
        );
        clientFullName.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setClientFullName(event.getNewValue());
                    String newClientFullName = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateClientFullName(newClientFullName, uniqueIdentifier);
                    clientTable.setEditable(false);
                    clientTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                }
        );

        typeDropdown.setItems(FXCollections.observableArrayList(Arrays.asList(ClientType.values())));
    }

    private void updateClientFullName(String newClientFullName, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE clients SET fullname = ? WHERE id = ?");
            ps.setString(1,newClientFullName);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateClientType(String newClientType, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE clients SET type = ? WHERE id = ?");
            ps.setString(1,newClientType);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateMaintenance(int newMaintenance, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE property SET maintenance = ? WHERE id = ?");
            ps.setInt(1,newMaintenance);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateAddress(String newAddress, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE property SET address = ? WHERE id = ?");
            ps.setString(1,newAddress);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void onAddEmployee(){
        System.out.println("Adding employee " + employeeSalary.getText());
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();


            ps.executeUpdate("INSERT INTO employee (fullname, salary) VALUES('"
                    +employeeFullName.getText()+"', '"+ Integer.parseInt(employeeSalary.getText()) +"')");

            loadData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void onDeleteEmployee(){
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();


            ps.executeUpdate("DELETE FROM employee WHERE id='"
                    +employTable.getSelectionModel().getSelectedItem().getId() +"'");

            loadData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void onEditEmployee(){
        EditUtil.onEditProperty(employTable);
    }



    public void loadData() {
        name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM employee");
            ResultSet rs = ps.executeQuery();


            List<Employee> employeeList = new ArrayList<>();
            while (rs.next()) {
                Employee employee = new Employee(rs.getString("fullname"), rs.getInt("salary"),
                        rs.getInt("id"));
                employeeList.add(employee);
            }


            ObservableList<Employee> data = FXCollections.observableArrayList(employeeList);
            employTable.setItems(data);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadPropertyData() {
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        maintenance.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM property");
            ResultSet rs = ps.executeQuery();


            List<Property> propertyList = new ArrayList<>();
            while (rs.next()) {
                Property property = new Property(rs.getString("address"), rs.getInt("maintenance"),
                        rs.getInt("id"));
                propertyList.add(property);
            }


            ObservableList<Property> data = FXCollections.observableArrayList(propertyList);
            propertyTable.setItems(data);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateName(String newName, int id) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE employee SET fullname = ? WHERE id = ?");
            ps.setString(1,newName);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateSalary(int newSalary, int id) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE employee SET salary = ? WHERE id = ?");
            ps.setInt(1,newSalary);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onAddProperty(ActionEvent actionEvent) {
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();


            ps.executeUpdate("INSERT INTO property (address, maintenance) VALUES('"
                    +addressField.getText()+"', '"+ Integer.parseInt(maintenanceField.getText()) +"')");

            loadPropertyData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onDeleteProperty(ActionEvent actionEvent) {
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();

            ps.executeUpdate("DELETE FROM property WHERE id='"
                    +propertyTable.getSelectionModel().getSelectedItem().getId() +"'");

            loadPropertyData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onEditProperty(ActionEvent actionEvent) {
        EditUtil.onEditProperty(propertyTable);
    }


    public void loadClientData() {
        clientFullName.setCellValueFactory(new PropertyValueFactory<>("clientFullName"));
        clientType.setCellValueFactory(new PropertyValueFactory<>("clientType"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clients");
            ResultSet rs = ps.executeQuery();


            List<Client> clientList = new ArrayList<>();
            while (rs.next()) {
                Client client = new Client(rs.getString("fullname"), rs.getString("type"),
                        rs.getInt("id"));
                clientList.add(client);
            }


            ObservableList<Client> data = FXCollections.observableArrayList(clientList);
            clientTable.setItems(data);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void onEditClient(ActionEvent actionEvent) {
        EditUtil.onEditProperty(clientTable);
    }

    public void onAddClient(ActionEvent actionEvent) {
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();


            ps.executeUpdate("INSERT INTO clients (fullname, type) VALUES('"
                    +clientField.getText()+"', '"+ typeDropdown.getSelectionModel().getSelectedItem().toString() +"')");

            loadClientData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onDeleteClient(ActionEvent actionEvent) {
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();

            ps.executeUpdate("DELETE FROM clients WHERE id='"
                    +clientTable.getSelectionModel().getSelectedItem().getId() +"'");

            loadClientData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
