package com.mega.greentrade.service;
import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.dto.ProductSellerProjection;
import com.mega.greentrade.dto.ProductWithUserProjection;
import com.mega.greentrade.entity.Product;
import com.mega.greentrade.repository.ProductRepository;

import com.mega.greentrade.repository.ChatRoomRepository;
import com.mega.greentrade.repository.HeartRepository;
import com.mega.greentrade.repository.LogRepository;
import com.mega.greentrade.entity.TradeLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final HeartRepository heartRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final LogRepository logRepository;

    public int addItem(ProductDTO dto, MultipartFile imageFile, String uploadDir) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File dest = new File(uploadDir, filename);
            dest.getParentFile().mkdirs();
            imageFile.transferTo(dest);
            dto.setImage(filename);
        }

        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setLikenum(0);
        product.setProductStatus(dto.getProductStatus());
        product.setProductDetail(dto.getProductDetail());
        product.setPaymethod(dto.getPaymethod());
        product.setTrademethod(dto.getTrademethod());
        product.setSellstatus("판매중");
        product.setAdddate(Date.valueOf(LocalDate.now()));
        product.setViewcount(0);
        product.setUserno(dto.getUserno());
        product.setImage(dto.getImage());
        product.setTradelocation(dto.getTradelocation());
        Product saved = productRepository.save(product);

        TradeLog log = new TradeLog();
        log.setTradestartdate(Date.valueOf(LocalDate.now()));
        log.setTradetype("판매");
        log.setTrademethod(dto.getTrademethod());
        log.setTradestatus("거래중");
        log.setProductno(saved.getProductno());
        log.setSelluserno(dto.getUserno());
        logRepository.save(log);

        return saved.getProductno();
    }

    @Transactional
    public ProductDTO getProductDetail(int productno) {
        productRepository.increaseViewCount(productno);
        return getProductInfo(productno);
    }

    public ProductDTO getProductInfo(int productno) {
        return productRepository.findById(productno).map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setProductno(p.getProductno());
            dto.setTitle(p.getTitle());
            dto.setPrice(p.getPrice());
            dto.setLikenum(p.getLikenum());
            dto.setProductStatus(p.getProductStatus());
            dto.setProductDetail(p.getProductDetail());
            dto.setPaymethod(p.getPaymethod());
            dto.setTrademethod(p.getTrademethod());
            dto.setSellstatus(p.getSellstatus());
            dto.setAdddate(p.getAdddate());
            dto.setViewcount(p.getViewcount());
            dto.setUserno(p.getUserno());
            dto.setImage(p.getImage());
            dto.setTradelocation(p.getTradelocation());
            return dto;
        }).orElse(null);
    }

    public ProductSellerProjection getSellerInfo(int productno) {
        return productRepository.findSellerByProductno(productno);
    }

    public List<ProductWithUserProjection> getRecentItems() {
        return productRepository.findRecentItems();
    }

    public Page<ProductWithUserProjection> getProductList(int page, int pageSize) {
        return productRepository.findProductList(PageRequest.of(page - 1, pageSize));
    }

    public List<ProductWithUserProjection> getShareList() {
        return productRepository.findShareList();
    }

    public List<ProductWithUserProjection> getBestList() {
        return productRepository.findBestList();
    }

    public Page<Product> getAllProducts(int page, int pageSize) {
        return productRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Page<ProductWithUserProjection> searchByTitle(String title, int page, int pageSize) {
        return productRepository.searchByTitle("%" + title + "%", PageRequest.of(page - 1, pageSize));
    }

    public List<Product> getSellerItems(int userno) {
        return productRepository.findByUsernoOrderByAdddateDesc(userno);
    }

    @Transactional
    public void deleteProduct(int productno) {
        chatRoomRepository.deleteBySellproduct(productno);
        heartRepository.deleteByProductno(productno);
        productRepository.deleteById(productno);
    }

    @Transactional
    public void deleteSellItem(int productno, int selluserno) {
        chatRoomRepository.deleteBySellproduct(productno);
        heartRepository.deleteByProductno(productno);
        productRepository.deleteById(productno);
        logRepository.deleteByProductnoAndSelluserno(productno, selluserno);
    }
}
