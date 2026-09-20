package com.tjit.sms.dao;

import com.tjit.sms.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class StudentDAO {

    private final EntityManagerFactory emf;

    public StudentDAO() {
        // "studentPU" must match the name in persistence.xml
        this.emf = Persistence.createEntityManagerFactory("studentPU");
    }

    // ---------- CREATE ----------
    public Long addStudent(Student student) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(student);
            tx.commit();
            return student.getId();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ---------- READ ----------
    public Student findStudent(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Student.class, id);
        } finally {
            em.close();
        }
    }

    // ---------- UPDATE ----------
    public boolean updateStudent(Long id, String newCourse, String newPhone) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Student student = em.find(Student.class, id);
            if (student == null) {
                tx.rollback();
                return false;
            }
            student.setCourse(newCourse);
            student.setPhone(newPhone);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ---------- DELETE ----------
    public boolean deleteStudent(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Student student = em.find(Student.class, id);
            if (student == null) {
                tx.rollback();
                return false;
            }
            em.remove(student);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}