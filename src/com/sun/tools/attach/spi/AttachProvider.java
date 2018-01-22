package com.sun.tools.attach.spi;

/**
 * Created by Ethan on 1/22/2018.
 */

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.AttachPermission;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import jdk.Exported;

@Exported
public abstract class AttachProvider
{
    private static final Object lock = new Object();
    private static List<AttachProvider> providers = null;

    protected AttachProvider()
    {
        SecurityManager localSecurityManager = System.getSecurityManager();
        if (localSecurityManager != null) {
            localSecurityManager.checkPermission(new AttachPermission(
                    "createAttachProvider"));
        }
    }

    public abstract String name();

    public abstract String type();

    public abstract VirtualMachine attachVirtualMachine(String paramString)
            throws AttachNotSupportedException, IOException;

    public VirtualMachine attachVirtualMachine(VirtualMachineDescriptor paramVirtualMachineDescriptor)
            throws AttachNotSupportedException, IOException
    {
        if (paramVirtualMachineDescriptor.provider() != this) {
            throw new AttachNotSupportedException("provider mismatch");
        }
        return attachVirtualMachine(paramVirtualMachineDescriptor.id());
    }

    public abstract List<VirtualMachineDescriptor> listVirtualMachines();

    public static List<AttachProvider> providers()
    {
        synchronized (lock)
        {
            if (providers == null)
            {
                providers = new ArrayList();

                ServiceLoader<AttachProvider> localServiceLoader =
                        ServiceLoader.load(AttachProvider.class,
                                AttachProvider.class.getClassLoader());

                Iterator<AttachProvider> localIterator = localServiceLoader
                        .iterator();
                while (localIterator.hasNext()) {
                    try
                    {
                        providers.add((AttachProvider)localIterator.next());
                    }
                    catch (Throwable localThrowable)
                    {
                        if ((localThrowable instanceof ThreadDeath))
                        {
                            ThreadDeath localThreadDeath = (ThreadDeath)localThrowable;
                            throw localThreadDeath;
                        }
                        System.err.println(localThrowable);
                    }
                }
            }
            return Collections.unmodifiableList(providers);
        }
    }
}
