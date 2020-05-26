package com.voice.law.jpa

import com.voice.law.domain.Industry
import com.voice.law.domain.Occupation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IndustryRepository extends JpaRepository<Industry, Integer> {


}