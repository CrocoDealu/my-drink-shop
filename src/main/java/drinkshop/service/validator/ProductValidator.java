package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {
        StringBuilder errors = new StringBuilder();

        if (product.getId() <= 0)
            errors.append("ID invalid!\n");

        if (product.getNume() == null || product.getNume().isBlank()) {
            errors.append("Numele nu poate fi gol!\n");
        } else if (product.getNume().length() > 255) {
            errors.append("Numele este prea lung (max 255 caractere)!\n");
        }

        if (product.getPret() <= 0)
            errors.append("Pret invalid!\n");

        if (product.getCategorie() == null)
            errors.append("Categoria nu poate fi null!\n");

        if (product.getTip() == null)
            errors.append("Tipul bauturii nu poate fi null!\n");

        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }
}