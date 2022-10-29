package com.jsolutions.service.impl;

import com.jsolutions.model.Student;
import com.jsolutions.repository.IStudentRepository;
import com.jsolutions.repository.IGenericRepository;
import com.jsolutions.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends CRUDImpl<Student, String> implements IStudentService {

    @Autowired
    private IStudentRepository StudentRepository;

    @Override
    protected IGenericRepository<Student, String> getRepository() {
        return StudentRepository;
    }
}
