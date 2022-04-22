package com.example.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TacoOrder implements Serializable {

    private final static long serialVersionUID = 1L;
    private long id;
    private Date placedAt;
}
