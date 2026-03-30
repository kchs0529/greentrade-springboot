package com.mega.greentrade.product;

import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDAO productDAO;

    public int addItem(ProductDTO dto, MultipartFile imageFile, String uploadDir) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File dest = new File(uploadDir, filename);
            dest.getParentFile().mkdirs();
            imageFile.transferTo(dest);
            dto.setImage(filename);
        }
        dto.setLikenum(0);
        return productDAO.insertItem(dto);
    }

    public ProductDTO getProductDetail(int productno) {
        productDAO.increaseViewCount(productno);
        return productDAO.getProductInfo(productno);
    }

    public ProductDTO getProductInfo(int productno) {
        return productDAO.getProductInfo(productno);
    }

    public UserDTO getSellerInfo(int productno) {
        return productDAO.getSellerInfo(productno);
    }

    public List<ProductDTO> getRecentItems() {
        return productDAO.getRecentItems();
    }

    public List<ProductDTO> getProductList() {
        return productDAO.getProductList();
    }

    public List<ProductDTO> getShareList() {
        return productDAO.getShareList();
    }

    public List<ProductDTO> getBestList() {
        return productDAO.getBestList();
    }

    public List<ProductDTO> getAllProducts(int page, int pageSize) {
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        return productDAO.getAllProducts(startRow, endRow);
    }

    public List<ProductDTO> searchByTitle(String title, int page, int pageSize) {
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        return productDAO.searchByTitle(title, startRow, endRow);
    }

    public int getTotalCount() {
        return productDAO.getTotalCount();
    }

    public int getTotalCount(String title) {
        return productDAO.getTotalCount(title);
    }

    public List<ProductDTO> getSellerItems(int userno) {
        return productDAO.getSellerItems(userno);
    }

    public void deleteProduct(int productno) {
        productDAO.deleteProduct(productno);
    }
}
