package com.zynap.talentstudio.integration.adapter;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 14-Oct-2005
 * Time: 14:08:25
 */
public interface IRemoteXmlService extends Remote {

    public String execute(String input, byte[][] attachment) throws RemoteException;

}


