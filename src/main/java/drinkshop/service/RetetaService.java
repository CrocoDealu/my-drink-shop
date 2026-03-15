package drinkshop.service;

import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.repository.RetetaRepository;

import java.util.List;

public class RetetaService {

    private final RetetaRepository retetaRepo;

    public RetetaService(RetetaRepository retetaRepo) {
        this.retetaRepo = retetaRepo;
    }

    public void addReteta(Reteta r) {
        retetaRepo.save(r);
    }

    public void updateReteta(Reteta r) {
        retetaRepo.update(r);
    }

    public void deleteReteta(int id) {
        retetaRepo.delete(id);
    }

    public Reteta findById(int id) {
        return retetaRepo.findOne(id);
    }

    public List<Reteta> getAll() {
        return retetaRepo.findAll();
    }
}