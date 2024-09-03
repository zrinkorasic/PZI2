package org.example.service;

import org.example.Models.Brand;
import org.example.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    BrandRepository brandRepository;

    public void save(Brand brand)
    {
        brandRepository.save(brand);
    }

    public List<Brand> findAllBrands(){
        return brandRepository.findAll();
    }

    public void delete(Long id ){
        brandRepository.deleteById(id);
    }

    public Optional<Brand> findById(Long id){
        return brandRepository.findById(id);
    }
}
