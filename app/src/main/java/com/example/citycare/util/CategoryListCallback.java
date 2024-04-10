package com.example.citycare.util;

import com.example.citycare.model.MainCategoryModel;
import java.util.List;

public interface CategoryListCallback {
    void onSuccess(List<MainCategoryModel> categoryModels);
    void onError(String errorMessage);
}
