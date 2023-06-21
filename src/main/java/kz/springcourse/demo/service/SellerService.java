package kz.springcourse.demo.service;

import kz.springcourse.demo.model.Seller;
import kz.springcourse.demo.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SellerService {
    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public List<Seller> findAll(){
        return sellerRepository.findAll();
    }

    public Seller findById(Integer id){
        return sellerRepository.findById(id).orElse(null);
    }

    public void save(Seller seller){
        sellerRepository.save(seller);
    }



    public Seller findByUserId(Integer id){
        return sellerRepository.findByUserId(id);
    }

    public void delete(Integer id){
        sellerRepository.delete(sellerRepository.findById(id).orElse(null));
    }
}
