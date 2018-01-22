package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */

import com.sun.tools.attach.spi.AttachProvider;
import jdk.Exported;

@Exported
public class VirtualMachineDescriptor
{
    private AttachProvider provider;
    private String id;
    private String displayName;
    private volatile int hash;

    public VirtualMachineDescriptor(AttachProvider paramAttachProvider, String paramString1, String paramString2)
    {
        if (paramAttachProvider == null) {
            throw new NullPointerException("provider cannot be null");
        }
        if (paramString1 == null) {
            throw new NullPointerException("identifier cannot be null");
        }
        if (paramString2 == null) {
            throw new NullPointerException("display name cannot be null");
        }
        this.provider = paramAttachProvider;
        this.id = paramString1;
        this.displayName = paramString2;
    }

    public VirtualMachineDescriptor(AttachProvider paramAttachProvider, String paramString)
    {
        this(paramAttachProvider, paramString, paramString);
    }

    public AttachProvider provider()
    {
        return this.provider;
    }

    public String id()
    {
        return this.id;
    }

    public String displayName()
    {
        return this.displayName;
    }

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
        if (!(paramObject instanceof VirtualMachineDescriptor)) {
            return false;
        }
        VirtualMachineDescriptor localVirtualMachineDescriptor = (VirtualMachineDescriptor)paramObject;
        if (localVirtualMachineDescriptor.provider() != provider()) {
            return false;
        }
        if (!localVirtualMachineDescriptor.id().equals(id())) {
            return false;
        }
        return true;
    }

    public String toString()
    {
        String str = this.provider.toString() + ": " + this.id;
        if (this.displayName != this.id) {
            str = str + " " + this.displayName;
        }
        return str;
    }
}
