package drinkshop.service;

import drinkshop.domain.Order;
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

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private Order mockOrder;

    // Șterge @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepo, productRepo);
    }

    @Test
    public void testFindById() {
        // Arrange
        int orderId = 1;
        when(orderRepo.findOne(orderId)).thenReturn(mockOrder);

        // Act
        Order result = orderService.findById(orderId);

        // Assert & Verify
        assertNotNull(result);
        assertEquals(mockOrder, result);
        verify(orderRepo, times(1)).findOne(orderId);
    }

    @Test
    public void testGetAllOrders() {
        // Arrange
        when(orderRepo.findAll()).thenReturn(Arrays.asList(mockOrder));

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert & Verify
        assertEquals(1, result.size());
        assertEquals(mockOrder, result.get(0));
        verify(orderRepo, times(1)).findAll();
    }

    @Test
    public void testDeleteOrder() {
        // Arrange
        int orderId = 2;

        // Act
        orderService.deleteOrder(orderId);

        // Verify
        verify(orderRepo, times(1)).delete(orderId);
    }
}