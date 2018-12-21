package group.ydq.model.dao.dm;

import group.ydq.model.entity.dm.DeclareRule;
import group.ydq.model.entity.dm.Project;
import group.ydq.model.entity.rbac.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Daylight
 * @date 2018/11/12 16:22
 */
public interface ProjectRepository extends JpaRepository<Project,Long>{
    Page<Project> findProjectsByLeader(Pageable pageable,User leader);

    Page<Project> findProjectsByManagerAndSubmitTrue(Pageable pageable,User manager);

    Page<Project> findProjectsByManagerAndSubmitTrueAndStateBetween(Pageable pageable,User manager,int state1,int state2);

    @Modifying
    @Transactional
    @Query("update Project as p set p.state=?2,p.updateTime=?3 where p.id=?1")
    void changeState(long id, int state, Date updateTime);

    boolean existsProjectById(long id);

    int countProjectsByEntrance(DeclareRule rule);

}
