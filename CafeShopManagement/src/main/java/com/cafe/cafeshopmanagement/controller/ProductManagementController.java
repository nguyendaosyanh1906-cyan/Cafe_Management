package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.ProductDAO;
import com.cafe.cafeshopmanagement.dao.ToppingDAO;
import com.cafe.cafeshopmanagement.model.Product;
import com.cafe.cafeshopmanagement.model.Topping;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;

public class ProductManagementController {

    // TAB SẢN PHẨM
    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, Integer> colProductId;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, Integer> colCategory;
    @FXML private TableColumn<Product, Integer> colPrice;
    @FXML private TableColumn<Product, String> colDescription;
    @FXML private TableColumn<Product, Boolean> colProductStatus;

    @FXML private TextField txtProductName, txtPrice, txtImagePath;
    @FXML private TextArea txtDescription;
    @FXML private ComboBox<String> cbCategory;
    @FXML private CheckBox chkProductActive;
    @FXML private Label lblProductMsg;

    // TAB TOPPING
    @FXML private TableView<Topping> tableToppings;
    @FXML private TableColumn<Topping, Integer> colToppingId;
    @FXML private TableColumn<Topping, String> colToppingName;
    @FXML private TableColumn<Topping, Integer> colToppingPrice;
    @FXML private TableColumn<Topping, Boolean> colToppingStatus;

    @FXML private TextField txtToppingName, txtToppingPrice;
    @FXML private CheckBox chkToppingActive;
    @FXML private Label lblToppingMsg;

    private ProductDAO productDAO = new ProductDAO();
    private ToppingDAO toppingDAO = new ToppingDAO();

    @FXML
    public void initialize() {
        // Cấu hình cột Sản phẩm
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductStatus.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Cấu hình cột Topping
        colToppingId.setCellValueFactory(new PropertyValueFactory<>("toppingId"));
        colToppingName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colToppingPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colToppingStatus.setCellValueFactory(new PropertyValueFactory<>("active"));

        cbCategory.setItems(FXCollections.observableArrayList("Cà phê", "Trà sữa", "Phindi", "Đồ ăn kèm"));
        cbCategory.getSelectionModel().selectFirst();

        loadProductList();
        loadToppingList();

        tableProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillProductForm(newSel);
        });

        tableToppings.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillToppingForm(newSel);
        });
    }

    private void loadProductList() {
        tableProducts.setItems(FXCollections.observableArrayList(productDAO.getActiveProducts()));
    }

    private void fillProductForm(Product p) {
        txtProductName.setText(p.getName());
        txtPrice.setText(String.valueOf(p.getBasePrice()));
        txtDescription.setText(p.getDescription());
        txtImagePath.setText(p.getImageUrl());
        chkProductActive.setSelected(p.isActive());

        if (p.getCategoryId() >= 1 && p.getCategoryId() <= cbCategory.getItems().size()) {
            cbCategory.getSelectionModel().select(p.getCategoryId() - 1);
        }
    }

    @FXML
    void handleBrowseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sản phẩm");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );
        File selectedFile = fileChooser.showOpenDialog(txtImagePath.getScene().getWindow());
        if (selectedFile != null) {
            txtImagePath.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML void handleAddProduct(ActionEvent event) {
        try {
            int categoryId = cbCategory.getSelectionModel().getSelectedIndex() + 1;
            Product p = new Product(0, categoryId, txtProductName.getText().trim(),
                    txtDescription.getText().trim(), txtImagePath.getText().trim(),
                    Integer.parseInt(txtPrice.getText().trim()), chkProductActive.isSelected());

            if (productDAO.addProduct(p)) {
                loadProductList();
                handleClearProduct(null);
                lblProductMsg.setText("Thêm sản phẩm thành công!");
            }
        } catch (Exception e) { lblProductMsg.setText("Lỗi nhập liệu!"); }
    }

    @FXML void handleUpdateProduct(ActionEvent event) {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) return;
        try {
            p.setCategoryId(cbCategory.getSelectionModel().getSelectedIndex() + 1);
            p.setName(txtProductName.getText().trim());
            p.setBasePrice(Integer.parseInt(txtPrice.getText().trim()));
            p.setDescription(txtDescription.getText().trim());
            p.setImageUrl(txtImagePath.getText().trim());
            p.setActive(chkProductActive.isSelected());

            if (productDAO.updateProduct(p)) {
                loadProductList();
                lblProductMsg.setText("Cập nhật thành công!");
            }
        } catch (Exception e) { lblProductMsg.setText("Lỗi nhập liệu!"); }
    }

    @FXML void handleDeleteProduct(ActionEvent event) {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p != null && productDAO.deleteProduct(p.getProductId())) {
            loadProductList();
            handleClearProduct(null);
        }
    }

    @FXML void handleClearProduct(ActionEvent event) {
        txtProductName.clear(); txtPrice.clear(); txtDescription.clear(); txtImagePath.clear();
        chkProductActive.setSelected(true); lblProductMsg.setText("");
    }

    // --- TOPPING HANDLERS ---
    private void loadToppingList() {
        tableToppings.setItems(FXCollections.observableArrayList(toppingDAO.getAllToppings()));
    }

    private void fillToppingForm(Topping t) {
        txtToppingName.setText(t.getName());
        txtToppingPrice.setText(String.valueOf(t.getPrice()));
        chkToppingActive.setSelected(t.isActive());
    }

    @FXML void handleAddTopping(ActionEvent event) {
        try {
            Topping t = new Topping(0, txtToppingName.getText().trim(),
                    Integer.parseInt(txtToppingPrice.getText().trim()), chkToppingActive.isSelected());
            if (toppingDAO.addTopping(t)) {
                loadToppingList();
                handleClearTopping(null);
                lblToppingMsg.setText("Thêm Topping thành công!");
            }
        } catch (Exception e) { lblToppingMsg.setText("Lỗi nhập liệu!"); }
    }

    @FXML void handleUpdateTopping(ActionEvent event) {
        Topping t = tableToppings.getSelectionModel().getSelectedItem();
        if (t == null) return;
        try {
            t.setName(txtToppingName.getText().trim());
            t.setPrice(Integer.parseInt(txtToppingPrice.getText().trim()));
            t.setActive(chkToppingActive.isSelected());
            if (toppingDAO.updateTopping(t)) {
                loadToppingList();
                lblToppingMsg.setText("Cập nhật thành công!");
            }
        } catch (Exception e) { lblToppingMsg.setText("Lỗi nhập liệu!"); }
    }

    @FXML void handleDeleteTopping(ActionEvent event) {
        Topping t = tableToppings.getSelectionModel().getSelectedItem();
        if (t != null && toppingDAO.deleteTopping(t.getToppingId())) {
            loadToppingList();
            handleClearTopping(null);
        }
    }

    @FXML void handleClearTopping(ActionEvent event) {
        txtToppingName.clear(); txtToppingPrice.clear();
        chkToppingActive.setSelected(true); lblToppingMsg.setText("");
    }
}