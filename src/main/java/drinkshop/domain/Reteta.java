package drinkshop.domain;

import java.util.List;

public class Reteta extends Entity<Integer> {

    private List<IngredientReteta> ingrediente;

    public Reteta(int id, List<IngredientReteta> ingrediente) {
        super(id);
        this.ingrediente = ingrediente;
    }

    public List<IngredientReteta> getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(List<IngredientReteta> ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public String toString() {
        return "Reteta{" +
                "productId=" + this.getId() +
                ", ingrediente=" + ingrediente +
                '}';
    }
}

