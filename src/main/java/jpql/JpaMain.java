package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUserName("member1");
            member.setAge(10);
            em.persist(member);

//            // 반환타입이 명확할떄
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//
//            // 반환타입이 명확하지 않을떄
//            Query query3 = em.createQuery("select m.userName,m.age from Member m");
//
//            List<Member> resultList = query1.getResultList();
//            Member singleResult = query1.getSingleResult();
//
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//            }
            Member singleResult = em.createQuery("select m from Member m where m.userName= :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult.getUserName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

}
