package com.zynap.talentstudio.web.utils.tree;

import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.ArtefactAssociationHelper;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.users.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: jsueiras
 * Date: 15-Feb-2005
 * Time: 17:06:30
 */
public class TreeBuilderHelper {

    public static List<Branch> buildOrgUnitTree(List<? extends Node> results) {
        return buildTree(results, ORG_UNIT_TYPE, null);
    }

    public static List<Branch> buildOrgUnitTree(List<? extends Node> results, Map<String, Branch> treeBranches) {
        return buildTree(results, ORG_UNIT_TYPE, treeBranches);
    }

    public static List buildPositionsTree(List<? extends Node> results) {
        return buildTree(results, POSITION_TYPE, null);
    }

    public static List<Branch> buildSubjectTree(List<? extends Node> results) {
        return buildTree(results, SUBJECT_TYPE, null);
    }

    private static List<Branch> buildTree(List<? extends Node> results, int type, Map<String, Branch> branches) {
        ArrayList<Branch> tracker = new ArrayList<Branch>();
        for (Iterator it = results.iterator(); it.hasNext();) {
            Branch node = createOrgUnitContainer((OrganisationUnit) it.next(), type);
            if (branches != null) branches.put(node.getId(), node);
            if (tracker.isEmpty()) {
                // we have a top level set this position to have level 0
                tracker.add(node);
            } else {  // it is not empty we now need to see if it is a top level or a child of one of the list
                boolean found = connectTree(node, tracker);
                if (!found) {
                    tracker.add(node);
                }
            }
        }
        return tracker;
    }

    private static Branch createOrgUnitContainer(OrganisationUnit node, int treeType) {
        Branch branch = createOrgUnitContainer(node);
        switch (treeType) {
            case ORG_UNIT_TYPE:
                break;
            case POSITION_TYPE:
                List<Node> positions = new ArrayList<Node>(node.getPositions());
                sortNodes(positions);
                for (Node p : positions) {
                    if (p.isActive()) branch.addLeaf(createPositionAssociationLeaf((Position) p, true));
                }
                break;
            case SUBJECT_TYPE:
                List<ArtefactAssociation> allAssociations = buildSubjectAssociations(node.getPositions());
                createSubjectAssociationLeaves(branch, allAssociations);
                break;
        }
        return branch;
    }

    private static void createSubjectAssociationLeaves(Branch branch, List<ArtefactAssociation> allAssociations) {
        for (ArtefactAssociation assoc : allAssociations) {
            SubjectAssociation subjectAssociation = (SubjectAssociation) assoc;
            if (subjectAssociation.getSubject().isActive()) {
                branch.addLeaf(createSubjectAssociationLeaf(subjectAssociation));
            }
        }
    }

    private static void sortAssociations(List<ArtefactAssociation> allAssociations) {
        Collections.sort(allAssociations, new Comparator<ArtefactAssociation>() {
            public int compare(ArtefactAssociation o1, ArtefactAssociation o2) {
                return o1.getSource().getLabel().compareTo(o2.getSource().getLabel());
            }
        });
    }

    private static List<ArtefactAssociation> buildSubjectAssociations(Collection<Position> positions) {
        List<ArtefactAssociation> allAssociations = new ArrayList<ArtefactAssociation>();
        for (Position p : positions) {
            if (p.isActive()) {
                allAssociations.addAll(ArtefactAssociationHelper.getPrimaryAssociations(p.getSubjectAssociations()));
            }
        }
        sortAssociations(allAssociations);
        return allAssociations;
    }

    private static void sortNodes(List<Node> nodes) {
        Collections.sort(nodes, new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
    }

    private static Branch createOrgUnitContainer(OrganisationUnit node) {
        Branch container = createBranch(node);
        container.setParentId(node.getParent() != null ? node.getParent().getId().toString() : null);
        return container;
    }

    public static void appendPositionLeaves(Branch branch, Collection<Position> positions, boolean displayAssociation) {
        List<Node> nodes = new ArrayList<Node>(positions);
        sortNodes(nodes);
        for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
            Position position = (Position) iterator.next();
            if (position.isActive())
                branch.addLeaf(createPositionAssociationLeaf(position, displayAssociation));
        }
    }

    public static void appendSubjectFromPositionLeaves(Branch branch, Collection<Position> positions) {
        List<ArtefactAssociation> allAssociations = buildSubjectAssociations(positions);
        createSubjectAssociationLeaves(branch, allAssociations);
    }

    public static void appendSubjectLeavesFromAreaElements(Branch branch, Collection<AreaElement> assignedSubjects) {
        List<Node> areaNodes = buildNodesFromAreaElement(assignedSubjects);
        sortNodes(areaNodes);
        for (Node node : areaNodes) {
            branch.addLeaf(createLeaf(node));
        }
    }

    private static List<Node> buildNodesFromAreaElement(Collection<AreaElement> assignedSubjects) {
        List<Node> areaNodes = new ArrayList<Node>();
        for (AreaElement element : assignedSubjects) {
            areaNodes.add(element.getNode());
        }
        return areaNodes;
    }

    public static void appendUserLeaves(Branch usersBranch, List<UserDTO> systemUsers) {
        if (systemUsers.isEmpty()) return;
        Collections.sort(systemUsers);
        for (UserDTO user : systemUsers) {
            final Leaf leaf = new Leaf(user.getId().toString(), user.getDisplayLabel());
            leaf.setHasAccess(true);
            leaf.setActive(true);
            usersBranch.addLeaf(leaf);
        }
    }

    public static void appendSubjectUserLeavesFromAreaElements(Branch branch, Collection<AreaElement> assignedSubjects) {
        List<Node> areaNodes = buildNodesFromAreaElement(assignedSubjects);
        for (Node node : areaNodes) {
            Subject subject = (Subject) node;
            if (subject.getUser() != null) {
                branch.addLeaf(createLeafSubjectUser(subject));
            }
        }
    }

    public static void appendSubjectUserFromPositionLeaves(Branch branch, Set<Position> positions) {

        for (Iterator iterator = positions.iterator(); iterator.hasNext();) {
            Position position = (Position) iterator.next();

            final Collection subjectAssoc = ArtefactAssociationHelper.getPrimaryAssociations(position.getSubjectAssociations());
            for (Iterator iterator1 = subjectAssoc.iterator(); iterator1.hasNext();) {
                SubjectAssociation subjectAssociation = (SubjectAssociation) iterator1.next();
                if (subjectAssociation.getSubject().getUser() != null) {
                    branch.addLeaf(createSubjectUserAssociationLeaf(subjectAssociation));
                }
            }
        }
    }

    private static boolean connectTree(final Branch node, final List<Branch> roots) {

        final String targetId = node.getParentId();
        boolean result = false;
        for (Iterator iterator = roots.iterator(); iterator.hasNext();) {
            Branch parent = (Branch) iterator.next();
            result = parent.build(new ITreeBuilder() {
                public boolean build(Branch container) {
                    // then this container is a parent
                    if (container.getId().equals(targetId)) {
                        container.addChild(node);
                        // sort children
                        Collections.sort(container.getChildren(), new Comparator<ITreeContainer>() {
                            public int compare(ITreeContainer o1, ITreeContainer o2) {
                                return o1.getLabel().compareTo(o2.getLabel());
                            }
                        });
                        return true;
                    }
                    return false;
                }
            });
            if (result) return result;
        }
        return result;
    }

    private static Leaf createLeafSubjectUser(Subject subject) {
        Leaf leaf = new Leaf();
        initContainer(leaf, subject);
        leaf.setId(subject.getUser().getId().toString());
        return leaf;
    }

    private static Leaf createSubjectUserAssociationLeaf(SubjectAssociation subjectAssociation) {
        Leaf leaf = createLeafSubjectUser(subjectAssociation.getSubject());
        leaf.setLabel(leaf.getLabel() + " - " + subjectAssociation.getPosition().getLabel());
        return leaf;
    }

    private static Leaf createSubjectAssociationLeaf(SubjectAssociation subjectAssociation) {
        Leaf leaf = createLeaf(subjectAssociation.getSubject());
        leaf.setLabel(leaf.getLabel() + " - " + subjectAssociation.getPosition().getLabel());
        return leaf;
    }

    private static Branch createBranch(Node node) {
        Branch container = new Branch();
        initContainer(container, node);
        return container;
    }

    private static Leaf createLeaf(Node node) {
        Leaf leaf = new Leaf();
        initContainer(leaf, node);
        return leaf;
    }

    private static Leaf createPositionAssociationLeaf(Position node, boolean displayAssociation) {
        Leaf leaf = createLeaf(node);
        if (displayAssociation) {
            final Collection<ArtefactAssociation> primaryAssociations = ArtefactAssociationHelper.getPrimaryAssociations(node.getSubjectAssociations());
            StringBuilder leafLabel = new StringBuilder();
            int index = primaryAssociations.size();
            if (!primaryAssociations.isEmpty()) leafLabel.append(" ( ");
            for (ArtefactAssociation association : primaryAssociations) {
                leafLabel.append(((SubjectAssociation) association).getSubject().getLabel());
                index--;
                if (index > 0) leafLabel.append(", ");
            }
            if (leafLabel.length() > 0) {
                leafLabel.append(" )");
            }
            leaf.setLabel(node.getLabel() + leafLabel.toString());
        }
        return leaf;
    }

    private static void initContainer(Leaf leaf, Node node) {
        leaf.setLabel(node.getLabel());
        leaf.setId(node.getId().toString());
        leaf.setActive(node.isActive());
        leaf.setHasAccess(node.isHasAccess());
    }

    private static final int ORG_UNIT_TYPE = 0;
    private static final int POSITION_TYPE = 1;
    private static final int SUBJECT_TYPE = 2;
}
