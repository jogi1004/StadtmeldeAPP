package com.example.citycare.util;

public interface AllReportsCallback {
    default void onSuccess(){

    }
    default void onError(String errorMessage){

    }
}
