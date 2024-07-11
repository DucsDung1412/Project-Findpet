package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.SponsDAO;
import vn.finder.pet.entity.Spons;

import java.util.List;

@Service
public class SponsService {
    private SponsDAO sponsDAO;

    @Autowired
    public SponsService(SponsDAO sponsDAO) {
        this.sponsDAO = sponsDAO;
    }

    @Transactional
    public Boolean save(Spons spons){
        try {
            this.sponsDAO.save(spons);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public Spons findById(Long id){
        return this.sponsDAO.findById(id).get();
    }

    public List<Spons> findAll(){
        return this.sponsDAO.findAll();
    }
}
