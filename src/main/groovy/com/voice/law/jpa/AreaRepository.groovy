package com.voice.law.jpa

import com.voice.law.domain.Area
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AreaRepository extends JpaRepository<Area, Integer> {

}