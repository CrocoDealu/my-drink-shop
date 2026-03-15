package drinkshop.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order extends Entity<Integer> {

    private List<OrderItem> items;
    private double totalPrice;

    public Order(int id) {
        super(id);
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Order(int id, List<OrderItem> items, double totalPrice) {
        super(id);
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + this.getId() +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public double getTotal() {
        return totalPrice;
    }

    public void computeTotalPrice() {
        this.totalPrice=items.stream().mapToDouble(OrderItem::getTotal).sum();
    }
}