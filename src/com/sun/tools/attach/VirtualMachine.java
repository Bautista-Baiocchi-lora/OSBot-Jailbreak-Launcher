package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */

import com.sun.tools.attach.spi.AttachProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import jdk.Exported;

@Exported
public abstract class VirtualMachine
{
    private AttachProvider provider;
    private String id;
    private volatile int hash;

    protected VirtualMachine(AttachProvider paramAttachProvider, String paramString)
    {
        if (paramAttachProvider == null) {
            throw new NullPointerException("provider cannot be null");
        }
        if (paramString == null) {
            throw new NullPointerException("id cannot be null");
        }
        this.provider = paramAttachProvider;
        this.id = paramString;
    }

    public static List<VirtualMachineDescriptor> list()
    {
        ArrayList<VirtualMachineDescriptor> localArrayList = new ArrayList();

        List<AttachProvider> localList = AttachProvider.providers();
        for (AttachProvider localAttachProvider : localList) {
            localArrayList.addAll(localAttachProvider.listVirtualMachines());
        }
        return localArrayList;
    }

    public static VirtualMachine attach(String paramString)
            throws AttachNotSupportedException, IOException
    {
        if (paramString == null) {
            throw new NullPointerException("id cannot be null");
        }
        List<AttachProvider> localList = AttachProvider.providers();
        if (localList.size() == 0) {
            throw new AttachNotSupportedException("no providers installed");
        }
        AttachNotSupportedException localObject = null;
        for (AttachProvider localAttachProvider : localList) {
            try
            {
                return localAttachProvider.attachVirtualMachine(paramString);
            }
            catch (AttachNotSupportedException localAttachNotSupportedException)
            {
                localObject = localAttachNotSupportedException;
            }
        }
        throw localObject;
    }

    public static VirtualMachine attach(VirtualMachineDescriptor paramVirtualMachineDescriptor)
            throws AttachNotSupportedException, IOException
    {
        return paramVirtualMachineDescriptor.provider().attachVirtualMachine(
                paramVirtualMachineDescriptor);
    }

    public abstract void detach()
            throws IOException;

    public final AttachProvider provider()
    {
        return this.provider;
    }

    public final String id()
    {
        return this.id;
    }

    public abstract void loadAgentLibrary(String paramString1, String paramString2)
            throws AgentLoadException, AgentInitializationException, IOException;

    public void loadAgentLibrary(String paramString)
            throws AgentLoadException, AgentInitializationException, IOException
    {
        loadAgentLibrary(paramString, null);
    }

    public abstract void loadAgentPath(String paramString1, String paramString2)
            throws AgentLoadException, AgentInitializationException, IOException;

    public void loadAgentPath(String paramString)
            throws AgentLoadException, AgentInitializationException, IOException
    {
        loadAgentPath(paramString, null);
    }

    public abstract void loadAgent(String paramString1, String paramString2)
            throws AgentLoadException, AgentInitializationException, IOException;

    public void loadAgent(String paramString)
            throws AgentLoadException, AgentInitializationException, IOException
    {
        loadAgent(paramString, null);
    }

    public abstract Properties getSystemProperties()
            throws IOException;

    public abstract Properties getAgentProperties()
            throws IOException;

    public abstract void startManagementAgent(Properties paramProperties)
            throws IOException;

    public abstract String startLocalManagementAgent()
            throws IOException;

    public int hashCode()
    {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = (this.provider.hashCode() * 127 + this.id.hashCode());
        return this.hash;
    }

    public boolean equals(Object paramObject)
    {
        if (paramObject == this) {
            return true;
        }
        if (!(paramObject instanceof VirtualMachine)) {
            return false;
        }
        VirtualMachine localVirtualMachine = (VirtualMachine)paramObject;
        if (localVirtualMachine.provider() != provider()) {
            return false;
        }
        if (!localVirtualMachine.id().equals(id())) {
            return false;
        }
        return true;
    }

    public String toString()
    {
        return this.provider.toString() + ": " + this.id;
    }
}
