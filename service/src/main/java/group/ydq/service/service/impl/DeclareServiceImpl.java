package group.ydq.service.service.impl;

import group.ydq.model.dao.dm.DeclareRuleRepository;
import group.ydq.model.dao.dm.ExpertRepository;
import group.ydq.model.dao.dm.ProjectRepository;
import group.ydq.model.dao.rbac.UserRepository;
import group.ydq.model.entity.dm.DeclareRule;
import group.ydq.model.entity.dm.ExpertReview;
import group.ydq.model.entity.dm.Project;
import group.ydq.service.service.DeclareService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private DeclareRuleRepository ruleRepository;


    @Override
    public void distributeExpert(long projectId, List<Long> expertIds) {
        Project project=projectDao.getOne(projectId);
        project.setExperts(userDao.findUsersByIdIn(expertIds));
        projectDao.save(project);
        for (Long id:expertIds) {
            ExpertReview expertReview = new ExpertReview();
            expertReview.setExpert(userDao.getOne(id));
            expertReview.setProject(project);
            expertDao.save(expertReview);
        }
    }

    @Override
    public List<DeclareRule> getValuableRules() {
        return ruleRepository.findDeclareRulesByEndTimeAfter(new Date());
    }

    @Override
    public Page<DeclareRule> getAllRules(int page,int limit) {
        Pageable pageable= PageRequest.of(page-1, limit);
        return ruleRepository.findAll(pageable);
    }

    @Override
    public DeclareRule getRule(long id) {
        return ruleRepository.getOne(id);
    }

    @Override
    public void addRule(DeclareRule rule) {
        ruleRepository.save(rule);
    }

    @Override
    public int getProjectNumOfRule(long ruleId) {
        return projectDao.countProjectsByEntrance(ruleRepository.getOne(ruleId));
    }

    @Override
    public void delRule(long id) {
        if (ruleRepository.existsById(id))
            ruleRepository.deleteById(id);
    }
}
