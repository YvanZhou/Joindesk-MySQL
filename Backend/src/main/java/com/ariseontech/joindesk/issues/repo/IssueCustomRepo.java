package com.ariseontech.joindesk.issues.repo;

import com.ariseontech.joindesk.JsonUtil;
import com.ariseontech.joindesk.issues.domain.Issue;
import com.ariseontech.joindesk.issues.domain.IssueType;
import com.ariseontech.joindesk.project.domain.Project;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
@Log
public class IssueCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IssueRepo issueRepo;

    @Autowired
    private IssueTypeRepo issueTypeRepo;

    @Transactional
    public void createSeq(String seq, Long start) {
        String createSeqQuery = "CREATE SEQUENCE " + seq + ((null != start && start > 0) ? " START " + start : "");
        entityManager.createNativeQuery(createSeqQuery).executeUpdate();
        log.warning("creating sequence for " + seq + " => " + createSeqQuery);
    }

    @Transactional
    public void updateCustomDataByIssueType(Long id, String key, String data) {
//        String query = "update issue SET custom_data = jsonb_set(custom_data, '{" + key + "}', '" + data + "') where issue_type =" + id;
//        System.out.println(query);
//        entityManager.createNativeQuery(query).executeUpdate();

        Optional<IssueType> optional = issueTypeRepo.findById(id);
        if (optional.isPresent()) {
            Set<Issue> issues = issueRepo.findByIssueType(optional.get());
            if (!CollectionUtils.isEmpty(issues)) {
                issues.forEach(i -> {
                    String customData = i.getCustomData();
                    if (StringUtils.hasText(customData)) {
                        Map<String, String> map = JsonUtil.deserialize(customData, new TypeReference<>() {});
                        map.put(key, data != null ? data : "");

                        String query = "update issue SET custom_data = '" + JsonUtil.serialize(map) + "' where id =" + i.getId();
                        log.info(query);
                        entityManager.createNativeQuery(query).executeUpdate();
                    }
                });
            }
        }
    }

    @Transactional
    public void updateCustomDataByIssue(Long id, String key, String data) {
//        String query = "update issue SET custom_data = jsonb_set(custom_data, " + key + ", '" + data + "') where id =" + id;
//        System.out.println(query);
//        entityManager.createNativeQuery(query).executeUpdate();

        Optional<Issue> first = issueRepo.findById(id);
        if (first.isPresent()) {
            String customData = first.get().getCustomData();
            if (StringUtils.hasText(customData)) {
                Map<String, String> map = JsonUtil.deserialize(customData, new TypeReference<>() {});
                map.put(key, data != null ? data : "");

                String query = "update issue SET custom_data = '" + JsonUtil.serialize(map) + "' where id =" + id;
                log.info(query);
                entityManager.createNativeQuery(query).executeUpdate();
            }
        }
    }

    @Transactional
    public void deleteCustomFieldByKey(String key) {
        String query = "update issue SET custom_data = custom_data - '" + key + "'";
        System.out.println(query);
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Transactional
    public void deleteCustomFieldByKey(String key, Project project) {
        Set<Issue> issues = issueRepo.findByProject(project);
        if (!CollectionUtils.isEmpty(issues)) {
            issues.forEach(i -> {
                String customData = i.getCustomData();
                if (StringUtils.hasText(customData)) {
                    Map<String, String> map = JsonUtil.deserialize(customData, new TypeReference<>() {});
                    map.remove(key);

                    String query = "update issue SET custom_data = '" + JsonUtil.serialize(map) + "' where id =" + i.getId();
                    log.info(query);
                    entityManager.createNativeQuery(query).executeUpdate();
                }
            });
        }
    }
}
