package com.example.mihwar.DTO;

import com.example.mihwar.Enum.NodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NavigationPointDTO {


    private Long nodeId;
    private String name;
    private Double x;
    private Double y;
    private NodeType nodeType;
    private Long destinationId;
    private String destinationName;

}
