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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class OrderServiceIntegrationStep2Test {

    // ------------------------------------------------------------------
    // Inner anonymous class: REAL in-memory ProductRepository
    // Extends AbstractRepository (in-memory) and implements the interface.
    // ------------------------------------------------------------------
    private static class InMemoryProductRepository
            extends AbstractRepository<Integer, Product>
            implements ProductRepository {

        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    // REAL ProductRepository (in-memory — no file I/O)
    private ProductRepository productRepo;

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private Order mockOrder;

    private OrderService orderService;

    // Shared test data
    private Product espresso;
    private Product latte;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Instantiate the REAL product repository and populate it
        productRepo = new InMemoryProductRepository();
        espresso = new Product(1, "Espresso", 8.5,
                CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        latte = new Product(3, "Latte", 14.0,
                CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        productRepo.save(espresso);
        productRepo.save(latte);

        // OrderService wired: REAL productRepo, MOCKED orderRepo
        orderService = new OrderService(orderRepo, productRepo);
    }

    // ------------------------------------------------------------------
    // Test 1 – computeTotal queries the REAL productRepo for prices
    // ------------------------------------------------------------------
    @Test
    public void testComputeTotal_usesRealProductRepository() {
        // Arrange
        OrderItem item1 = new OrderItem(espresso, 2); // 2 × 8.5 = 17.0
        OrderItem item2 = new OrderItem(latte, 1);    // 1 × 14.0 = 14.0
        when(mockOrder.getItems()).thenReturn(Arrays.asList(item1, item2));

        // Act – computeTotal internally calls productRepo.findOne()
        double total = orderService.computeTotal(mockOrder);

        // Assert
        assertEquals(31.0, total, 1e-9,
                "Total should be 2×8.5 + 1×14.0 = 31.0");

        // Verify – mock interactions
        verify(mockOrder, times(1)).getItems();
        verifyNoInteractions(orderRepo); // orderRepo is NOT involved in computeTotal
    }

    // ------------------------------------------------------------------
    // Test 2 – addOrder persists via mocked orderRepo; getAllOrders returns list
    // ------------------------------------------------------------------
    @Test
    public void testAddOrder_andGetAllOrders_withMockedOrderRepo() {
        // Arrange
        when(orderRepo.findAll()).thenReturn(List.of(mockOrder));

        // Act
        orderService.addOrder(mockOrder);
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockOrder, result.get(0));

        // Verify
        verify(orderRepo, times(1)).save(mockOrder);
        verify(orderRepo, times(1)).findAll();
    }

    // ------------------------------------------------------------------
    // Test 3 – addItem mutates real Order and delegates update to mocked repo
    // ------------------------------------------------------------------
    @Test
    public void testAddItem_mutatesOrderAndDelegatesUpdate() {
        // Arrange – real Order so getItems() actually mutates state
        Order realOrder = new Order(20);
        OrderItem newItem = new OrderItem(espresso, 3);

        // Act
        orderService.addItem(realOrder, newItem);

        // Assert – item is in the real order
        assertEquals(1, realOrder.getItems().size());
        assertEquals(newItem, realOrder.getItems().get(0));

        // Verify – mocked orderRepo.update was called once with the real order
        verify(orderRepo, times(1)).update(realOrder);
    }
}
