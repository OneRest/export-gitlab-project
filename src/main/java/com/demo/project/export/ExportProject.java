
package com.demo.project.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.project.model.Project;
import com.demo.project.utils.EncodeUtils;
import com.demo.project.utils.FileUtils;
import com.demo.project.utils.RequestUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <p>
 * 步骤四个接口：1、获取用户所有的项目列表；2、导出操作；3、查看导出状态；4、下载导出状态成功的的项目（本例只是生成脚本命令，Linux执行命令导出）
 * GET /projects
 * POST /projects/:id/export
 * GET /projects/:id/export
 * GET /projects/:id/export/download
 * </p>
 * 
 * @author: weijiashang
 * @date: 2018年12月24日 下午3:24:33
 */
@RestController
@RequestMapping("/project")
@Slf4j
public class ExportProject {

    // TODO 用户token
    private static String PRIVATE_TOKEN = "XXXXXXXXX";

    // TODO 项目域名
    private static String DOMAIN = "http://XXXXXXXXX.com";

    // TODO 项目列表信息 -写入文件名路径
    private static String PROJECT_INFO = "F://expro_file//project-info";

    // TODO 项目导出状态-写入文件名路径
    private static String EXPROT_STATUS = "F://expro_file//exprot-status";

    // TODO 生成项目下载脚本文件名路径
    private static String DOWNLOAD_CURL = "F://expro_file//download-curl";

    // 导出成功状态
    private static String FINISHED = "finished";

    private static Long info_time = null;

    private static Long status_time = null;

    /**
     * @Description:生成项目下载脚本
     * @param:
     * @author: weijiashang
     * @date: 2018年12月10日 下午5:10:38
     */
    @RequestMapping("/download")
    public static void projectDownload() {
        List<Project> projects = getFileProjects(EXPROT_STATUS + status_time);// 获取导出状态文件
        Map<String, String> headers = new HashMap<>();
        headers.put("Private-Token", PRIVATE_TOKEN);
        Long time = System.currentTimeMillis();
        // 将导出操作成功的项目生成脚本命令
        projects.stream().filter(p -> FINISHED.equals(p.getExportStatus())).collect(Collectors.toList()).forEach(o -> {
            if(!StringUtils.isEmpty(o.getId())) {
                String url = DOMAIN + "/api/v4/projects/" + o.getId() + "/export/download";
                // Map map = RequestUtils.get(EncodeUtils.decode(url, "utf-8"), headers, Map.class);
                // log.debug("下载操作：" + map);
                // 生成脚本文件
                String content = "curl --header \"PRIVATE-TOKEN:" + PRIVATE_TOKEN
                        + "\" --remote-header-name --remote-name " + url;
                FileUtils.writeFile(DOWNLOAD_CURL + time, content, true);
            }
        });
    }

    /**
     * @Description:导出项目状态
     * @param:
     * @author: weijiashang
     * @date: 2018年12月10日 下午3:25:05
     */
    @RequestMapping("/export-status")
    public static List<Project> exportProjectStatus() {
        List<Project> projects = getFileProjects(PROJECT_INFO + info_time);// 获取项目信息状态文件
        Map<String, String> headers = new HashMap<>();
        headers.put("Private-Token", PRIVATE_TOKEN);
        List<Project> reulst = new ArrayList<>();
        status_time = System.currentTimeMillis();
        projects.forEach(o -> {
            if(!StringUtils.isEmpty(o.getId())) {
                String url = DOMAIN + "/api/v4/projects/" + o.getId() + "/export";
                Map map = RequestUtils.get(EncodeUtils.decode(url, "utf-8"), headers, Map.class);
                log.debug("导出状态：" + map);
                Integer id = Integer.parseInt(map.get("id").toString());
                String name = map.get("name").toString();
                String pth = map.get("path").toString();
                String name_with_namespace = map.get("name_with_namespace").toString();
                String exportStatus = map.get("export_status").toString();

                Project project = Project.builder().id(id).name(name).path(pth).nameWithNamespace(name_with_namespace)
                        .exportStatus(exportStatus).build();
                reulst.add(project);
                // 写入文件
                StringBuilder sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
                sb.append(map.get("id") + ",");
                sb.append(map.get("name") + ",");
                sb.append(map.get("path") + ",");
                sb.append(map.get("name_with_namespace").toString().replaceAll("\\s*", "") + ",");
                sb.append(map.get("export_status"));
                FileUtils.writeFile(EXPROT_STATUS + status_time, sb.toString(), true);
            }
        });
        return reulst;
    }

    /**
     * @Description:导出项目
     * @param:
     * @author: weijiashang
     * @date: 2018年12月10日 下午2:39:13
     */
    @RequestMapping("/export")
    public static void exportProject() {
        List<Project> projects = getFileProjects(PROJECT_INFO + info_time); // 获取项目信息状态文件
        Map<String, String> headers = new HashMap<>();
        headers.put("Private-Token", PRIVATE_TOKEN);
        projects.forEach(o -> {
            if(!StringUtils.isEmpty(o.getId())) {
                String url = DOMAIN + "/api/v4/projects/" + o.getId() + "/export";
                url = EncodeUtils.decode(url, "utf-8");
                Map clientPost = RequestUtils.post(url, headers, null, Map.class);
                log.debug("导出操作：" + clientPost);
            }
        });
    }

    /**
     * @Description:获取用户所有的项目列表
     * @param:
     * @author: weijiashang
     * @date: 2018年12月10日 下午1:57:40
     */
    @RequestMapping("/get-list")
    @SuppressWarnings("unchecked")
    public static List<Project> getProjectList() {
        String url = DOMAIN + "/api/v4/projects";
        Map<String, String> headers = new HashMap<>();
        headers.put("Private-Token", PRIVATE_TOKEN);
        List list = RequestUtils.get(EncodeUtils.decode(url, "utf-8"), headers, List.class);
        log.info("total:" + list.size());
        log.info("total:" + list);

        info_time = System.currentTimeMillis();
        List<Project> reulst = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = (Map<String, Object>)o;
            Integer id = StringUtils.isEmpty(map.get("id")) ? null : Integer.parseInt(map.get("id").toString());
            String name = StringUtils.isEmpty(map.get("name")) ? null : map.get("name").toString();
            String pth = StringUtils.isEmpty(map.get("path")) ? null : map.get("path").toString();
            String name_with_namespace = StringUtils.isEmpty(map.get("name_with_namespace")) ? null
                    : map.get("name_with_namespace").toString();

            Project project =
                    Project.builder().id(id).name(name).path(pth).nameWithNamespace(name_with_namespace).build();
            reulst.add(project);
            // 写入文件
            StringBuilder sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
            sb.append(map.get("id") + ",");
            sb.append(map.get("name") + ",");
            sb.append(map.get("path") + ",");
            sb.append(map.get("name_with_namespace").toString().replaceAll("\\s*", ""));
            FileUtils.writeFile(PROJECT_INFO + info_time, sb.toString(), true);
        });
        log.info("=========项目总数：" + reulst.size());
        return reulst;
    }

    /**
     * @Description:获取文件中的项目列表
     * @param: @param filePath
     * @param: @return
     * @author: weijiashang
     * @date: 2018年12月11日 上午11:40:17
     */
    private static List<Project> getFileProjects(String filePath) {
        List<String> strList = FileUtils.readFileIntoStringArrList(filePath);
        List<Project> reulst = new ArrayList<>();
        strList.forEach(s -> {
            String[] str = s.split(",");
            if(!StringUtils.isEmpty(str[0])) {
                if(str.length == 4) {
                    // 信息文件
                    Project project = Project.builder().id(Integer.parseInt(str[0])).name(str[1]).path(str[2])
                            .nameWithNamespace(str[3]).build();
                    reulst.add(project);
                } else {
                    // 状态文件
                    Project project = Project.builder().id(Integer.parseInt(str[0])).name(str[1]).path(str[2])
                            .nameWithNamespace(str[3]).exportStatus(str[4]).build();
                    reulst.add(project);
                }
            }
        });
        return reulst;
    }
}
