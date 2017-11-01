package com.nablarch.example.form;

import java.io.Serializable;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.Required;

/**
 * プロジェクト登録フォーム。
 * @author Nabu Rakutaro
 */
public class ProjectForm implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** プロジェクト名 */
    @Required
    @Domain("projectName")
    private String projectName;

    /** プロジェクト種別 */
    @Required
    @Domain("projectType")
    private String projectType;

    /** プロジェクト分類 */
    @Required
    @Domain("projectClass")
    private String projectClass;

    /**
     * プロジェクト名を取得する。
     *
     * @return プロジェクト名
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * プロジェクト種別を取得する。
     *
     * @return プロジェクト種別
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * プロジェクト分類を取得する。
     *
     * @return プロジェクト分類
     */
    public String getProjectClass() {
        return projectClass;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }
}
