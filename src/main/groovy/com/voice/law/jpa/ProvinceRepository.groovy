package com.voice.law.jpa

import com.voice.law.domain.Province
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProvinceRepository extends JpaRepository<Province, Integer> {

}