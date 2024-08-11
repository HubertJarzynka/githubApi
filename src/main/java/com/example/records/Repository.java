package com.example.records;

public record Repository (
    String name,
    Owner owner,
    boolean fork

){}
