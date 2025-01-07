package com.jyx.eshopbackend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  // 关联的订单

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 关联的商品

    @Column(nullable = false)
    private int quantity;  // 商品数量

    @Column(nullable = false)
    private BigDecimal price;  // 商品单价

    @Column(nullable = false)
    private BigDecimal totalPrice;  // 商品总价（单价 * 数量）

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    // 计算总价：price * quantity
    public void calculateTotalPrice() {
        this.totalPrice = this.price.multiply(new BigDecimal(this.quantity));
    }
}

