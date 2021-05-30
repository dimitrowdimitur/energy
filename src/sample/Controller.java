package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import sample.model.*;
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

    @FXML
    private TableView<Tariff> tariffTable;
    @FXML
    private TableColumn<Tariff, Integer> price;
    @FXML
    private TableColumn<Tariff, String> priceType;

    @FXML
    private TableView<Consumption> consumptionTable;
    @FXML
    private TableColumn<Consumption, String> clientConsumptionName;
    @FXML
    private TableColumn<Consumption, Integer> consumption;
    @FXML
    private TextField consumptionField;
    @FXML
    private ChoiceBox clientDropdown;


    @FXML
    private TableView<Saldo> saldoTable;
    @FXML
    private TableColumn<Saldo, Integer> income;
    @FXML
    private TableColumn<Saldo, Integer> expense;
    @FXML
    private TableColumn<Saldo, Integer> profit;
    @FXML
    private TableColumn<Saldo, Double> profittaxed;

    @FXML
    private TableView<ClientsBill> clientsBillTable;
    @FXML
    private TableColumn<ClientsBill, String> clientname;
    @FXML
    private TableColumn<ClientsBill, Integer> allbills;
    @FXML
    private TableColumn<ClientsBill, Integer> highestbill;


    @FXML
    private TableView<BillUnder> billUnderTable;
    @FXML
    private TableColumn<BillUnder, String> billunderclientname;
    @FXML
    private TextField billundersearch;

    private ObservableList<Consumption> consumptionObservableList;
    private ObservableList<Client> clientObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        loadPropertyData();
        loadClientData();
        loadTariffData();
        loadConsumptionData();
        loadSaldoData();
        loadClientsBillData();

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
                    loadSaldoData();
                }
        );
        employeeSalary.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    employeeSalary.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        name.setOnEditCommit(
                event -> {
                    ((Employee) event.getTableView().getItems().get(event.getTablePosition().getRow())).setFullName(event.getNewValue());
                    String newName = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateName(newName, uniqueIdentifier);
                    employTable.setEditable(false);
                    employTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                    loadSaldoData();
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
                    loadSaldoData();
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
                    loadSaldoData();
                }
        );
        maintenanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    maintenanceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

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
                    loadSaldoData();
                    loadClientsBillData();
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
                    clientDropdown.setItems(FXCollections.observableArrayList(getCustomersNamesList()));
                    loadSaldoData();
                    loadClientsBillData();
                }
        );

        price.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceType.setCellFactory(
                param -> {
                    ObservableList<String> priceTypeList = FXCollections
                            .observableArrayList(ClientType.FIRM.toString(), ClientType.PRIVATE.toString());
                    return new ChoiceBoxTableCell(priceTypeList);
                });
        priceType.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setPriceType(event.getNewValue());
                    String newTariffType = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updatePriceType(newTariffType, uniqueIdentifier);
                    tariffTable.setEditable(false);
                    tariffTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                    loadSaldoData();
                    loadClientsBillData();
                }
        );
        price.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setPrice(event.getNewValue());
                    int newPrice = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updatePrice(newPrice, uniqueIdentifier);
                    tariffTable.setEditable(false);
                    tariffTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                    loadSaldoData();
                    loadClientsBillData();
                }
        );

        consumption.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        clientConsumptionName.setCellFactory(
                param -> new ChoiceBoxTableCell(FXCollections
                        .observableArrayList(getCustomersNamesList())));
        clientConsumptionName.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setClientName(event.getNewValue());
                    String newClientConsumptionName = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateClientConsumptionName(newClientConsumptionName, uniqueIdentifier);
                    consumptionTable.setEditable(false);
                    consumptionTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                    loadSaldoData();
                    loadClientsBillData();
                }
        );
        consumption.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setConsumption(event.getNewValue());
                    int newConsumption = event.getNewValue();
                    int uniqueIdentifier = event.getRowValue().getId();
                    updateConsumption(newConsumption, uniqueIdentifier);
                    consumptionTable.setEditable(false);
                    consumptionTable.getSelectionModel().cellSelectionEnabledProperty().set(false);
                    loadSaldoData();
                    loadClientsBillData();
                }
        );

        consumptionField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    consumptionField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        billundersearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    billundersearch.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        clientDropdown.setItems(FXCollections.observableArrayList(getCustomersNamesList()));
        typeDropdown.setItems(FXCollections.observableArrayList(Arrays.asList(ClientType.values())));
    }

    private void updateConsumption(int newConsumption, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE consumption SET consumption = ? WHERE id = ?");
            ps.setInt(1,newConsumption);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateClientConsumptionName(String newClientConsumptionName, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clients\n" +
                    "WHERE fullname='"
                    +newClientConsumptionName +"'");
            ResultSet rs = ps.executeQuery();


            List<Integer> clients = new ArrayList<>();
            while (rs.next()) {
                int clientid = rs.getInt("id");
                clients.add(clientid);
            }


            ps = connection.prepareStatement("UPDATE consumption SET clientid = ? WHERE id = ?");
            ps.setInt(1,clients.get(0));
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updatePrice(int newPrice, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE tariff SET price = ? WHERE id = ?");
            ps.setInt(1,newPrice);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updatePriceType(String newTariffType, int uniqueIdentifier) {
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE tariff SET type = ? WHERE id = ?");
            ps.setString(1,newTariffType);
            ps.setInt(2, uniqueIdentifier);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
            loadSaldoData();

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
            loadSaldoData();
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


            clientObservableList = FXCollections.observableArrayList(clientList);
            clientTable.setItems(clientObservableList);
            loadSaldoData();
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
            loadClientsBillData();
            clientDropdown.setItems(FXCollections.observableArrayList(getCustomersNamesList()));

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
            clientDropdown.setItems(FXCollections.observableArrayList(getCustomersNamesList()));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onEditTariff(ActionEvent actionEvent) {
        EditUtil.onEditProperty(tariffTable );
    }

    public void loadTariffData() {
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceType.setCellValueFactory(new PropertyValueFactory<>("priceType"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tariff");
            ResultSet rs = ps.executeQuery();


            List<Tariff> tariffList = new ArrayList<>();
            while (rs.next()) {
                Tariff tariff = new Tariff(rs.getString("type"), rs.getInt("price"),
                        rs.getInt("id"));
                tariffList.add(tariff);
            }


            ObservableList<Tariff> data = FXCollections.observableArrayList(tariffList);
            tariffTable.setItems(data);
            loadSaldoData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onAddClientConsumption(ActionEvent actionEvent) {
        try {
            Connection connection =  establishConnection();


            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clients\n" +
                    "WHERE fullname='"
                    + clientDropdown.getSelectionModel().getSelectedItem().toString() +"'");
            ResultSet rs = ps.executeQuery();


            List<Integer> clients = new ArrayList<>();
            while (rs.next()) {
                int clientid = rs.getInt("id");
                clients.add(clientid);
            }


            ps.executeUpdate("INSERT INTO consumption (clientid, consumption) VALUES('"
                    +clients.get(0)+"', '"+ Integer.parseInt(consumptionField.getText()) +"')");

            loadConsumptionData();
            loadClientsBillData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onDeleteClientConsumption(ActionEvent actionEvent) {
        try {
            Connection connection =  establishConnection();
            Statement ps = connection.createStatement();

            ps.executeUpdate("DELETE FROM consumption WHERE id='"
                    +consumptionTable.getSelectionModel().getSelectedItem().getId() +"'");

            loadConsumptionData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadConsumptionData() {
        consumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        clientConsumptionName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT consumption.id, consumption.consumption, consumption.clientid, c.fullname FROM energy.consumption\n" +
                    "JOIN energy.clients c ON c.id = consumption.clientid;");
            ResultSet rs = ps.executeQuery();


            List<Consumption> consumptionList = new ArrayList<>();
            while (rs.next()) {
                Consumption consumption = new Consumption(rs.getInt("clientid"), rs.getInt("consumption"),
                        rs.getInt("id"), rs.getString("fullname"));
                consumptionList.add(consumption);
            }


            consumptionObservableList = FXCollections.observableArrayList(consumptionList);
            consumptionTable.setItems(consumptionObservableList);
            loadSaldoData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onEditConsumption(ActionEvent actionEvent) {
        EditUtil.onEditProperty(consumptionTable);
    }

    private List<String> getCustomersNamesList(){
        List<String> names = new ArrayList<>();
        for (Client client:
                clientObservableList) {
            names.add(client.getClientFullName());
        }
        return names;
    }

    public void loadSaldoData() {
        income.setCellValueFactory(new PropertyValueFactory<>("income"));
        expense.setCellValueFactory(new PropertyValueFactory<>("expense"));
        profit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        profittaxed.setCellValueFactory(new PropertyValueFactory<>("profittaxed"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT SUM(salary) as salarysum FROM employee");
            ResultSet rs = ps.executeQuery();

            List<Integer> salarySum = new ArrayList<>();
            while (rs.next()) {
                int salary = rs.getInt("salarysum");
                salarySum.add(salary);
            }

            ps = connection.prepareStatement("SELECT SUM(maintenance) as maintenancesum FROM property");
            rs = ps.executeQuery();

            List<Integer> maintenancesum = new ArrayList<>();
            while (rs.next()) {
                int maintenance = rs.getInt("maintenancesum");
                maintenancesum.add(maintenance);
            }

            ps = connection.prepareStatement("SELECT SUM(consumption.consumption) as consumptionsum FROM consumption " +
                    "JOIN energy.clients c ON c.id = consumption.clientid " +
                    "WHERE c.type ='"
                    + "PRIVATE" +"'");
            rs = ps.executeQuery();

            List<Integer> consumptionPrivate = new ArrayList<>();
            while (rs.next()) {
                int consumption = rs.getInt("consumptionsum");
                consumptionPrivate.add(consumption);
            }

            ps = connection.prepareStatement("SELECT SUM(consumption.consumption) as consumptionsum FROM consumption " +
                    "JOIN energy.clients c ON c.id = consumption.clientid " +
                    "WHERE c.type='"
                    + "FIRM" +"'");
            rs = ps.executeQuery();

            List<Integer> consumptionFirm = new ArrayList<>();
            while (rs.next()) {
                int consumption = rs.getInt("consumptionsum");
                consumptionFirm.add(consumption);
            }

            ps = connection.prepareStatement("SELECT price FROM tariff WHERE type ='"
                    + "PRIVATE" +"'");
            rs = ps.executeQuery();

            List<Integer> privateTariffPrice = new ArrayList<>();
            while (rs.next()) {
                int price = rs.getInt("price");
                privateTariffPrice.add(price);
            }

            ps = connection.prepareStatement("SELECT price FROM tariff WHERE type ='"
                    + "FIRM" +"'");
            rs = ps.executeQuery();

            List<Integer> firmTariffPrice = new ArrayList<>();
            while (rs.next()) {
                int price = rs.getInt("price");
                firmTariffPrice.add(price);
            }

            int income = 0;

            if (privateTariffPrice.get(0) != null && consumptionPrivate.get(0) != null) {
                income = consumptionPrivate.get(0) * privateTariffPrice.get(0);
            }
            if (firmTariffPrice.get(0) != null && consumptionFirm.get(0) != null) {
                income += firmTariffPrice.get(0) * consumptionFirm.get(0);
            }

            int expense = 0;

            if (salarySum.get(0) != null) {
                expense += salarySum.get(0);
            }
            if (maintenancesum.get(0) != null) {
                expense += maintenancesum.get(0);
            }

            int profit = income - expense;
            double profittaxed = profit - profit*0.1;

            Saldo saldo = new Saldo(income, expense, profit,  profittaxed);

            List<Saldo> saldoList = new ArrayList<>();
            saldoList.add(saldo);

            ObservableList<Saldo> data = FXCollections.observableArrayList(saldoList);
            saldoTable.setItems(data);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadClientsBillData() {
        clientname.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        allbills.setCellValueFactory(new PropertyValueFactory<>("allbills"));
        highestbill.setCellValueFactory(new PropertyValueFactory<>("highestbill"));
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
            List<ClientsBill> clientsBillList = new ArrayList<>();
            for (Client client: clientList) {
                ps = connection.prepareStatement("SELECT consumption FROM energy.consumption\n" +
                        " WHERE clientid='"
                        +client.getId() +"'");
                rs = ps.executeQuery();

                ClientsBill clientsBill = new ClientsBill(client.getClientFullName(), 0,
                        0);
                int totalSum = 0;
                int highestSum = 0;
                while (rs.next()) {
                   totalSum +=  rs.getInt("consumption");
                    if (rs.getInt("consumption") > highestSum ) {
                        highestSum = rs.getInt("consumption");
                    }
                }

                ps = connection.prepareStatement("SELECT price FROM tariff WHERE type ='"
                        + client.getClientType() +"'");
                rs = ps.executeQuery();

                List<Integer> tariffPrice = new ArrayList<>();
                while (rs.next()) {
                    int price = rs.getInt("price");
                    tariffPrice.add(price);
                }

                totalSum = totalSum * tariffPrice.get(0);
                highestSum = highestSum * tariffPrice.get(0);

                clientsBill.setAllbills(totalSum);
                clientsBill.setHighestbill(highestSum);
                clientsBillList.add(clientsBill);
            }

            ObservableList<ClientsBill> data = FXCollections.observableArrayList(clientsBillList);
            clientsBillTable.setItems(data);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onSearchBillUnder(ActionEvent actionEvent) {
        billunderclientname.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        try {
            Connection connection =  establishConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT c.fullname FROM energy.consumption" +
                    " JOIN energy.clients c ON c.id = consumption.clientid" +
                    " WHERE consumption < '"+ Integer.parseInt(billundersearch.getText()) +"'");
            ResultSet rs = ps.executeQuery();


            List<BillUnder> clientList = new ArrayList<>();
            while (rs.next()) {
                BillUnder client = new BillUnder(rs.getString("fullname"));
                clientList.add(client);
            }


            ObservableList<BillUnder> data  = FXCollections.observableArrayList(clientList);
            billUnderTable.setItems(data);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
