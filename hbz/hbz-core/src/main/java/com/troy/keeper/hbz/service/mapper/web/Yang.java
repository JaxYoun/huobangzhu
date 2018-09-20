package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.hbz.type.TransType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
//@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_yang")
public class Yang {

    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "trans_type")
    private TransType transType;

}
