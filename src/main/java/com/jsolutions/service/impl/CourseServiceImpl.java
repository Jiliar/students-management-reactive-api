package com.jsolutions.service.impl;

import com.jsolutions.model.Course;
import com.jsolutions.repository.IGenericRepository;
import com.jsolutions.repository.ICourseRepository;
import com.jsolutions.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends CRUDImpl<Course, String> implements ICourseService {

    @Autowired
    private ICourseRepository CourseRepository;

    @Override
    protected IGenericRepository<Course, String> getRepository() {
        return CourseRepository;
    }
}
