package com.jsolutions.repository;

import com.jsolutions.model.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface IStudentRepository extends IGenericRepository<Student, String>{
}
