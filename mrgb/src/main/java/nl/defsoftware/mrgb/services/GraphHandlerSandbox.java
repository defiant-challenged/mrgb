package nl.defsoftware.mrgb.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import nl.defsoftware.mrgb.view.models.GraphModel;
import nl.defsoftware.mrgb.view.models.IGraphViewModel;
import nl.defsoftware.mrgb.view.models.Sequence;

/**
 * @author D.L. Ettema
 *
 */
public class GraphHandlerSandbox {
    
    private static final int HOR_NODE_SPACING = 20;
    private static final int VER_NODE_SPACING = 25;
    private static final int VER_NODE_BASELINE = 200;
    
    /**
     * Layout algorithm for the graphMap data to be set on the model.
     * 
     * @param model
     * @param graphMap
     */
    public void setGraphViewModel(IGraphViewModel model, Map<Integer, int[]> graphMap) {
        int HORIZONTAL_NODE_CURSOR = 1;
        for (Integer fromKey : graphMap.keySet()) {
            int VERTICAL_NODE_CURSOR = 1;// reset after each new key
            int xCoord = HORIZONTAL_NODE_CURSOR * HOR_NODE_SPACING;
            int yCoord = VER_NODE_BASELINE;

            // @TODO do check if node is already placed and which coords
            // Sequence prevPlacedSequence = model.findSequenceById(fromKey);
            // if (prevPlacedSequence != null) {
            // // set new y baseline
            // yCoord = (int) prevPlacedSequence.getCenterY();
            // } else {
            // model.addSequence(fromKey, xCoord, yCoord);
            // }
            model.addSequence(fromKey, xCoord, yCoord);
            HORIZONTAL_NODE_CURSOR++;

            int prevFollowUpNodeId = fromKey.intValue();
            // @TODO do topological sort first
            int[] connectedSequences = graphMap.get(fromKey);
            for (int i = 0; i < connectedSequences.length; i++) {
                // Sequence prevPlacedSequence =
                // model.findSequenceById(connectedSequences[i]);
                // if (prevPlacedSequence != null) {
                // int avgYCoord =
                // calculateAverageYCoordinate(prevPlacedSequence.getSequenceParents());
                // prevPlacedSequence.setCenterY(avgYCoord);
                //
                // } else {
                if (prevFollowUpNodeId + 1 == connectedSequences[i]) {
                    if (i == 0) { // backbone vertical position
                        xCoord = HORIZONTAL_NODE_CURSOR * HOR_NODE_SPACING;
                        HORIZONTAL_NODE_CURSOR++;
                    }
                    yCoord = VER_NODE_BASELINE + (VERTICAL_NODE_CURSOR * VER_NODE_SPACING);
                    model.addSequence(connectedSequences[i], xCoord, yCoord);
                    VERTICAL_NODE_CURSOR++;
                    prevFollowUpNodeId = connectedSequences[i];
                } else {
                    xCoord = (HORIZONTAL_NODE_CURSOR * HOR_NODE_SPACING);
                    yCoord = VER_NODE_BASELINE;
                    model.addSequence(connectedSequences[i], xCoord, yCoord);
                    HORIZONTAL_NODE_CURSOR++;
                }
                // add edge @TODO add weight and know if we need to go around
                // other
                // nodes when drawing
                model.addEdge(fromKey, connectedSequences[i], 0);
            }
            // }
        }
    }

    /**
     * @param sequenceParents
     * @return int
     */
    private int calculateAverageYCoordinate(List<Sequence> sequenceParents) {
        BigDecimal avgYCoord = new BigDecimal(0);
        for (Sequence sequence : sequenceParents) {
            avgYCoord.add(new BigDecimal(sequence.getCenterY()));
        }
        return avgYCoord.divideToIntegralValue(BigDecimal.valueOf(sequenceParents.size())).intValue();
    }

    private void addEdges(int[] toNodes, IGraphViewModel model, Integer from) {
        for (int i = 0; i < toNodes.length; i++) {
            model.addEdge(from, toNodes[i], 0);
        }
    }
}
