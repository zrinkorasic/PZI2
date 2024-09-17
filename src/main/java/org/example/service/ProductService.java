package org.example.service;

import org.example.Models.Product;
import org.example.repository.ProductRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProductsByBrandId(Long id){
        return this.productRepository.findAllProductsByBrandId(id);
    }

    public Product findById(Long id){
        Optional <Product> product = this.productRepository.findById(id);
        if(product.isPresent())
        {
            return product.get();
        }
        else{
            throw new RuntimeException("Produkt ne postoji");
        }
    }

    public List<Product>findAll(String search){
        if(search == null){
            return this.productRepository.findAll();
        }else{
            return this.productRepository.findAllSearchProducts(search);
        }
    }

    public void saveProduct(Product product , MultipartFile image)throws IOException{

        if(image != null){
            String originalFileName = image.getOriginalFilename();
            String fullFileName = UUID.randomUUID().toString()+originalFileName.substring(originalFileName.lastIndexOf("."));

            String directory = "/app/uploads/";
            Path imagePath = Paths.get(directory, fullFileName);

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
            }
            product.setImageName(fullFileName);
            this.productRepository.save(product);
        }else{
            product.setImageName(" ");
            this.productRepository.save(product);
        }
    }
    public void deleteProductById(Long id){

        Optional <Product> product = this.productRepository.findById(id);
        if(product.isPresent())
        {
            String imageName = product.get().getImageName();
            if(imageName != null){
                String directory ="/app/uploads/";
                Path imagePath = Paths.get(directory, imageName);
                try {
                    Files.delete(imagePath);
                    this.productRepository.deleteById(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                this.productRepository.deleteById(id);
            }
        } else{
            throw new RuntimeException("Produkt ne postoji.");
        }



    }
    public void updateProduct(Product product,MultipartFile image,Product existingProduct){

        String previousImage = existingProduct.getImageName();
        String directory = "/app/uploads/";
        Path imagePath = Paths.get(directory, previousImage);
        try {
            Files.delete(imagePath);
            this.saveProduct(product,image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
