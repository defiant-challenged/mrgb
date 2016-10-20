/**
 * 
 */
package nl.defsoftware.mrgb.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import javafx.beans.property.DoubleProperty;
import nl.defsoftware.mrgb.models.Rib;
import nl.defsoftware.mrgb.view.controllers.MatchingScoreEntry;
import nl.defsoftware.mrgb.view.models.IGraphViewModel;

/**
 * This GraphHandler object is responsible for filling the graph model from
 * parsed data with the nodes and placing them in a local coordinate system.
 * 
 * @author D.L. Ettema
 *
 */
public class GraphHandler {

    private static final Logger log = LoggerFactory.getLogger(GraphHandler.class);

    private static final int HOR_NODE_SPACING = 20;
    private static final int VER_NODE_SPACING = 25;
    private static final int VER_NODE_BASELINE = 200;

    private static final int BACKBONE_X_BASELINE = 200;
    private static final int BACKBONE_Y_BASELINE = 200;

    private static final int Y_CORRECTION = 5;

    private Int2ObjectLinkedOpenHashMap<Rib> graphData = new Int2ObjectLinkedOpenHashMap<>();
    private Short2ObjectOpenHashMap<String> genomeNamesMap = new Short2ObjectOpenHashMap<>();

    private Int2ObjectOpenHashMap<int[]> edgeQueue = new Int2ObjectOpenHashMap<>();
    
    /**
     * Given the two datastructures this Handler will input these into the model
     * and the model will determine the layout
     * 
     * @param graphMap
     * @param genomeNamesMap
     */
    public GraphHandler(Int2ObjectLinkedOpenHashMap<Rib> graphData, Short2ObjectOpenHashMap<String> genomeNamesMap) {
        this.graphData = graphData;
        this.genomeNamesMap = genomeNamesMap;
        
    }

    /**
     * This will fill the graph based on the graphmap.
     * 
     * @param model
     * @param graphMap
     * @param genomeNamesMap
     */
    public void loadAlternateGraphViewModel(IGraphViewModel model, DoubleProperty scaleYProperty) {

        int sourceNode = graphData.firstIntKey();
        // int sourceNode = 344;
        Rib firstRib = graphData.get(sourceNode);

        GraphHandlerUtil.addEdgesToQueue(edgeQueue, firstRib.getNodeId(), firstRib.getConnectedEdges());
        drawSequence(model, firstRib, BACKBONE_X_BASELINE, BACKBONE_Y_BASELINE, 0, scaleYProperty);

        // for (int i = (sourceNode + 1); i < 500; i++) { //testing purposes
        for (int i = (sourceNode + 1); i < graphData.size(); i++) {
            if (graphData.containsKey(i)) {
                Rib aRib = graphData.get(i);
                GraphHandlerUtil.addEdgesToQueue(edgeQueue, aRib.getNodeId(), aRib.getConnectedEdges());
                drawEdgesAndNodesToParents(model, aRib, scaleYProperty);
            }
        }
    }

    /**
     * <p>
     * This method will determine any parent nodes previously stored in the
     * 'addEdgesToQueue' in a previous iteration and draw these edges with
     * respect to the location of the parent node and current node
     * 
     * insertion: 5-6 = 4; 5-7 = 5 ; 6-7 = 4;
     * </p>
     * 
     * @param model
     *            the data model to be filled
     * @param graphMap
     *            map containing all the nodes in the graph
     * @param aRib
     *            current node
     */
    private void drawEdgesAndNodesToParents(IGraphViewModel model, Rib aRib, DoubleProperty scaleYProperty) {
        if (edgeQueue.containsKey(aRib.getNodeId())) {
            int[] parentNodes = edgeQueue.get(aRib.getNodeId());
            List<MatchingScoreEntry> matchedGenomeRanking = GraphHandlerUtil.determineSortedNodeRanking(aRib,
                    parentNodes, graphData);

            if (matchedGenomeRanking.size() == 1) {
                int rank = 0;
                Rib parentRib = matchedGenomeRanking.get(rank).getParentRib();
                drawSequence(model, aRib, parentRib.getXCoordinate(), parentRib.getYCoordinate(), rank, scaleYProperty);
                model.addEdge(aRib.getNodeId(), parentRib.getNodeId(), rank);
            } else {
                // if the matching produces an equal score, they will end up on
                // a different rank. We must determine the true rank based on:
                // 1. is a score equal to a score in a higher rank
                // 2. find the one with the highest node id closest to the
                // aRib.nodeId and draw from those coordinates

                int highestYCoord = 0;
                int xCoord = 0;
                int nodeRank = 0;
                for (int rank = 0; rank < matchedGenomeRanking.size(); rank++) {
                    MatchingScoreEntry entry = matchedGenomeRanking.get(rank);
                    // Find the first occurrence of aRib on its main axis by
                    // aligning it up with the parent that has the highest rank
                    // with this aRib.
                    if (entry.getChildNodeId() == aRib.getNodeId() && xCoord == 0) {
                        xCoord = entry.getParentRib().getXCoordinate();
                        nodeRank = rank;
                    }

                    // Find highest y coordinate from this child's parents so
                    // its drawn at an adequate Y distance.
                    if (highestYCoord < entry.getParentRib().getYCoordinate()) {
                        highestYCoord = entry.getParentRib().getYCoordinate();
                    }
                }
                drawSequence(model, aRib, xCoord, highestYCoord, nodeRank, scaleYProperty);

                // draw all the edges to this aRib
                matchedGenomeRanking = GraphHandlerUtil.determineSortedEdgeRanking(aRib, parentNodes, graphData);
                for (int rank = 0; rank < matchedGenomeRanking.size(); rank++) {
                    MatchingScoreEntry entry = matchedGenomeRanking.get(rank);
                    if (entry.getChildNodeId() == aRib.getNodeId()) {
                        model.addEdge(aRib.getNodeId(), entry.getParentRib().getNodeId(), rank);
                    }
                }
                matchedGenomeRanking = null;
            }

        } else {
            log.info("Rib({}) has no parent edges, new parent", aRib.getNodeId());
            // contains a node with no parent which means it is a strain that
            // starts at this position.
            // TODO: not yet implemented
        }
    }

    private void drawSequence(IGraphViewModel model, Rib aRib, int parentXCoordinate, int parentYCoordinate, int rank, DoubleProperty scaleYProperty) {
        int xCoordinate = parentXCoordinate + (HOR_NODE_SPACING * rank);
        int yCoordinate = parentYCoordinate + VER_NODE_SPACING;

        aRib.setCoordinates(xCoordinate, yCoordinate);
        model.addSequence(aRib, rank, scaleYProperty);
        // model.addLabel(Integer.toString(aRib.getNodeId()), xCoordinate + 10,
        // yCoordinate, 0);
    }

    private void drawEdge(IGraphViewModel model, Rib aRib, int parentXCoordinate, int parentYCoordinate, int rank) {
        int startX = parentXCoordinate;
        int startY = parentYCoordinate + Y_CORRECTION;

        int endX = aRib.getXCoordinate();
        int endY = aRib.getYCoordinate() + Y_CORRECTION;

        model.addEdge(aRib.getNodeId(), startX, startY, endX, endY, rank);
        model.addLabel(Integer.toString(aRib.getNodeId()), endX, endY, 2);
    }
}
