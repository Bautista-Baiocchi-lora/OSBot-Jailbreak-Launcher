package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */
import java.security.BasicPermission;
import jdk.Exported;

@Exported
public final class AttachPermission
        extends BasicPermission
{
    static final long serialVersionUID = -4619447669752976181L;

    public AttachPermission(String paramString)
    {
        super(paramString);
        if ((!paramString.equals("attachVirtualMachine")) &&
                (!paramString.equals("createAttachProvider"))) {
            throw new IllegalArgumentException("name: " + paramString);
        }
    }

    public AttachPermission(String paramString1, String paramString2)
    {
        super(paramString1);
        if ((!paramString1.equals("attachVirtualMachine")) &&
                (!paramString1.equals("createAttachProvider"))) {
            throw new IllegalArgumentException("name: " + paramString1);
        }
        if ((paramString2 != null) && (paramString2.length() > 0)) {
            throw new IllegalArgumentException("actions: " + paramString2);
        }
    }

}
