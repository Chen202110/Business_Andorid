package com.chen.vo;

import java.util.List;

public class UserLikesProductVo {

    private String categoryName;
    private List<ProductListVo> productListVoList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductListVo> getProductListVoList() {
        return productListVoList;
    }

    public void setProductListVoList(List<ProductListVo> productListVoList) {
        this.productListVoList = productListVoList;
    }
}
