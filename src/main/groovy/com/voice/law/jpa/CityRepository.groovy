package com.voice.law.jpa

import com.voice.law.domain.City
import com.voice.law.domain.Province
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findByProvinceCodeOrderByCode(String provinceCode)
    List<City> findAllByOrderByCode()
}