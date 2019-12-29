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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUserName("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);

            em.persist(member);


            em.flush();
            em.clear();

//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();
//
//            Member findMember = result.get(0);
//            findMember.setAge(20);

//            List<Team> result = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();
            //select t from Member m join m.team t 명시적으로 표시해주는게 좋음

            //임베디드 타입은 엔티티로부터 시작해야함
//             em.createQuery("select o.address from Order o", Address.class)
//                    .getResultList();


//            List resultList = em.createQuery("select distinct m.userName,m.age from Member m")
//                    .getResultList();
//            Object o = resultList.get(0);
//            Object[] result = (Object[]) o;
//            System.out.println("result[0] = " + result[0]);
//            System.out.println("result[0] = " + result[1]);

//            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.userName,m.age) from Member m", MemberDTO.class)
//                    .getResultList();
//
//            MemberDTO memberDTO = result.get(0);
//            System.out.println("memberDTO = " + memberDTO.getUsername());
//            System.out.println("memberDTO = " + memberDTO.getAge());

//            페이징
//            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(1)
//                    .setMaxResults(10)
//                    .getResultList();
//            System.out.println("result = " + result.size());
//            for (Member member1 : result) {
//                System.out.println("member1 = " + member1);
//            }
//            //조인
//            String query = "select m from Member m left join m.team t on t.name='teamA'";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

//            타입표현과 기타식
//            String query = "select m.userName, 'HELLO', true from Member m " +
//                    "where m.type = :userType" ;
//            List<Object[]> result = em.createQuery(query)
//                    .setParameter("userType",MemberType.ADMIN)
//                    .getResultList();

//            조건식
//            String query = "select " +
//                    "case when m.age<=10 then '학생요금' " +
//                    "     when m.age>=60 then '경로요금' " +
//                    "     else '일반요금' " +
//                    "end " +
//                    "from Member m";
//            String query2 = "select nullif(m.userName, '관리자') from Member m ";
//            List<String> result = em.createQuery(query2, String.class)
//                    .getResultList();

//            jpql 함수 - concat, substring, trim, lower, upper, length, locate, abs, sqrt, mod, size, index
            String query = "select size(t.members) from Team t ";
            String query2 = "select function('group_concat',m.userName) from Member m "; // persistence.xml에 ibernate.dialect에 my dialect 등록해야
            List<Integer> result = em.createQuery(query, Integer.class)
                    .getResultList();
            for (Integer integer : result) {
                System.out.println("integer = " + integer);
            }


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
