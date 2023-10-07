package com.ariseontech.joindesk.auth.domain;

import com.ariseontech.joindesk.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Data
@Table(indexes = {
        @Index(name = "JD_AUTH_P_IDX", columnList = "login"),
        @Index(name = "JD_AUTH_PR_IDX", columnList = "project")
})
public class AuthorityProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "login")
    private Login login;

    @NotNull
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "project")
    private Project project;

    @Enumerated(EnumType.STRING)
    private AuthorityCode authorityCode;
}
