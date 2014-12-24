var dView = new dTree('dView');
var nodeId;
var openNodes = [];
var dModel;
var type;
var POS_LEAF_ADD = 'pos_leaf_add';
var OU_WITH_POS_LEAF_LINK = 'ou_pos_leaf_link';
var OU_LEAF = 'ou_leaf';

var ouTreeBeanCallback = function(orgUnits) {
    dView.add(-1, -2, '', '');
    traverse(orgUnits[0], -1, type);
    dModel = deepClone(dView);
    document.getElementById('showTree').innerHTML = dView;
    for (var i = 0; i < dView.aNodes.length; i++) {
        if (dView.aNodes[i].id == nodeId) {
            dView.openTo(dView.aNodes[i].pid);
            document.getElementById("sdView" + i).style.backgroundColor="#66ff99";
            break;
        }
    }
}

var posLeafBeanCallback = function(selectedOrgUnits) {
    dView = deepClone(dModel);
    openNodes[openNodes.length] = selectedOrgUnits[0].id;
    var positions = selectedOrgUnits[0].leaves;
    for (var i = 0; i < positions.length; i++) {
        var positionHref = 'javascript:popupServerOK(\'chartTree\',\''
                + positions[i].label + '\',\'' + positions[i].id + '\');';
        dView.add(positions[i].id, positions[i].parent.id,
                positions[i].label, positionHref, '', '', dView.icon.position);
    }
    dModel = deepClone(dView);
    document.getElementById('showTree').innerHTML = dView;
    //open all nodes with positions
    //todo: only open the open nodes, as some may have been closed again
    for (var i = 0; i < openNodes.length; i++) {
        dView.openTo(openNodes[i]);
    }
}

function getOrgUnits(id) {
    type = OU_LEAF;
    nodeId = id;
    ouBean.getOrganisationUnits(ouTreeBeanCallback);
}

function getOrgUnitsExcludingBranch(excludeOrgUnitId) {
    type = OU_LEAF;
    ouBean.getOrganisationUnitsExcludingBranch(excludeOrgUnitId, ouTreeBeanCallback);
}

function getOrgUnitsAndPositionLinks() {
    type = OU_WITH_POS_LEAF_LINK;
    ouBean.getOrganisationUnits(ouTreeBeanCallback);
}

function getPositionsForOrgUnit(orgUnitId) {
    type = POS_LEAF_ADD;
    positionsBean.getPositions(orgUnitId, posLeafBeanCallback);
}

function traverse(orgUnit, prevId) {
    var orgUnitHref;
    if (type == OU_LEAF) {
        orgUnitHref = 'javascript:popupOK(\'' + orgUnit.label + '\'' + ',' + orgUnit.id + ');';
    } else if (type == OU_WITH_POS_LEAF_LINK) {
        orgUnitHref = 'javascript:getPositionsForOrgUnit(' + orgUnit.id + ');';
    }
    dView.add(orgUnit.id, prevId, orgUnit.label, orgUnitHref);
    var children = orgUnit.children;
    for (var i = 0; i < children.length; i++) {
        traverse(children[i], orgUnit.id);
    }
}

//The object model is changed when dTree.toString() is called
//So if we need to adapt it after calling toString(),
//we need a ref to the original model, which we get by
//deep cloning it before calling toString()
function deepClone(obj) {
    var objectClone = new obj.constructor();
    for (var property in obj) {
        if (obj[property] == null)
            continue;
        if (typeof obj[property] == 'object') {
            objectClone[property] = deepClone(obj[property]);
        } else {
            objectClone[property] = obj[property];
        }
    }
    return objectClone;
}