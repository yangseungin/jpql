package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUserName("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUserName("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUserName("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            em.flush();
            em.clear();

//            경로표현식
//            String query1 = "select t.members From Team t"; //묵시적 조인 - 사용하지말것을 권장
//            String query = "select m.userName From Team t join t.members m"; // 명시적 조
//            List<Collection> result = em.createQuery(query, Collection.class).getResultList();

//            패치조인 ManyToOne
//            String query = "select m From Member m join fetch m.team";
//            List<Member> result = em.createQuery(query, Member.class).getResultList();
//            for (Member member : result) {
//                System.out.println("member = " + member.getUserName()+ ", "+member.getTeam().getName());
//                //회원 1, 팀A(SQL)
//                //회원 2, 팀A(1차캐시)
//                //회원 3, 팀B(SQL)
//                //회원 100명-> N + 1 (1= 회원을 가져오기위해 가져온 쿼리 만큼N번을 날린다 해서 N+1문제)
//            }

//            컬렉션 페치조인
            String query = "select distinct t From Team t join fetch t.members";
            List<Team> result = em.createQuery(query, Team.class).getResultList();
            for (Team team : result) {
                System.out.println("team = " + team.getName()+" | "+team.getMembers().size());
                for( Member member:team.getMembers()){
                    System.out.println("-> member = " + member);
                }
            }
            //SPQL의 DISTINCT는 sql에 DISTINCT를 추가하는 역활과 애플리케이션에서 엔티티 중복을 제거한
            //모든것을 페치 조인으로 해결할 수는 없음
            // 객체그래프를 유지할떄 효과적
            // 여러테이블을 조인해서 전혀다른 결과를 내야하면 필요데이터만 조회해서 DTO로 반환하는것이 효과


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
