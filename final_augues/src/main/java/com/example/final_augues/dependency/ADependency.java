package com.example.final_augues.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ADependency {
    @Autowired
    private BDependency bDependency;
}
