package com.zynap.talentstudio.integration.dto.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.zynap.common.util.XmlUtils;
import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.integration.common.CommandResult;
import com.zynap.talentstudio.integration.common.IZynapCommand;
import com.zynap.talentstudio.integration.common.IZynapCommandResult;
import com.zynap.talentstudio.integration.common.IZynapDataTransferObject;
import com.zynap.talentstudio.integration.common.IZynapTransformer;
import com.zynap.talentstudio.integration.common.IntegrationConstants;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 10-Oct-2005 16:18:38
* @version 0.1
*/
public class ZynapTransformer implements IZynapTransformer {

    public IDomainObject transform(IZynapDataTransferObject dto, byte[][] attachments, Map createdReferences) throws TalentStudioException {
        Class domainObjectClass = getDomainObjectClass(dto);
        return zynapDtoToDomainTransformer.transformDtoToDomainObject((ZynapDataTransferObject) dto, domainObjectClass, attachments, createdReferences);
    }

    public Class getDomainObjectClass(IZynapDataTransferObject dto) throws TalentStudioException {
        return zynapDtoToDomainTransformer.getResourcesLocator().getClassFromAlias(dto.getName());
    }

    public IZynapDataTransferObject transform(Collection domainObjects) throws TalentStudioException {
        return zynapDtoToDomainTransformer.transformDomainObjectToDto(domainObjects);
    }

    public void setZynapDtoToDomainTransformer(ZynapDtoToDomainTransformer zynapDtoToDomainTransformer) {
        this.zynapDtoToDomainTransformer = zynapDtoToDomainTransformer;
    }

    public IDomainObject transform(IDomainObject domainObject, IZynapDataTransferObject input, byte[][] attachments, Map createdReferences) throws TalentStudioException {
        return zynapDtoToDomainTransformer.transformDtoToDomainObject(domainObject, (ZynapDataTransferObject) input, attachments, createdReferences);
    }

    private Element createDocument(List results) {
        Document document = XmlUtils.newDocument();
        Element rootElement = document.createElement(IntegrationConstants.RESULTS_NODE);

        for (Iterator iterator = results.iterator(); iterator.hasNext();) {

            final IZynapCommandResult result = (IZynapCommandResult) iterator.next();

            final ZynapDataTransferObject zynapDataTransferObject = (ZynapDataTransferObject) result.getResult();
            final Node newNode = document.importNode(zynapDataTransferObject.getElement(), true);
            rootElement.appendChild(newNode);
        }

        return rootElement;
    }

    private void serializeDocument(java.io.Writer outputWriter, Element rootElement) throws TalentStudioException {
        OutputFormat outputFormat = new OutputFormat(rootElement.getOwnerDocument(), "UTF-8", true);
        XMLSerializer serializer = new XMLSerializer(outputWriter, outputFormat);

        try {
            serializer.serialize(rootElement);
        } catch (IOException e) {
            throw new TalentStudioException("Failed to build XML output", e);
        }
    }

    public String serialize(List results) throws TalentStudioException {
        Element rootElement = createDocument(results);
        CharArrayWriter outputStream = new CharArrayWriter();
        serializeDocument(outputStream, rootElement);
        return outputStream.toString();
    }

    public void serialize(Writer outputWriter, List results) throws TalentStudioException {
        Element rootElement = createDocument(results);
        serializeDocument(outputWriter, rootElement);
    }

    public IZynapCommandResult createCommandResult(IZynapCommand command) throws TalentStudioException {

        final Document document = XmlUtils.newDocument();
        final Element resultElement = document.createElement(IntegrationConstants.RESULT_NODE);
        resultElement.setAttribute(IntegrationConstants.ACTION_ATTRIBUTE, command.getAction());
        IZynapDataTransferObject dto = new ZynapDataTransferObject(resultElement);
        return createCommandResult(command, dto);
    }

    public IZynapCommandResult createCommandResult(IZynapCommand command, IZynapDataTransferObject dataTransferObject) throws TalentStudioException {
        return new CommandResult(command, dataTransferObject);
    }

    private ZynapDtoToDomainTransformer zynapDtoToDomainTransformer;
}
