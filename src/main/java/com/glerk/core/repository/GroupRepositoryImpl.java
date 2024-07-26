package com.glerk.core.repository;

import com.glerk.core.entity.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class GroupRepositoryImpl implements GroupRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Group> findByGroupIdOrTemplateNameAndUserId(Long groupId, String templateName, Long userId) {
        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT g FROM Group g LEFT JOIN FETCH g.templates t WHERE g.createdBy = :userId");

        if (groupId != null && groupId != 0) {
            queryBuilder.append(" AND g.id = :groupId");
        }

        if (StringUtils.hasText(templateName)) {
            queryBuilder.append(" AND t.name LIKE :templateName");
        }

        queryBuilder.append(" ORDER BY g.createdAt, t.createdAt");

        TypedQuery<Group> query = em.createQuery(queryBuilder.toString(), Group.class);
        query.setParameter("userId", userId);

        if (groupId != null && groupId != 0) {
            query.setParameter("groupId", groupId);
        }

        if (StringUtils.hasText(templateName)) {
            query.setParameter("templateName", "%" + templateName + "%");
        }

        return query.getResultList();
    }
}
