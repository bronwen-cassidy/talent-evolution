package com.zynap.talentstudio.integration.dto.dom;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;
import sun.misc.BASE64Encoder;

import com.zynap.common.util.XmlUtils;
import com.zynap.domain.IBasicDomainObject;
import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.common.mapping.ExternalRefMapping;
import com.zynap.talentstudio.integration.common.IZynapDataTransferObject;
import com.zynap.talentstudio.integration.common.IntegrationConstants;
import com.zynap.talentstudio.integration.delegate.ZynapIdMapper;
import com.zynap.talentstudio.integration.dto.config.ZynapResourcesLocator;
import com.zynap.talentstudio.integration.dto.filters.IPropertyFilter;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.questionnaires.Questionnaire;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 11-Oct-2005
 * Time: 11:41:11
 * <p/>
 * Helper that handles conversion of DTOs to domain objects and vice versa.
 */
public class ZynapDtoToDomainTransformer {

    public ZynapDtoToDomainTransformer() {
    }

    public IDomainObject transformDtoToDomainObject(ZynapDataTransferObject dto, Class domainObjectClass, byte[][] attachments, Map createdReferences) throws TalentStudioException {
        return (IDomainObject) objectDtoToDomainObjectTransformer.transform(dto, domainObjectClass, attachments, false, createdReferences);
    }

    public IDomainObject transformDtoToDomainObject(IDomainObject domainObject, ZynapDataTransferObject dto, byte[][] attachments, Map createdReferences) throws TalentStudioException {
        return (IDomainObject) objectDtoToDomainObjectTransformer.populate(domainObject, dto, attachments, true, createdReferences);
    }

    public IZynapDataTransferObject transformDomainObjectToDto(IDomainObject domainObject) throws TalentStudioException {

        Document document = XmlUtils.newDocument();
        return objectDtoToDomainObjectTransformer.transform(domainObject, null, getNameFromClass(domainObject.getClass()), document, 0)[0];
    }

    public IZynapDataTransferObject transformDomainObjectToDto(Collection collection) throws TalentStudioException {
        Document document = XmlUtils.newDocument();
        return collectionDtoToDomainObjectTransformer.transform(collection, null, IntegrationConstants.RESULT_NODE, document, 0)[0];
    }

    private BaseDtoToDomainObjectTranformer getToDomainObjectTransformer(PropertyDescriptor propertyDescriptor) {
        return getToDomainObjectTransformer(propertyDescriptor.getPropertyType(), propertyDescriptor.getName());
    }

    private BaseDtoToDomainObjectTranformer getToDomainObjectTransformer(Class objectClass, String name) {
        if (IBasicDomainObject.class.isAssignableFrom(objectClass)) {
            return objectDtoToDomainObjectTransformer;
        } else if (Collection.class.isAssignableFrom(objectClass)) {
            if (IntegrationConstants.ASSOCIATIONS_ATTRIBUTE.contains(name)) {
                return associationDtoToDomainObjectTransformer;
            } else {
                return collectionDtoToDomainObjectTransformer;
            }
        } else if (AttributeValuesCollection.class.isAssignableFrom(objectClass)) {
            return daDtoToDomainObjectTransformer;
        } else if (byte[].class.isAssignableFrom(objectClass)) {
            return byteArrayDtoToDomainObjectTransformer;
        } else if (IntegrationConstants.ID_ATTRIBUTE.equalsIgnoreCase(name) && Long.class.isAssignableFrom(objectClass)) {
            return idDtoToDomainObjectTransformer;
        } else if (Date.class.isAssignableFrom(objectClass)) {
            return dateDtoToDomainObjectTransformer;
        } else {
            return propertyDtoToDomainObjectTransformer;
        }
    }

    private String getNameFromClass(Class objectClass) {
        return resourcesLocator.getAliasFromClass(objectClass);
    }

    private interface BaseDtoToDomainObjectTranformer {

        ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document parentDocument, int deepLevel) throws TalentStudioException;
    }

    private interface DtoToDomainObjectTransformer extends BaseDtoToDomainObjectTranformer {

        Object transform(ZynapDataTransferObject dto, Class propertyClass, byte[][] attachments, boolean replaceIds, Map createdReferences) throws TalentStudioException;
    }

    private class PropertyDtoToDomainObjectTransformer implements DtoToDomainObjectTransformer {

        public Object transform(ZynapDataTransferObject dto, Class propertyClass, byte[][] attachments, boolean replaceIds, Map createdReferences) {
            Node firstChild = dto.getElement().getFirstChild();
            if (firstChild instanceof CDATASection) {
                firstChild = firstChild.getNextSibling();
            }
            return firstChild != null ? firstChild.getNodeValue() : null;
        }

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document document, int deepLevel) {
            Element element = document.createElement(propertyName);
            element.appendChild(document.createTextNode(object.toString()));
            return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};
        }
    }

    private class DaDtoToDomainObjectTransformer implements BaseDtoToDomainObjectTranformer {

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document document, int deepLevel) {
            AttributeValuesCollection attributes = (AttributeValuesCollection) object;
            ZynapDataTransferObject[] results = new ZynapDataTransferObject[attributes.size()];
            int i = 0;
            for (Iterator iterator = attributes.getValues().iterator(); iterator.hasNext();) {
                AttributeValue item = (AttributeValue) iterator.next();
                final DynamicAttribute dynamicAttribute = item.getDynamicAttribute();
                try {
                    Element element = document.createElement(dynamicAttribute.getExternalRefLabel());
                    String value = item.getValue();
                    if (dynamicAttribute.getRefersToType() != null) {
                        value = dynamicAttribute.getRefersToType().getLookupValue(new Long(value)).getValueId();
                    }
                    String type = dynamicAttribute.getType();
                    if (type.equals(DynamicAttribute.DA_TYPE_OU) || type.equals(DynamicAttribute.DA_TYPE_POSITION) || type.equals(DynamicAttribute.DA_TYPE_SUBJECT)) {
                        value = null;
                    }
                    element.appendChild(document.createTextNode(value));
                    results[i++] = new ZynapDataTransferObject(element);
                } catch (DOMException e) {
                    // todo this must be handled better we cannot have dynamicAttribute ref labels with invalid characters
                    logger.error("Invalid xml element of " + dynamicAttribute.getExternalRefLabel());
                }
            }

            return results;
        }

        public void transform(IBasicDomainObject propertyObject, ZynapDataTransferObject propertyDto, DynamicAttribute dynamicAttribute) throws TalentStudioException {

            Node child = propertyDto.getElement().getFirstChild();
            String value = null;
            if (child != null) {
                value = child.getNodeValue();
            }

            final LookupType lookupType = dynamicAttribute.getRefersToType();
            if (lookupType != null) {
                LookupValue lookupValue = lookupType.getLookupValue(value);
                if (lookupValue == null) {
                    String msg = "Extended Attribute Value not found: " + dynamicAttribute + " Value: " + value;
                    logger.error(msg);
                    throw new TalentStudioException(msg);
                }

                value = lookupValue.getId().toString();
            }


            final com.zynap.talentstudio.organisation.Node node = ((com.zynap.talentstudio.organisation.Node) propertyObject);
            final NodeExtendedAttribute newNodeExtendedAttribute = new NodeExtendedAttribute(value, node, dynamicAttribute);
            final Set currentExtendedAttributes = node.getExtendedAttributes();

            final Set toAdd = new HashSet();
            final Set toRemove = new HashSet();

            String type = dynamicAttribute.getType();

            // Organisation, user and position are currently NOT supported
            boolean supported = !(type.equals(DynamicAttribute.DA_TYPE_OU) || type.equals(DynamicAttribute.DA_TYPE_POSITION) || type.equals(DynamicAttribute.DA_TYPE_SUBJECT));

            boolean found = false;
            for (Iterator iterator = currentExtendedAttributes.iterator(); iterator.hasNext();) {
                final NodeExtendedAttribute currentExtendedAttribute = (NodeExtendedAttribute) iterator.next();
                if (dynamicAttribute.equals(currentExtendedAttribute.getDynamicAttribute())) {

                    if (StringUtils.hasText(value) && supported) {
                        currentExtendedAttribute.setValue(value);
                    } else {
                        // if the node has no value then remove it from the node set
                        toRemove.add(currentExtendedAttribute);
                    }
                    found = true;
                    break;
                }
            }
            if (!found && supported) {
                toAdd.add(newNodeExtendedAttribute);
            }

            node.removeNodeExtendedAttributes(toRemove);
            node.addNodeExtendedAttributes(toAdd);
        }
    }

    private class IdDtoToDomainObjectTransformer implements BaseDtoToDomainObjectTranformer {

        public Object transform(ZynapDataTransferObject dto, IBasicDomainObject parent, byte[][] attachments, boolean replaceIds, Map createdReferences) {
            if (replaceIds)
                return zynapIdMapper.getInternalId(dto.getValue(), parent.getClass().getName(), resourcesLocator.getSupportedClasses());
            else {
                final String nodeValue = dto.getElement().getFirstChild().getNodeValue();
                ExternalRefMapping externalRefMapping = new ExternalRefMapping(null, parent.getClass().getName(), null, nodeValue, null, false);
                createdReferences.put(parent, externalRefMapping);
                return null;
            }
        }

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document document, int deepLevel) throws TalentStudioException {
            ExternalRefMapping externalId = zynapIdMapper.getExternalId((Serializable) object, parentClass.getName());
            Element element = document.createElement(propertyName);
            if (externalId.isGenerated()) {
                element.setAttribute(IntegrationConstants.GENERATED_ATTRIBUTE, Boolean.TRUE.toString());
            }
            element.appendChild(document.createTextNode(externalId.getExternalRefId()));
            return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};
        }
    }

    private class ByteArrayDtoToDomainObjectTransformer implements DtoToDomainObjectTransformer {

        public Object transform(ZynapDataTransferObject dto, Class propertyClass, byte[][] attachments, boolean replaceIds, Map createdReferences) {
            final int pos = new Integer(dto.getElement().getAttribute(IntegrationConstants.REF_ATTRIBUTE)).intValue();
            return attachments[pos];
        }

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document document, int deepLevel) {
            if (deepLevel < 2) {
                byte[] data = (byte[]) object;
                Element element = document.createElement(propertyName);
                BASE64Encoder encoder = new BASE64Encoder();
                element.appendChild(document.createCDATASection(encoder.encode(data)));
                return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};
            } else
                return new ZynapDataTransferObject[0];
        }
    }

    private class ObjectDtoToDomainObjectTransformer implements DtoToDomainObjectTransformer {

        protected Map getDynamicAttributesMap(IBasicDomainObject propertyObject) {
            Map daMap = null;
            if (propertyObject instanceof com.zynap.talentstudio.organisation.Node) {
                com.zynap.talentstudio.organisation.Node node = (com.zynap.talentstudio.organisation.Node) propertyObject;
                Collection das = dynamicAttributesService.getAllActiveAttributes(node.getNodeType(), false);
                daMap = new HashMap();
                for (Iterator iterator = das.iterator(); iterator.hasNext();) {
                    DynamicAttribute da = (DynamicAttribute) iterator.next();
                    daMap.put(da.getExternalRefLabel(), da);
                }
            }
            return daMap;
        }

        public Object transform(ZynapDataTransferObject dto, Class propertyClass, byte[][] attachments, boolean replaceIds, Map createdReferences) throws TalentStudioException {
            IBasicDomainObject propertyObject = (IBasicDomainObject) createObjectFromProperty(propertyClass);
            return populate(propertyObject, dto, attachments, replaceIds, createdReferences);
        }


        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document parentDocument, int deepLevel) throws TalentStudioException {
            Element element = parentDocument.createElement(propertyName);
            final Class objectClass = object.getClass();
            PropertyDescriptor[] properties = propertyFilter.filter(PropertyUtils.getPropertyDescriptors(objectClass), propertyName);

            for (int i = 0; i < properties.length; i++) {
                PropertyDescriptor property = properties[i];
                final Class propertyType = property.getPropertyType();
                final String fieldName = property.getName();

                if ((deepLevel > 2) && (Collection.class.isAssignableFrom(propertyType)))
                    continue;

                BaseDtoToDomainObjectTranformer transformer = getToDomainObjectTransformer(property);
                try {
                    Object value = PropertyUtils.getProperty(object, fieldName);
                    if (value != null) {
                        ZynapDataTransferObject[] transformValues = transformer.transform(value, objectClass, fieldName, parentDocument, deepLevel + 1);
                        for (int j = 0; j < transformValues.length; j++) {
                            ZynapDataTransferObject transformValue = transformValues[j];
                            if (transformValue != null) {
                                element.appendChild(transformValue.getElement());
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new TalentStudioException("Failed to transform object because of: ", e);
                } catch (InvocationTargetException e) {
                    throw new TalentStudioException("Failed to transform object because of: ", e);
                } catch (NoSuchMethodException e) {
                    throw new TalentStudioException("Failed to transform object because of: ", e);
                }
            }

            return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};
        }

        public Object populate(IBasicDomainObject propertyObject, ZynapDataTransferObject dto, byte[][] attachments, boolean replaceIds, Map createdReferences) throws TalentStudioException {

            NodeList attributes = dto.getElement().getChildNodes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);
                ZynapDataTransferObject propertyDto = new ZynapDataTransferObject((Element) node);
                PropertyDescriptor propertyDescriptor;
                try {
                    try {
                        propertyDescriptor = PropertyUtils.getPropertyDescriptor(propertyObject, propertyDto.getName());
                    } catch (NoSuchMethodException e) {
                        propertyDescriptor = null;
                    }
                    if (propertyDescriptor != null) {
                        final BaseDtoToDomainObjectTranformer transformer = getToDomainObjectTransformer(propertyDescriptor);
                        Class propertyType = propertyDescriptor.getPropertyType();
                        boolean replaceIdsInternal = true;
                        Object propertyValue;
                        if (transformer instanceof IdDtoToDomainObjectTransformer) {
                            replaceIdsInternal = replaceIds;
                            propertyValue = ((IdDtoToDomainObjectTransformer) transformer).transform(propertyDto, propertyObject, attachments, replaceIdsInternal, createdReferences);
                        } else if (transformer instanceof CollectionDtoToDomainObjectTransformer && replaceIds) {
                            propertyValue = PropertyUtils.getProperty(propertyObject, propertyDto.getName());
                            ((CollectionDtoToDomainObjectTransformer) transformer).populate(propertyDto, (Collection) propertyValue, attachments, createdReferences);
                        } else {
                            propertyValue = ((DtoToDomainObjectTransformer) transformer).transform(propertyDto, propertyType, attachments, replaceIdsInternal, createdReferences);
                        }

                        if (propertyValue != null) {
                            BeanUtils.setProperty(propertyObject, propertyDto.getName(), propertyValue);
                        }
                    } else {
                        final Class<? extends IBasicDomainObject> aClass = propertyObject.getClass();
                        String type = "S";
                        if (aClass.getName().equals(Position.class.getName())) type = "P";
                        else if (aClass.getName().equals(OrganisationUnit.class.getName())) type = "O";
                        else if (aClass.getName().equals(Questionnaire.class.getName())) type = "Q";
                        DynamicAttribute dynAtt = dynamicAttributesService.findAttributeByRefLabel(propertyDto.getName().toLowerCase(), type);
                        if (dynAtt != null)
                            daDtoToDomainObjectTransformer.transform(propertyObject, propertyDto, dynAtt);
                    }
                } catch (IllegalAccessException e) {
                    throw new TalentStudioException("Failed to transform object because of: ", e);
                } catch (InvocationTargetException e) {
                    throw new TalentStudioException("Failed to transform object because of: ", e);
                } catch (NoSuchMethodException e) {
                    throw new TalentStudioException("Failed to transform object because of: ", e);
                }
            }

            return propertyObject;
        }

    }

    private class CollectionDtoToDomainObjectTransformer implements DtoToDomainObjectTransformer {

        public Object transform(ZynapDataTransferObject dto, Class propertyClass, byte[][] attachments, boolean replaceIds, Map createdReferences) throws TalentStudioException {
            Collection propertyObject;
            if (Set.class.isAssignableFrom(propertyClass)) {
                propertyObject = new HashSet();
            } else if (List.class.isAssignableFrom(propertyClass)) {
                propertyObject = new ArrayList();
            } else {
                throw new IllegalArgumentException("Cannot transform class: " + propertyClass);
            }

            return populate(dto, propertyObject, attachments, createdReferences);

        }

        public Collection populate(ZynapDataTransferObject dto, Collection propertyObject, byte[][] attachments, Map createdReferences) throws TalentStudioException {
            propertyObject.clear();
            NodeList attributes = dto.getElement().getChildNodes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);
                final ZynapDataTransferObject propertyDto = new ZynapDataTransferObject((Element) node);
                Class itemClass = resourcesLocator.getClassFromAlias(propertyDto.getName());
                propertyObject.add(((DtoToDomainObjectTransformer) getToDomainObjectTransformer(itemClass, null)).transform(propertyDto, itemClass, attachments, true, createdReferences));
            }

            return propertyObject;
        }

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document parentDocument, int deepLevel) throws TalentStudioException {
            Element element = parentDocument.createElement(propertyName);
            Collection attributes = (Collection) object;
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                Object item = iterator.next();
                final BaseDtoToDomainObjectTranformer transformer = getToDomainObjectTransformer(item.getClass(), null);
                ZynapDataTransferObject[] transformValues = transformer.transform(item, null, getNameFromClass(item.getClass()), parentDocument, deepLevel + 1);
                for (int j = 0; j < transformValues.length; j++) {
                    ZynapDataTransferObject transformValue = transformValues[j];
                    element.appendChild(transformValue.getElement());
                }
            }

            return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};
        }
    }

    private class AssociationDtoToDomainObjectTransformer extends CollectionDtoToDomainObjectTransformer implements DtoToDomainObjectTransformer {

        // todo NB if the source association is primary there can only be one!!
        public Collection populate(ZynapDataTransferObject dto, Collection propertyObject, byte[][] attachments, Map createdReferences) throws TalentStudioException {
            NodeList attributes = dto.getElement().getChildNodes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Element node = (Element) attributes.item(i);
                final ZynapDataTransferObject propertyDto = new ZynapDataTransferObject(node);
                ArtefactAssociation association = (ArtefactAssociation) resourcesLocator.getObjectFromAlias(propertyDto.getName());
                LookupValue qualifier = getQualifier(node, dto.getName());
                association.setQualifier(qualifier);
                ZynapDataTransferObject targetDto = new ZynapDataTransferObject((Element) node.getFirstChild());
                Class itemClass = Position.class;
                Position target = (Position) ((DtoToDomainObjectTransformer) getToDomainObjectTransformer(itemClass, null)).transform(targetDto, itemClass, attachments, true, createdReferences);
                association.setTarget(target);

                ArtefactAssociation found = findAssociation(association, propertyObject);
                if (found == null) {
                    propertyObject.add(association);
                } else if (found.isPrimary() && found.hasPositionSource()) {                    
                    final boolean targetsMatch = found.getTarget().getId().equals(target.getId());
                    if (!targetsMatch) {
                        found.setTarget(target);                    
                    }
                }
            }
            return propertyObject;
        }

        private ArtefactAssociation findAssociation(final ArtefactAssociation toMatch, Collection currentAssociations) {
            return (ArtefactAssociation) CollectionUtils.find(currentAssociations, new Predicate() {
                public boolean evaluate(Object object) {
                    ArtefactAssociation association = (ArtefactAssociation) object;
                    Long newQualifierId = toMatch.getQualifier().getId();
                    Long existingQualifierId = association.getQualifier().getId();
                    Long newTargetNodeId = toMatch.getTarget().getId();
                    Long existingTargetId = association.getTarget().getId();
                    // position associations of all types neval allow the target to be duplicate regardless of qualifier.
                    boolean isPositionAssociation = association.hasPositionSource();
                    // todo if toMatch is primary && this is a position association return the existing association that is primary
                    if (toMatch.isPrimary() && isPositionAssociation) {
                        return association.isPrimary();
                    } else if (toMatch.isPrimary() || isPositionAssociation) {
                        return newTargetNodeId.equals(existingTargetId);
                    } else {
                        // only applies to secondary subject associations
                        return (newQualifierId.equals(existingQualifierId) && newTargetNodeId.equals(existingTargetId));
                    }
                }
            });
        }

        private LookupValue getQualifier(Element node, String collectionName) throws TalentStudioException {
            String type = node.getAttribute(IntegrationConstants.TYPE_ATTRIBUTE);
            if (collectionName.equals(IntegrationConstants.SUBJECT_ASSOCIATION_FIELD))
                return lookupManager.findSubjectAssociationQualifier(type);
            else
                return lookupManager.findPositionAssociationQualifier(type);

        }

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document parentDocument, int deepLevel) throws TalentStudioException {
            Element element = parentDocument.createElement(propertyName);
            Collection attributes = (Collection) object;
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                ArtefactAssociation item = (ArtefactAssociation) iterator.next();
                final ZynapDataTransferObject association = new ZynapDataTransferObject(parentDocument.createElement(getNameFromClass(item.getClass())));
                association.getElement().setAttribute(IntegrationConstants.TYPE_ATTRIBUTE, item.getQualifier().getValueId());
                Element target = parentDocument.createElement(IntegrationConstants.TARGET_NODE);
                target.appendChild(idDtoToDomainObjectTransformer.transform(item.getTarget().getId(), item.getTarget().getClass(), "id", parentDocument, deepLevel + 1)[0].getElement());
                association.getElement().appendChild(target);
                element.appendChild(association.getElement());
            }
            return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};

        }
    }

    private class DateDtoToDomainObjectTransformer implements DtoToDomainObjectTransformer {

        public Object transform(ZynapDataTransferObject dto, Class propertyClass, byte[][] attachments, boolean replaceIds, Map createdReferences) throws TalentStudioException {
            Node child = dto.getElement().getFirstChild();
            String value = child != null ? child.getNodeValue() : null;
            return value != null ? FormatterFactory.getDateFormatter().getDateValue(value) : null;
        }

        public ZynapDataTransferObject[] transform(Object object, Class parentClass, String propertyName, Document parentDocument, int deepLevel) throws TalentStudioException {

            String value = FormatterFactory.getDateFormatter().formatDateAsString((Date) object);

            Element element = parentDocument.createElement(propertyName);
            element.appendChild(parentDocument.createTextNode(value));
            return new ZynapDataTransferObject[]{new ZynapDataTransferObject(element)};
        }
    }

    private Object createObjectFromProperty(Class propertyClass) throws TalentStudioException {
        try {
            return propertyClass.newInstance();
        } catch (InstantiationException e) {
            throw new TalentStudioException("Failed to transform object because of: ", e);
        } catch (IllegalAccessException e) {
            throw new TalentStudioException("Failed to transform object because of: ", e);
        }
    }

    public IDynamicAttributeService getDynamicAttributesService() {
        return dynamicAttributesService;
    }

    public void setDynamicAttributesService(IDynamicAttributeService dynamicAttributesService) {
        this.dynamicAttributesService = dynamicAttributesService;
    }

    public ZynapResourcesLocator getResourcesLocator() {
        return resourcesLocator;
    }

    public void setResourcesLocator(ZynapResourcesLocator resourcesLocator) {
        this.resourcesLocator = resourcesLocator;
    }

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public ZynapIdMapper getZynapIdMapper() {
        return zynapIdMapper;
    }

    public void setZynapIdMapper(ZynapIdMapper zynapIdMapper) {
        this.zynapIdMapper = zynapIdMapper;
    }

    public void setPropertyFilter(IPropertyFilter propertyFilter) {
        this.propertyFilter = propertyFilter;
    }

    private ObjectDtoToDomainObjectTransformer objectDtoToDomainObjectTransformer = new ObjectDtoToDomainObjectTransformer();
    private DtoToDomainObjectTransformer propertyDtoToDomainObjectTransformer = new PropertyDtoToDomainObjectTransformer();
    private DtoToDomainObjectTransformer collectionDtoToDomainObjectTransformer = new CollectionDtoToDomainObjectTransformer();
    private DtoToDomainObjectTransformer associationDtoToDomainObjectTransformer = new AssociationDtoToDomainObjectTransformer();
    private DtoToDomainObjectTransformer byteArrayDtoToDomainObjectTransformer = new ByteArrayDtoToDomainObjectTransformer();
    private IdDtoToDomainObjectTransformer idDtoToDomainObjectTransformer = new IdDtoToDomainObjectTransformer();
    private DaDtoToDomainObjectTransformer daDtoToDomainObjectTransformer = new DaDtoToDomainObjectTransformer();
    private DateDtoToDomainObjectTransformer dateDtoToDomainObjectTransformer = new DateDtoToDomainObjectTransformer();

    private IDynamicAttributeService dynamicAttributesService;
    private ILookupManager lookupManager;
    private ZynapIdMapper zynapIdMapper;
    private ZynapResourcesLocator resourcesLocator;
    private IPropertyFilter propertyFilter;

    private static final Log logger = LogFactory.getLog(ZynapDtoToDomainTransformer.class);

}
