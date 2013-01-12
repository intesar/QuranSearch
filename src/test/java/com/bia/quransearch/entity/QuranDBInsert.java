/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.quransearch.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author mdshannan
 */
public class QuranDBInsert {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            InputStream input = QuranDBInsert.class.getResourceAsStream("/English-Yusuf-Ali-59 (2).csv");
            BufferedReader x = new BufferedReader(new InputStreamReader(input));
            try {
                String line = null; //not declared within while loop
                /*
                 * readLine is a bit quirky :
                 * it returns the content of a line MINUS the newline.
                 * it returns null only for the END of the stream.
                 * it returns an empty String if two newlines appear in a row.
                 */
                int count = 1;
                List<Quran> list = new ArrayList<>();
                while ((line = x.readLine()) != null) {
                    System.out.println(line);
                    String[] tokens = line.split("\\|");
                    System.out.println(tokens[0]);
                    Quran quran = new Quran();
                    //quran.setDatabaseID(Short.parseShort(tokens[0]));
                    quran.setSuraID(Integer.parseInt(tokens[0]));
                    quran.setVerseID(Integer.parseInt(tokens[1]));
                    quran.setAyahText(tokens[2]);
                    list.add(quran);
                    System.out.println("inserted.." + count++);
                }
                persist(list);
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void persist(List<Quran> list) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("QuranDBInsertPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            for (Quran q : list) {
                em.persist(q);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
