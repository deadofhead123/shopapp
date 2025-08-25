package com.example.shopapp.services.product;

import com.example.shopapp.components.converter.ProductConverter;
import com.example.shopapp.constant.MessageKeys;
import com.example.shopapp.entities.Category;
import com.example.shopapp.entities.Product;
import com.example.shopapp.entities.ProductImage;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.dtos.ProductDTO;
import com.example.shopapp.models.dtos.ProductImageDTO;
import com.example.shopapp.models.responses.ProductResponse;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.utils.LocalizationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductConverter productConverter;
    private final ProductImageRepository productImageRepository;
    private final LocalizationUtil localizationUtil;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                                                      .orElseThrow(() -> new DataNotFoundException(localizationUtil.getLocalizedMessage(MessageKeys.CATEGORY_NOT_FOUND)));

        Product newProduct = productConverter.convertToEntity(productDTO);
        newProduct.setCategory(existingCategory);
        newProduct.setId(null);

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest request) {
        Page<Product> products = productRepository.findAll(request);
        return products.map(productConverter::convertToResponse);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(localizationUtil.getLocalizedMessage(MessageKeys.PRODUCT_NOT_FOUND)));

        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(existingCategory);

        return productConverter.convertToDTO(productRepository.save(existingProduct));
    }

    @Override
    public Boolean deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(localizationUtil.getLocalizedMessage(MessageKeys.PRODUCT_NOT_FOUND)));
        productRepository.delete(existingProduct);
        return true;
    }

    @Override
    public ProductImage createProductImage(ProductImageDTO productImageDTO) {
        // - Khi mà thêm nhiều bản ghi ảnh, tại sao ta phải lấy nhiều lần 1 object sản phẩm ở đây,
        // mà không lấy 1 lần ở lần thêm bản ghi ảnh đầu tiên, rồi dùng cho các lần sau?
        // - Theo tôi hiểu thì có thể trong khi thêm ảnh, bạn ra ngoài database và XÓA CỨNG bản ghi cái sản phẩm đang được thêm ảnh,
        // thì với trường hợp "lấy 1 lần, dùng cho các lần sau", lúc ta thêm ảnh 1,
        // tới ảnh 2 thì object sản phẩm đã bị xóa -> lúc setProduct vào object newProductImage sẽ bị lỗi -> ko insert được vào bảng
        // -> Vẫn nên kiểm tra sản phẩm mỗi lần thêm bản ghi ảnh
        Product existingProduct = productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(localizationUtil.getLocalizedMessage(MessageKeys.PRODUCT_NOT_FOUND)));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        return productImageRepository.save(newProductImage);
    }

//    @Override
//    public ProductImage createProductImage(ProductImageDTO productImageDTO) {
//        Product product = productRepository.findById(productImageDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Product not found"));
//
//        ProductImage newProductImage = ProductImage.builder()
//                .imageUrl(productImageDTO.getImageUrl())
//                .product(product)
//                .build();
//
//        // Cannot insert more than 5 image of a product (Check in database)
//        /* Problem: a product have 4 image exists in "product_images" table. Your purpose is to add 2 images.
//        After add 1 images -> 5 images -> maximum -> Cannot add 1 remaining image -> Should solve this?
//        */
//        int maxImageNumber = productImageRepository.findByProductId(productImageDTO.getProductId()).size();
//        if(maxImageNumber >= 5){
//            throw new InvalidParamException(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX, ProductImage.MAX_IMAGE_PER_PRODUCT));
//        }
//
//        return productImageRepository.save(newProductImage);
//    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public int countProductImages(Long productId){
        return productImageRepository.findByProductId(productId).size();
    }
}
