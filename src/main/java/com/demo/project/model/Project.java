
package com.demo.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Integer id;

    private String name;

    private String nameWithNamespace;

    private String path;

    private String exportStatus;

}
