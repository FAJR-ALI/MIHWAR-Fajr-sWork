package com.example.mihwar.Service;

import com.example.mihwar.Model.MapNode;
import com.example.mihwar.Repository.MapNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapNodeService {


    private final MapNodeRepository mapNodeRepository;

    // Get all map nodes
    public List<MapNode> getAllMapNodes() {
        return mapNodeRepository.findAll();
    }

    // Get map node by ID
    public MapNode getMapNodeById(Long id) {
        return mapNodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map node not found"));
    }

    // Add map node
    public MapNode addMapNode(MapNode mapNode) {
        return mapNodeRepository.save(mapNode);
    }

    // Update map node
    public MapNode updateMapNode(Long id, MapNode mapNode) {

        MapNode oldMapNode = mapNodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map node not found"));

        oldMapNode.setDestination(mapNode.getDestination());
        oldMapNode.setName(mapNode.getName());
        oldMapNode.setLatitude(mapNode.getLatitude());
        oldMapNode.setLongitude(mapNode.getLongitude());
        oldMapNode.setNodeType(mapNode.getNodeType());

        return mapNodeRepository.save(oldMapNode);
    }

    // Delete map node
    public void deleteMapNode(Long id) {

        MapNode mapNode = mapNodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Map node not found"));

        mapNodeRepository.delete(mapNode);
    }
}
