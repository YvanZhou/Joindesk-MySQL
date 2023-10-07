package com.ariseontech.joindesk.bo;

import com.ariseontech.joindesk.issues.domain.Issue;
import com.ariseontech.joindesk.issues.domain.Label;
import com.ariseontech.joindesk.issues.domain.Version;
import com.ariseontech.joindesk.project.domain.Component;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class IssueSearchData {
    // IN OR NOT IN
    private String operatorType;
    private Set<Long> label = new HashSet<>();
    private Set<Long> version = new HashSet<>();
    private Set<Long> component = new HashSet<>();

    public void addData(String field, Long data) {
        switch (field) {
            case "label":
                label.add(data);
                break;
            case "version":
                version.add(data);
                break;
            case "component":
                component.add(data);
                break;
            default:
                break;
        }
    }

    public boolean isOperatorTypeIn() {
        return "IN".equals(operatorType);
    }

    public boolean hasLabel() {
        return label.size() > 0;
    }

    public boolean hasVersion() {
        return version.size() > 0;
    }

    public boolean hasComponent() {
        return component.size() > 0;
    }

    public boolean hasData() {
        return label.size() > 0 || version.size() > 0 || component.size() > 0;
    }

    public List<Issue> filterIssues(List<Issue> issues) {
        if (!CollectionUtils.isEmpty(issues) && hasData()) {
            Stream<Issue> stream = issues.stream();
            if (isOperatorTypeIn()) {
                if (hasLabel()) {
                    stream = stream.filter(i -> !CollectionUtils.isEmpty(i.getLabels()) && i.getLabels().stream().map(Label::getId).collect(Collectors.toSet()).containsAll(getLabel()));
                }

                if (hasVersion()) {
                    stream = stream.filter(i -> !CollectionUtils.isEmpty(i.getVersions()) && i.getVersions().stream().map(Version::getId).collect(Collectors.toSet()).containsAll(getVersion()));
                }

                if (hasComponent()) {
                    stream = stream.filter(i -> !CollectionUtils.isEmpty(i.getComponents()) && i.getComponents().stream().map(Component::getId).collect(Collectors.toSet()).containsAll(getComponent()));
                }
            } else {
                if (hasLabel()) {
                    stream = stream.filter(i -> CollectionUtils.isEmpty(i.getLabels()) || !i.getLabels().stream().map(Label::getId).collect(Collectors.toSet()).containsAll(getLabel()));
                }

                if (hasVersion()) {
                    stream = stream.filter(i -> CollectionUtils.isEmpty(i.getVersions()) || !i.getVersions().stream().map(Version::getId).collect(Collectors.toSet()).containsAll(getVersion()));
                }

                if (hasComponent()) {
                    stream = stream.filter(i -> CollectionUtils.isEmpty(i.getComponents()) || !i.getComponents().stream().map(Component::getId).collect(Collectors.toSet()).containsAll(getComponent()));
                }
            }

            return stream.collect(Collectors.toList());
        }

        return issues;
    }
}
