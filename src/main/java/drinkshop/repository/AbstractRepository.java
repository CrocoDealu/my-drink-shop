package drinkshop.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class AbstractRepository<I, E>
        implements Repository<I, E> {

    protected Map<I, E> entities = new HashMap<>();

    @Override
    public E findOne(I id) {
        return entities.get(id);
    }

    @Override
    public List<E> findAll() {
        return StreamSupport.stream(entities.values().spliterator(), false).toList();
    }

    @Override
    public E save(E entity) {
        entities.put(getId(entity), entity);
        return entity;
    }

    @Override
    public E delete(I id) {
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        E existing = entities.get(getId(entity));
        if (existing != null) {
            entities.put(getId(entity), entity);
        }
        return entity;
    }

    protected abstract I getId(E entity);
}
