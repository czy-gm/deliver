package com.jd.transportation.dao;

public interface UpperAddressDao {

    void save(Integer addressId, Integer upperId);

    Integer getUpperLevelAddressId(Integer addressId);
}
