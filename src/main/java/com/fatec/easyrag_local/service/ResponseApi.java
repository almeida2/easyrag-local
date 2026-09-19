package com.fatec.easyrag_local.service;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApi<T> {

    private String status;
    private T data;
    private String message;

    // O generics <T> permite generalizar o tipo de dado que será retornado
    public ResponseApi(T data, String message) {
        this.status = "success";
        this.data = data;
        this.message = message;
    }

    // Construtor para erro
    public ResponseApi(String message) {
        this.status = "error";
        this.data = null; // Para erros, a parte de dados é nula
        this.message = message;
    }

    public ResponseApi() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
