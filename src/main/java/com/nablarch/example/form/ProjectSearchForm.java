package com.nablarch.example.form;

import nablarch.core.validation.ee.Domain;

import java.io.Serializable;

/**
 * プロジェクト検索フォーム
 *
 * @author Nabu Rakutaro
 */
public class ProjectSearchForm implements Serializable {
    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** プロジェクト名 */
    @Domain("projectName")
    private String projectName;

    /**
     * プロジェクト名を取得する。
     * @return プロジェクト名
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * プロジェクト名を設定する。
     * @param projectName プロジェクト名
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
