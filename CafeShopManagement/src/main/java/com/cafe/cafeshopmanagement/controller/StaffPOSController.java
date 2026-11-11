package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.CustomerDAO;
import com.cafe.cafeshopmanagement.dao.OrderDAO;
import com.cafe.cafeshopmanagement.dao.OrderDetailDAO;
import com.cafe.cafeshopmanagement.dao.ProductDAO;
import com.cafe.cafeshopmanagement.dao.ToppingDAO;
import com.cafe.cafeshopmanagement.model.CartItem;
import com.cafe.cafeshopmanagement.model.Customer;
import com.cafe.cafeshopmanagement.model.Order;
import com.cafe.cafeshopmanagement.model.Product;
import com.cafe.cafeshopmanagement.model.Topping;
import com.cafe.cafeshopmanagement.utils.SceneManager;
import com.cafe.cafeshopmanagement.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffPOSController {

    @FXML private FlowPane flowPaneProducts;

    @FXML private TableView<CartItem> tableCart;
    @FXML private TableColumn<CartItem, String> colCartProduct;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, String> colCartUnitPrice;
    @FXML private TableColumn<CartItem, String> colCartTotal;

    @FXML private TextField txtCustomerPhone;
    @FXML private Label lblCustomerInfo;
    @FXML private Label lblSubTotal;
    @FXML private Label lblFinalTotal;

    private ProductDAO productDAO = new ProductDAO();
    private ToppingDAO toppingDAO = new ToppingDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    private ObservableList<CartItem> cartList = FXCollections.observableArrayList();

    // Biến lưu trạng thái khách hàng hiện tại và điểm muốn dùng
    private Customer currentCustomer = null;
    private int pointsToUse = 0;

    @FXML
    public void initialize() {
        colCartProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartUnitPrice.setCellValueFactory(new PropertyValueFactory<>("formattedUnitPrice"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotalPrice"));

        tableCart.setItems(cartList);
        loadProducts(productDAO.getActiveProducts());
    }

    private void loadProducts(List<Product> products) {
        flowPaneProducts.getChildren().clear();
        for (Product p : products) {
            VBox card = createProductCard(p);
            flowPaneProducts.getChildren().add(card);
        }
    }

    private VBox createProductCard(Product p) {
        VBox card = new VBox(8);
        card.setPrefSize(145, 175);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));

        String normalStyle = "-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);";
        String hoverStyle = "-fx-background-color: #f5f9ff; -fx-border-color: #3498db; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(52,152,219,0.3), 8, 0, 0, 3);";
        card.setStyle(normalStyle);

        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(normalStyle));

        Node imageNode;
        String imageUrl = p.getImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                Image image;
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://") || imageUrl.startsWith("file:")) {
                    image = new Image(imageUrl, true);
                } else {
                    File file = new File(imageUrl);
                    if (file.exists()) {
                        image = new Image(file.toURI().toString());
                    } else {
                        image = null;
                    }
                }

                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(60);
                    imageView.setFitHeight(60);
                    imageView.setPreserveRatio(true);
                    imageNode = imageView;
                } else {
                    Label iconLabel = new Label("☕");
                    iconLabel.setStyle("-fx-font-size: 32px;");
                    imageNode = iconLabel;
                }
            } catch (Exception e) {
                Label iconLabel = new Label("☕");
                iconLabel.setStyle("-fx-font-size: 32px;");
                imageNode = iconLabel;
            }
        } else {
            Label iconLabel = new Label("☕");
            iconLabel.setStyle("-fx-font-size: 32px;");
            imageNode = iconLabel;
        }

        Label lblName = new Label(p.getName());
        lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-alignment: center;");
        lblName.setWrapText(true);

        Label lblPrice = new Label(String.format("%,d VNĐ", p.getBasePrice()));
        lblPrice.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 13px;");

        card.getChildren().addAll(imageNode, lblName, lblPrice);
        card.setOnMouseClicked(e -> showCustomizeDialog(p));

        return card;
    }

    private void showCustomizeDialog(Product product) {
        Dialog<CartItem> dialog = new Dialog<>();
        dialog.setTitle("Tùy chọn món - " + product.getName());
        dialog.setHeaderText("Tùy chỉnh Size, Đường, Đá & Topping");

        ButtonType btnAddType = new ButtonType("Thêm vào giỏ", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAddType, ButtonType.CANCEL);

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(15));
        layout.setPrefWidth(380);

        // 1. Chọn Size
        double base = product.getBasePrice();
        Label lblSize = new Label("Kích cỡ (Size):");
        lblSize.setStyle("-fx-font-weight: bold;");
        ToggleGroup sizeGroup = new ToggleGroup();
        RadioButton rbS = new RadioButton(String.format("Size S (70%% - %,dđ)", (long)(base * 0.7)));
        RadioButton rbM = new RadioButton(String.format("Size M (100%% - %,dđ)", (long)(base * 1.0)));
        RadioButton rbL = new RadioButton(String.format("Size L (130%% - %,dđ)", (long)(base * 1.3)));
        rbS.setToggleGroup(sizeGroup);
        rbM.setToggleGroup(sizeGroup);
        rbL.setToggleGroup(sizeGroup);
        rbM.setSelected(true);
        VBox sizeBox = new VBox(5, rbS, rbM, rbL);

        // 2. Chọn Đường & Đá
        Label lblSugarIce = new Label("Mức Đường & Đá (Không đổi giá):");
        lblSugarIce.setStyle("-fx-font-weight: bold;");
        ComboBox<String> cbSugar = new ComboBox<>(FXCollections.observableArrayList("100% Đường", "70% Đường", "50% Đường", "30% Đường", "0% Đường"));
        cbSugar.getSelectionModel().selectFirst();
        ComboBox<String> cbIce = new ComboBox<>(FXCollections.observableArrayList("100% Đá", "70% Đá", "50% Đá", "30% Đá", "0% Đá"));
        cbIce.getSelectionModel().selectFirst();
        HBox sugarIceBox = new HBox(10, cbSugar, cbIce);

        // 3. Topping đi kèm
        Label lblTopping = new Label("Topping thêm:");
        lblTopping.setStyle("-fx-font-weight: bold;");
        VBox toppingBox = new VBox(5);
        List<CheckBox> toppingCheckBoxes = new ArrayList<>();
        List<Topping> allToppings = toppingDAO.getAllToppings();
        for (Topping t : allToppings) {
            CheckBox cb = new CheckBox(t.getName() + " (+" + String.format("%,d", t.getPrice()) + "đ)");
            cb.setUserData(t);
            toppingCheckBoxes.add(cb);
            toppingBox.getChildren().add(cb);
        }

        // 4. Số lượng
        Label lblQty = new Label("Số lượng:");
        lblQty.setStyle("-fx-font-weight: bold;");
        Spinner<Integer> spinnerQty = new Spinner<>(1, 50, 1);
        spinnerQty.setEditable(true);

        layout.getChildren().addAll(lblSize, sizeBox, new Separator(), lblSugarIce, sugarIceBox, new Separator(), lblTopping, toppingBox, new Separator(), lblQty, spinnerQty);
        dialog.getDialogPane().setContent(layout);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAddType) {
                String selectedSize = rbS.isSelected() ? "S" : (rbL.isSelected() ? "L" : "M");
                int sugar = Integer.parseInt(cbSugar.getValue().replace("% Đường", ""));
                int ice = Integer.parseInt(cbIce.getValue().replace("% Đá", ""));

                List<Topping> selectedToppings = new ArrayList<>();
                for (CheckBox cb : toppingCheckBoxes) {
                    if (cb.isSelected()) {
                        selectedToppings.add((Topping) cb.getUserData());
                    }
                }
                return new CartItem(product, selectedSize, sugar, ice, selectedToppings, spinnerQty.getValue());
            }
            return null;
        });

        Optional<CartItem> result = dialog.showAndWait();
        result.ifPresent(cartItem -> {
            cartList.add(cartItem);
            updateTotals();
        });
    }

    // TÍNH LẠI HÓA ĐƠN KÈM GIẢM GIÁ (TỪ ĐIỂM)
    private void updateTotals() {
        double subTotal = 0;
        for (CartItem item : cartList) {
            subTotal += item.getTotalPrice();
        }

        int discountAmount = pointsToUse * 5; // Tỷ lệ: 200 điểm = 1.000đ => 1 điểm = 5đ
        double finalTotal = Math.max(0, subTotal - discountAmount);

        lblSubTotal.setText(String.format("%,.0f VNĐ", subTotal));

        if (pointsToUse > 0) {
            lblFinalTotal.setText(String.format("%,.0f VNĐ (Đã giảm %,dđ từ %d điểm)", finalTotal, discountAmount, pointsToUse));
        } else {
            lblFinalTotal.setText(String.format("%,.0f VNĐ", finalTotal));
        }
    }

    @FXML
    void handleRemoveCartItem(ActionEvent event) {
        CartItem selected = tableCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartList.remove(selected);
            updateTotals();
        }
    }

    // XỬ LÝ TÌM KHÁCH HÀNG & GỢI Ý ĐỔI ĐIỂM
    // 1. NÚT TÌM KIẾM (Chỉ tìm, không tự động hỏi thêm mới)
    @FXML
    void handleSearchCustomer(ActionEvent event) {
        String phone = txtCustomerPhone.getText().trim();
        if (phone.isEmpty()) {
            resetCustomerInfo();
            return;
        }

        Customer c = customerDAO.getCustomerByPhone(phone);
        if (c != null) {
            currentCustomer = c;
            lblCustomerInfo.setText(String.format("Khách hàng: %s | Điểm: %d", c.getFullName(), c.getPoints()));

            // Gợi ý đổi điểm nếu đủ điều kiện
            if (c.getPoints() >= 200) {
                showUsePointsDialog(c);
            }
        } else {
            resetCustomerInfo();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Không tìm thấy khách hàng! Hãy nhấn nút 'Thêm' nếu muốn tạo mới.");
            alert.show();
        }
        updateTotals();
    }

    // 2. NÚT THÊM MỚI (Tách riêng biệt)
    @FXML
    void handleAddCustomer(ActionEvent event) {
        // Lấy sẵn SĐT nhân viên đang gõ (nếu có) để điền sẵn vào form cho nhanh
        String currentPhone = txtCustomerPhone.getText().trim();
        showRegisterCustomerDialog(currentPhone);
    }

    private void resetCustomerInfo() {
        currentCustomer = null;
        pointsToUse = 0;
        lblCustomerInfo.setText("Khách vãng lai (0 điểm)");
    }

    private void showUsePointsDialog(Customer customer) {
        int maxDiscount = (customer.getPoints() / 200) * 1000;

        TextInputDialog dialog = new TextInputDialog("200");
        dialog.setTitle("Sử dụng điểm thưởng");
        dialog.setHeaderText(String.format("Khách có %d điểm (Quy đổi tối đa: %,d VNĐ)", customer.getPoints(), maxDiscount));
        dialog.setContentText("Nhập số điểm muốn đổi (mỗi 200 điểm ứng với 1000 VNĐ):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                int pts = Integer.parseInt(input);
                if (pts > customer.getPoints()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Số điểm nhập vượt quá số điểm hiện có!");
                    alert.show();
                } else if (pts % 200 != 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Số điểm phải là bội số của 200!");
                    alert.show();
                } else {
                    pointsToUse = pts;
                    updateTotals();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Số điểm không hợp lệ!");
                alert.show();
            }
        });
    }

    // XỬ LÝ CHECKOUT & TÍCH ĐIỂM
    @FXML
    void handleCheckout(ActionEvent event) {
        if (cartList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Giỏ hàng đang trống!");
            alert.show();
            return;
        }

        int subTotalAmount = 0;
        for (CartItem item : cartList) {
            subTotalAmount += (int) item.getTotalPrice();
        }

        // Tính tiền thực tế khách phải trả sau khi trừ điểm
        int discountAmount = pointsToUse * 5;
        int finalTotalAmount = Math.max(0, subTotalAmount - discountAmount);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cafe/cafeshopmanagement/CheckoutConfirm.fxml"));
            javafx.scene.Parent root = loader.load();

            CheckoutConfirmController confirmController = loader.getController();
            String customerInfo = (currentCustomer != null) ? currentCustomer.getFullName() + " (" + currentCustomer.getPhone() + ")" : "Khách vãng lai";
            confirmController.setInvoiceData(cartList, customerInfo, finalTotalAmount);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Xác nhận Hóa đơn");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            if (confirmController.isConfirmed()) {
                Order order = new Order();
                order.setStaffId(UserSession.getUserId());
                if (currentCustomer != null) {
                    order.setCustomerId(currentCustomer.getCustomerId());
                }
                order.setTotalAmount(finalTotalAmount);
                // order.setPointsUsed(pointsToUse); // Mở dòng này nếu model Order của bạn có thuộc tính pointsUsed
                order.setPaymentMethod("CASH");
                order.setStatus("COMPLETED");

                int newOrderId = orderDAO.createOrder(order);

                if (newOrderId > 0) {
                    boolean isSuccess = orderDetailDAO.saveOrderDetailsAndToppings(newOrderId, cartList);

                    // XỬ LÝ TÍCH ĐIỂM
                    if (isSuccess && currentCustomer != null) {
                        int pointsEarned = finalTotalAmount / 100; // 1.000 VNĐ = 10 điểm
                        customerDAO.updateCustomerPoints(currentCustomer.getCustomerId(), pointsEarned, pointsToUse);
                    }

                    if (isSuccess) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thanh toán & Lưu Hóa Đơn thành công!");
                        alert.showAndWait();

                        // Reset UI
                        cartList.clear();
                        resetCustomerInfo();
                        txtCustomerPhone.clear();
                        updateTotals();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi lưu chi tiết đơn hàng!");
                        alert.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi mở giao diện xác nhận: " + e.getMessage());
            alert.show();
        }
    }

    @FXML void handleFilterAll(ActionEvent event) { loadProducts(productDAO.getActiveProducts()); }
    @FXML void handleFilterCoffee(ActionEvent event) { loadProducts(productDAO.getProductsByCategory(1)); }
    @FXML void handleFilterTea(ActionEvent event) { loadProducts(productDAO.getProductsByCategory(2)); }
    @FXML void handleFilterPhindi(ActionEvent event) { loadProducts(productDAO.getProductsByCategory(3)); }

    @FXML
    void handleLogout(ActionEvent event) {
        String currentRole = UserSession.getRole();

        if ("ADMIN".equals(currentRole)) {
            SceneManager.switchScene("/com/cafe/cafeshopmanagement/AdminDashboard.fxml", "Admin Dashboard");
        } else {
            UserSession.clear();
            SceneManager.switchScene("/com/cafe/cafeshopmanagement/Login.fxml", "Đăng nhập");
        }
    }

    // Dialog cho phép nhân viên đăng ký thành viên mới trực tiếp tại POS
    private void showRegisterCustomerDialog(String defaultPhone) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Đăng ký Khách hàng mới");
        dialog.setHeaderText("Tạo tài khoản tích điểm cho số điện thoại: " + defaultPhone);

        ButtonType btnRegisterType = new ButtonType("Tạo thành viên", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnRegisterType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtPhone = new TextField(defaultPhone);
        TextField txtName = new TextField();
        txtName.setPromptText("Nhập họ và tên khách hàng");

        grid.add(new Label("Số điện thoại:"), 0, 0);
        grid.add(txtPhone, 1, 0);
        grid.add(new Label("Họ và tên:"), 0, 1);
        grid.add(txtName, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Chuyển kết quả khi nhấn nút "Tạo thành viên"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnRegisterType) {
                return new Pair<>(txtPhone.getText().trim(), txtName.getText().trim());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String phone = pair.getKey();
            String name = pair.getValue();

            if (phone.isEmpty() || name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ Số điện thoại và Họ tên!");
                alert.show();
                return;
            }

            Customer newCustomer = customerDAO.registerCustomer(phone, name);
            if (newCustomer != null) {
                currentCustomer = newCustomer;
                txtCustomerPhone.setText(phone);
                lblCustomerInfo.setText(String.format("Khách hàng: %s | Điểm: %d", newCustomer.getFullName(), newCustomer.getPoints()));

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tạo tài khoản khách hàng thành công!");
                alert.show();
                updateTotals();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi tạo tài khoản (SĐT có thể đã tồn tại)!");
                alert.show();
            }
        });
    }
}