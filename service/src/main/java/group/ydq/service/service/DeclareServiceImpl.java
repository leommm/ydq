package group.ydq.service.service;

import group.ydq.model.dao.dm.ExpertRepository;
import group.ydq.model.dao.dm.ProjectRepository;
import group.ydq.model.dao.rbac.UserRepository;
import group.ydq.model.entity.dm.Project;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Daylight
 * @date 2018/11/12 16:32
 */
@Service("declareService")
public class DeclareServiceImpl extends BaseServiceImpl implements DeclareService {
    @Resource
    private UserRepository userDao;
    @Resource
    private ProjectRepository projectDao;
    @Resource
    private ExpertRepository expertDao;
    /**
     * 项目保存操作
     * @param project
     */
    @Override
    public void save(Project project) {
        project.setSubmit(false);
        projectDao.save(project);
    }

    /**
     * 项目提交操作
     * @param project
     */
    @Override
    public void submit(Project project) {
        project.setSubmit(true);
        projectDao.save(project);
    }

    /**
     * 查询项目详情
     * @param projectId
     * @return
     */
    @Override
    public Project getProject(long projectId) {
        return projectDao.getOne(projectId);
    }

    @Override
    public void distributeExpert(long projectId, List<Long> expertIds) {
        projectDao.getOne(projectId).setExperts(userDao.findUsersByIdIn(expertIds));
    }



}
