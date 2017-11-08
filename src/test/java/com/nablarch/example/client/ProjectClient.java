package com.nablarch.example.client;

import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.nablarch.example.CustomFormUrlEncodedProvider;
import com.nablarch.example.entity.Project;
import com.nablarch.example.form.ProjectForm;
import com.nablarch.example.form.ProjectUpdateForm;
import com.nablarch.example.util.MultivaluedMapUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class ProjectClient {

    private static final String targetUrl = "http://localhost:9080/projects";

    /**
     * プロジェクト情報の文字列変換を行う。
     *
     * @param args 引数
     */
    public static void main(String[] args) throws Exception {

        // 検索
        // 指定条件検索
        System.out.println(makeDataString(getProject("projectName", "プロジェクト００１")));

        // 登録
        ProjectForm projectForm = createInsertProject();
        System.out.println("insert status:" + postProject(projectForm));
        Project project = getProject("projectName", "プロジェクト５５５");
        System.out.println(makeDataString(project));

        // 更新対象プロジェクト取得
        Project updateProject = getProject("projectName", "プロジェクト５５５");
        ProjectUpdateForm projectUpdateForm = setUpdateProject(updateProject);

        // 更新
        System.out.println("update status:" + putProject(projectUpdateForm));
        Project updatedProject = getProject("projectName", "プロジェクト５５５");
        System.out.println(makeDataString(updatedProject));
    }

    /**
     * 登録用プロジェクト情報生成
     *
     * @return 登録用プロジェクト情報
     */
    private static ProjectForm createInsertProject() {
        ProjectForm form = new ProjectForm();
        form.setProjectName("プロジェクト５５５");
        form.setProjectType("development");
        form.setProjectClass("s");
        return form;
    }

    /**
     * 更新用プロジェクト情報設定
     *
     * @param project 更新対象プロジェクト情報
     * @return 更新用プロジェクト情報
     */
    private static ProjectUpdateForm setUpdateProject(Project project) {
        ProjectUpdateForm form = new ProjectUpdateForm();
        form.setProjectName(project.getProjectName());
        form.setProjectType("development");
        form.setProjectClass("a");
        return form;
    }

    /**
     * HTTP GETメソッドを使用したクライアント操作を行う。
     *
     * @return プロジェクト情報リスト
     */
    //private static MultivaluedMap<String, String> getProject() {
    private static Project getProject() {
        return new ResteasyClientBuilder()
                .establishConnectionTimeout(15, TimeUnit.SECONDS)
                .socketTimeout(10, TimeUnit.SECONDS)
                .build()
                .register(new CustomFormUrlEncodedProvider())
                .target(targetUrl)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                // .get(new GenericType<MultivaluedMap<String, String>>() {
                .get(new GenericType<Project>() {
                });
    }

    /**
     * HTTP GETメソッドを使用したクライアント操作を行う。
     *
     * @param key   query paramのキー
     * @param value query paramの値
     * @return プロジェクト情報リスト
     */
    private static Project getProject(String key, Object value) {

        return new ResteasyClientBuilder()
                .establishConnectionTimeout(15, TimeUnit.SECONDS)
                .socketTimeout(10, TimeUnit.SECONDS)
                .build()
                .target(targetUrl)
                .queryParam(key, value)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .get(new GenericType<Project>() {
                });
    }

    /**
     * HTTP POSTメソッドを使用したクライアント操作を行う。
     *
     * @param project 更新用プロジェクト情報
     * @return ステータスコード
     */
    private static Integer postProject(ProjectForm project) {
        return new ResteasyClientBuilder()
                .establishConnectionTimeout(15, TimeUnit.SECONDS)
                .socketTimeout(10, TimeUnit.SECONDS)
                .build()
                .target(targetUrl)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .post(Entity.form(MultivaluedMapUtil.convertMultiValuedMap(project))).getStatus();
    }

    /**
     * HTTP PUTメソッドを使用したクライアント操作を行う。
     *
     * @param project 更新用プロジェクト情報
     * @return ステータスコード
     */
    private static Integer putProject(ProjectUpdateForm project) {
        return new ResteasyClientBuilder()
                .establishConnectionTimeout(15, TimeUnit.SECONDS)
                .socketTimeout(10, TimeUnit.SECONDS)
                .build()
                .target(targetUrl)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(project)).getStatus();
    }

    /**
     * プロジェクト情報の文字列変換を行う。
     *
     * @param project プロジェクト情報
     * @return プロジェクト情報
     */
    private static String makeDataString(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Project(ProjectName: %s, ProjectType: %s, ProjectClass: %s",
                project.getProjectName(), project.getProjectType(),
                project.getProjectClass()));
        return sb.toString();
    }
}
