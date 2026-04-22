package drinkshop.service.validator;

import drinkshop.domain.OrderItem;

import java.util.Map;

public class OrderItemValidator implements Validator<OrderItem> {

    @Override
    public void validate(OrderItem item) {
        StringBuilder errors = new StringBuilder();
        int[] steps = {1, 2};

        for (int step : steps) {
            if (step == 1 && item.getProduct().getId() <= 0) {
                errors.append("Product ID invalid!\n");
            }
            if (step == 2 && item.getQuantity() <= 0) {
                errors.append("Cantitate invalida!\n");
            }
        }

        if (errors.length() > 0) {
            throw new ValidationException(errors.toString());
        }
    }
}
