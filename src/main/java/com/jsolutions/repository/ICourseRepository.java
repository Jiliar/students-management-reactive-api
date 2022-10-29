package com.jsolutions.repository;

import com.jsolutions.model.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseRepository extends IGenericRepository<Course, String>{
}
