package com.voice.law.jpa

import com.voice.law.domain.Occupation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OccupationRepository extends JpaRepository<Occupation, Integer> {

    List<Occupation> findByIndustryCode(String industryCode)
}