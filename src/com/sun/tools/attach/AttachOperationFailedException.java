package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */
import java.io.IOException;
import jdk.Exported;

@Exported
public class AttachOperationFailedException
        extends IOException
{
    private static final long serialVersionUID = 2140308168167478043L;

    public AttachOperationFailedException(String paramString)
    {
        super(paramString);
    }
}
