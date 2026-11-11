package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.CustomerDAO;
import com.cafe.cafeshopmanagement.dao.UserDAO;
import com.cafe.cafeshopmanagement.model.Customer;
import com.cafe.cafeshopmanagement.model.Role;
import com.cafe.cafeshopmanagement.model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserCustomerManagementController {

    // ================= TAB NHÂN VIÊN =================
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUsername;

    // Đã đổi String thành Role ở cột này để TableView hiển thị đúng
    @FXML private TableColumn<User, Role> colRole;

    @FXML private TableColumn<User, String> colUserFullName;
    @FXML private TableColumn<User, String> colUserPhone;
    @FXML private TableColumn<User, Integer> colSalary;
    @FXML private TableColumn<User, Boolean> colUserStatus;

    @FXML private TextField txtUsername, txtPassword, txtUserFullName, txtUserPhone, txtSalary;
    @FXML private ComboBox<String> cbRole;
    @FXML private CheckBox chkUserActive;
    @FXML private Label lblUserMsg;

    // ================= TAB KHÁCH HÀNG =================
    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, Integer> colCustomerId;
    @FXML private TableColumn<Customer, String> colCusPhone;
    @FXML private TableColumn<Customer, String> colCusFullName;
    @FXML private TableColumn<Customer, Integer> colPoints;

    @FXML private TextField txtCusPhone, txtCusFullName, txtPoints;
    @FXML private Label lblCusMsg;

    private UserDAO userDAO = new UserDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    public void initialize() {
        // Cấu hình cột Users
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colUserStatus.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Ẩn cột ID để tăng tính thẩm mỹ (vẫn giữ dữ liệu ngầm để Sửa/Xóa)
        colUserId.setVisible(false);

        // Cấu hình cột Customers
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCusPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCusFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));

        // ComboBox Quyền
        cbRole.setItems(FXCollections.observableArrayList("ADMIN", "STAFF"));
        cbRole.getSelectionModel().selectFirst();

        loadUserList();
        loadCustomerList();

        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillUserForm(newSel);
        });
        tableCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillCustomerForm(newSel);
        });
    }

    private void loadUserList() {
        // ĐÃ SỬA: Dùng getAllUsers() để lấy cả tài khoản bị khóa thay vì getActiveUsers()
        tableUsers.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
    }

    private void fillUserForm(User u) {
        txtUsername.setText(u.getUsername());
        txtPassword.setText(u.getPasswordHash());
        // Trích xuất String (tên quyền) từ đối tượng Role để gán vào ComboBox
        if (u.getRole() != null) {
            cbRole.setValue(u.getRole().getRoleName());
        }
        txtUserFullName.setText(u.getFullName());
        txtUserPhone.setText(u.getPhone());
        txtSalary.setText(String.valueOf(u.getSalary()));
        chkUserActive.setSelected(u.isActive());
    }

    @FXML void handleAddUser(ActionEvent event) {
        try {
            // Sử dụng constructor rỗng và setter để tránh lỗi truyền nhầm kiểu dữ liệu
            User u = new User();
            u.setUsername(txtUsername.getText().trim());
            u.setPasswordHash(txtPassword.getText().trim());
            u.setRoleFromString(cbRole.getValue()); // Tự động chuyển chuỗi thành đối tượng Role
            u.setFullName(txtUserFullName.getText().trim());
            u.setPhone(txtUserPhone.getText().trim());
            u.setSalary(Integer.parseInt(txtSalary.getText().trim()));
            u.setActive(chkUserActive.isSelected());

            if (userDAO.addUser(u)) {
                loadUserList();
                handleClearUser(null);
                lblUserMsg.setText("Thêm thành công!");
            }
        } catch (Exception e) {
            lblUserMsg.setText("Lỗi nhập liệu!");
            e.printStackTrace();
        }
    }

    @FXML void handleUpdateUser(ActionEvent event) {
        User u = tableUsers.getSelectionModel().getSelectedItem();
        if (u == null) return;
        try {
            u.setUsername(txtUsername.getText().trim());
            u.setPasswordHash(txtPassword.getText().trim());
            u.setRoleFromString(cbRole.getValue()); // Tự động chuyển chuỗi thành đối tượng Role
            u.setFullName(txtUserFullName.getText().trim());
            u.setPhone(txtUserPhone.getText().trim());
            u.setSalary(Integer.parseInt(txtSalary.getText().trim()));
            u.setActive(chkUserActive.isSelected());

            if (userDAO.updateUser(u)) {
                loadUserList();
                lblUserMsg.setText("Cập nhật thành công!");
            }
        } catch (Exception e) {
            lblUserMsg.setText("Lỗi nhập liệu!");
            e.printStackTrace();
        }
    }

    @FXML void handleDeleteUser(ActionEvent event) {
        User u = tableUsers.getSelectionModel().getSelectedItem();
        if (u != null && userDAO.deleteUser(u.getUserId())) {
            loadUserList();
            handleClearUser(null);
        }
    }

    @FXML void handleClearUser(ActionEvent event) {
        txtUsername.clear(); txtPassword.clear(); txtUserFullName.clear(); txtUserPhone.clear(); txtSalary.clear();
        chkUserActive.setSelected(true); cbRole.getSelectionModel().selectFirst(); lblUserMsg.setText("");
    }

    // ================= XỬ LÝ KHÁCH HÀNG =================
    private void loadCustomerList() {
        tableCustomers.setItems(FXCollections.observableArrayList(customerDAO.getAllCustomers()));
    }

    private void fillCustomerForm(Customer c) {
        txtCusPhone.setText(c.getPhone());
        txtCusFullName.setText(c.getFullName());
        txtPoints.setText(String.valueOf(c.getPoints()));
    }

    @FXML void handleAddCustomer(ActionEvent event) {
        try {
            Customer c = new Customer(0, txtCusPhone.getText().trim(), txtCusFullName.getText().trim(),
                    Integer.parseInt(txtPoints.getText().trim()));
            if (customerDAO.addCustomer(c)) {
                loadCustomerList();
                handleClearCustomer(null);
                lblCusMsg.setText("Thêm thành công!");
            }
        } catch (Exception e) { lblCusMsg.setText("Lỗi nhập liệu!"); }
    }

    @FXML void handleUpdateCustomer(ActionEvent event) {
        Customer c = tableCustomers.getSelectionModel().getSelectedItem();
        if (c == null) return;
        try {
            c.setPhone(txtCusPhone.getText().trim());
            c.setFullName(txtCusFullName.getText().trim());
            c.setPoints(Integer.parseInt(txtPoints.getText().trim()));
            if (customerDAO.updateCustomer(c)) {
                loadCustomerList();
                lblCusMsg.setText("Cập nhật thành công!");
            }
        } catch (Exception e) { lblCusMsg.setText("Lỗi nhập liệu!"); }
    }

    @FXML void handleDeleteCustomer(ActionEvent event) {
        Customer c = tableCustomers.getSelectionModel().getSelectedItem();
        if (c != null && customerDAO.deleteCustomer(c.getCustomerId())) {
            loadCustomerList();
            handleClearCustomer(null);
        }
    }

    @FXML void handleClearCustomer(ActionEvent event) {
        txtCusPhone.clear(); txtCusFullName.clear(); txtPoints.clear(); lblCusMsg.setText("");
    }
}