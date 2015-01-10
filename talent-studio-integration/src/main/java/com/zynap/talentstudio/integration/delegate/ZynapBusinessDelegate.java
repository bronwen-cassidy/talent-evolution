package com.zynap.talentstudio.integration.delegate;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.integration.common.IZynapCommand;
import com.zynap.talentstudio.integration.common.IZynapCommandResult;
import com.zynap.talentstudio.integration.common.IZynapDataTransferObject;
import com.zynap.talentstudio.integration.common.IZynapTransformer;
import com.zynap.talentstudio.integration.dto.config.ZynapResourcesLocator;
import com.zynap.talentstudio.integration.dto.processors.IPostProcessor;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.users.IUserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 14:55:59
 *
 * Class that looks up application services based on the commands and invokes the actual methods.
 */
public class ZynapBusinessDelegate implements IZynapBusinessDelegate {

    /**
     * Execute command.
     *
     * @param command
     * @param username
     * @return IZynapCommandResult
     * @throws TalentStudioException
     */
    public IZynapCommandResult invoke(IZynapCommand command, String username) throws TalentStudioException {

        // set the user session (threadlocal)
        User user = userService.findByUserName(username);
        final UserSession newSession = new UserSession(new UserPrincipal(user, null));
        UserSessionFactory.setUserSession(newSession);

        IZynapCommandResult commandResult;
        final byte[][] attachments = command.getAttachments();

        final Map createdReferences = new HashMap();

        final IZynapDataTransferObject input = command.getInput();
        IZynapService service = serviceLocator.getService(input.getName());

        IDomainObject domainObject;

        if (IZynapCommand.CREATE_ACTION.equals(command.getAction())) {

            domainObject = transformer.transform(input, attachments, createdReferences);
            IPostProcessor postProcessor = serviceLocator.getPostProcessor(input.getName());
            if (postProcessor != null) {
                postProcessor.process(domainObject, user);
            }
            service.create(domainObject);
            idMapper.createExternalReferences(createdReferences);
            service.updateStateInfo(domainObject);
            commandResult = transformer.createCommandResult(command);


        } else if (IZynapCommand.UPDATE_ACTION.equals(command.getAction())) {

            domainObject = getDomainObject(input, service);
            domainObject.initLazy();
            transformer.transform(domainObject, input, attachments, createdReferences);
            IPostProcessor postProcessor = serviceLocator.getPostProcessor(input.getName());
            if (postProcessor != null) {
                postProcessor.process(domainObject, user);
            }
            service.update(domainObject);
            service.updateStateInfo(domainObject);

            commandResult = transformer.createCommandResult(command);

        } else if (IZynapCommand.DELETE_ACTION.equals(command.getAction())) {

            domainObject = getDomainObject(input, service);
            service.delete(domainObject);

            commandResult = transformer.createCommandResult(command);

        } else if (IZynapCommand.FIND_ACTION.equals(command.getAction())) {

            domainObject = getDomainObject(input, service);
            List list = new ArrayList();
            list.add(domainObject);
            IZynapDataTransferObject data = transformer.transform(list);

            commandResult = transformer.createCommandResult(command, data);

        } else if (IZynapCommand.FIND_ALL_ACTION.equals(command.getAction())) {

            List list = service.findAll();
            IZynapDataTransferObject data = transformer.transform(list);

            commandResult = transformer.createCommandResult(command, data);

        } else if (IZynapCommand.SET_EXTERNAL_ID_ACTION.equals(command.getAction())) {

            idMapper.updateId(input.getExternalId(), input.getUpdatedExternalId(), transformer.getDomainObjectClass(input).getName());

            commandResult = transformer.createCommandResult(command);

        } else if (IZynapCommand.EXISTS_ACTION.equals(command.getAction())) {

            try {
                IZynapDataTransferObject data = find(input, service);
                commandResult = transformer.createCommandResult(command, data);
            } catch (DomainObjectNotFoundException e) {
                commandResult = transformer.createCommandResult(command);
            }

        } else {
            throw new UnsupportedOperationException("Action not supported");
        }


        return commandResult;
    }

    /**
     * Find domain object based on external id supplied in IZynapDataTransferObject.
     * <br/> Then transform the domain object so that we can produce XML.
     * <br/> Throws DomainObjectNotFoundException if cannot resolve external id or external id points to non-existent domain object.
     *
     * @param input
     * @param service
     * @return IZynapDataTransferObject
     * @throws TalentStudioException
     */
    private IZynapDataTransferObject find(final IZynapDataTransferObject input, IZynapService service) throws TalentStudioException {

        final IDomainObject domainObject = getDomainObject(input, service);

        final List list = new ArrayList();
        list.add(domainObject);

        return transformer.transform(list);
    }

    /**
     * Find domain object based on external id supplied in IZynapDataTransferObject.
     * <br/> Throws DomainObjectNotFoundException if cannot resolve external id or external id points to non-existent domain object.
     *
     * @param input
     * @param service
     * @return IDomainObject
     * @throws TalentStudioException
     */
    private IDomainObject getDomainObject(final IZynapDataTransferObject input, IZynapService service) throws TalentStudioException {
        final Class domainObjectClass = transformer.getDomainObjectClass(input);
        final String externalId = input.getExternalId();

        Serializable internalId = idMapper.getInternalId(externalId, domainObjectClass.getName(), serviceLocator.getSupportedClasses());
        if (internalId != null) {
            return service.findById(new Long(internalId.toString()));
        } else {
            throw new DomainObjectNotFoundException(domainObjectClass, externalId);
        }
    }

    public void setIdMapper(ZynapIdMapper idMapper) {
        this.idMapper = idMapper;
    }

    public void setTransformer(IZynapTransformer transformer) {
        this.transformer = transformer;
    }

    public void setServiceLocator(ZynapResourcesLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private IZynapTransformer transformer;
    private ZynapResourcesLocator serviceLocator;
    private ZynapIdMapper idMapper;
    private IUserService userService;
}
