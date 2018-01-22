package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */

import jdk.Exported;

@Exported
public class AttachNotSupportedException
        extends Exception
{
    static final long serialVersionUID = 3391824968260177264L;

    public AttachNotSupportedException() {}

    public AttachNotSupportedException(String paramString)
    {
        super(paramString);
    }
}
