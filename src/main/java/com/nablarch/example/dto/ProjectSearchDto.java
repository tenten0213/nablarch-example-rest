package com.nablarch.example.dto;

import java.io.Serializable;

/**
 * プロジェクト検索のDto
 *
 * @author Nabu Rakutaro
 */
public class ProjectSearchDto implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** プロジェクト名 */
    private String projectName;

    /**
     * プロジェクト名を返却する。
     *
     * @return プロジェクト名
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * プロジェクト名を設定する。
     *
     * @param projectName プロジェクト名
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
