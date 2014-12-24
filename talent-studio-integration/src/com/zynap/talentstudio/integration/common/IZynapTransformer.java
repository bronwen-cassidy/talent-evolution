package com.zynap.talentstudio.integration.common;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 16:15:24
 *
 * Interface that classes that transform domain objects to XML and vice versa must implement.
 */
public interface IZynapTransformer {

    IDomainObject transform(IZynapDataTransferObject dto, byte[][] attachments, Map CreatedReferences) throws TalentStudioException;

    IZynapDataTransferObject transform(Collection domainObjects) throws TalentStudioException;

    Class getDomainObjectClass(IZynapDataTransferObject input) throws TalentStudioException;

    IDomainObject transform(IDomainObject domainObject, IZynapDataTransferObject input, byte[][] attachments, Map createdReferences) throws TalentStudioException;

    String serialize(List results) throws TalentStudioException;

    void serialize(Writer outputWriter, List results) throws TalentStudioException;

    IZynapCommandResult createCommandResult(IZynapCommand command) throws TalentStudioException;

    IZynapCommandResult createCommandResult(IZynapCommand command, IZynapDataTransferObject dataTransferObject) throws TalentStudioException;    
}
