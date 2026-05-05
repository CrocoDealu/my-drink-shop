package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.OrderRepository;
import drinkshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceIntegrationStep3Test {

    // ------------------------------------------------------------------
    // REAL in-memory ProductRepository
    // ------------------------------------------------------------------
    private static class InMemoryProductRepository
            extends AbstractRepository<Integer, Product>
            implements ProductRepository {

        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    // ------------------------------------------------------------------
    // REAL in-memory OrderRepository
    // ------------------------------------------------------------------
    private static class InMemoryOrderRepository
            extends AbstractRepository<Integer, Order>
            implements OrderRepository {

        @Override
        protected Integer getId(Order entity) {
            return entity.getId();
        }
    }

    // REAL repositories
    private ProductRepository productRepo;
    private OrderRepository   orderRepo;

    @Mock
    private Order mockOrder;

    private OrderService orderService;

    // Shared test products
    private Product espresso;
    private Product latte;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Instantiate REAL repositories
        productRepo = new InMemoryProductRepository();
        orderRepo   = new InMemoryOrderRepository();

        // Populate product catalogue
        espresso = new Product(1, "Espresso", 8.5,
                CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        latte    = new Product(3, "Latte", 14.0,
                CategorieBautura.MILK_COFFEE,   TipBautura.DAIRY);
        productRepo.save(espresso);
        productRepo.save(latte);

        // OrderService wired with BOTH real repositories
        orderService = new OrderService(orderRepo, productRepo);
    }

    // ------------------------------------------------------------------
    // Test 1 – addOrder persists into REAL orderRepo; findById retrieves it
    // ------------------------------------------------------------------
    @Test
    public void testAddOrder_thenFindById_withRealOrderRepository() {
        // Arrange – a fully real Order
        Order order = new Order(1);
        OrderItem item = new OrderItem(espresso, 2);
        order.addItem(item);

        // Act
        orderService.addOrder(order);
        Order found = orderService.findById(1);

        // Assert – the real repo returned the saved order
        assertNotNull(found);
        assertEquals(1, found.getId());
        assertEquals(1, found.getItems().size());
        assertEquals(espresso, found.getItems().get(0).getProduct());

        // Verify – no stubs used; verify via state (real repo)
        // Cross-check getAllOrders also returns the order
        List<Order> all = orderService.getAllOrders();
        assertEquals(1, all.size());
    }

    // ------------------------------------------------------------------
    // Test 2 – deleteOrder removes entry from REAL orderRepo
    // ------------------------------------------------------------------
    @Test
    public void testDeleteOrder_removesFromRealOrderRepository() {
        // Arrange – save two orders, then delete one
        Order order1 = new Order(10);
        Order order2 = new Order(20);
        orderService.addOrder(order1);
        orderService.addOrder(order2);

        // Precondition
        assertEquals(2, orderService.getAllOrders().size());

        // Act
        orderService.deleteOrder(10);

        // Assert – only order2 remains
        List<Order> remaining = orderService.getAllOrders();
        assertEquals(1, remaining.size());
        assertNull(orderService.findById(10));
        assertNotNull(orderService.findById(20));
    }

    // ------------------------------------------------------------------
    // Test 3 – computeTotal uses REAL productRepo prices; mockOrder supplies items
    // ------------------------------------------------------------------
    @Test
    public void testComputeTotal_withRealProductRepoAndMockedOrder() {
        // Arrange
        OrderItem item1 = new OrderItem(espresso, 3); // 3 × 8.5 = 25.5
        OrderItem item2 = new OrderItem(latte, 2);    // 2 × 14.0 = 28.0
        when(mockOrder.getItems()).thenReturn(Arrays.asList(item1, item2));

        // Act – real productRepo.findOne() is called internally
        double total = orderService.computeTotal(mockOrder);

        // Assert
        assertEquals(53.5, total, 1e-9,
                "Total should be 3×8.5 + 2×14.0 = 53.5");

        // Verify – mockOrder.getItems() was accessed exactly once
        verify(mockOrder, times(1)).getItems();
    }

    // ------------------------------------------------------------------
    // Test 4 – addItem + removeItem cycle updates REAL orderRepo state
    // ------------------------------------------------------------------
    @Test
    public void testAddItem_thenRemoveItem_updatesRealOrderRepository() {
        // Arrange – persist a real order first
        Order order = new Order(5);
        orderService.addOrder(order);

        OrderItem item = new OrderItem(latte, 1);

        // Act – add item (internally calls orderRepo.update)
        orderService.addItem(order, item);

        // Assert – item is present
        Order afterAdd = orderService.findById(5);
        assertNotNull(afterAdd);
        assertEquals(1, afterAdd.getItems().size());

        // Act – remove item (internally calls orderRepo.update)
        orderService.removeItem(order, item);

        // Assert – item is gone
        Order afterRemove = orderService.findById(5);
        assertNotNull(afterRemove);
        assertTrue(afterRemove.getItems().isEmpty());
    }
}
