package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */
import jdk.Exported;

@Exported
public class AgentLoadException
        extends Exception
{
    static final long serialVersionUID = 688047862952114238L;

    public AgentLoadException() {}

    public AgentLoadException(String paramString)
    {
        super(paramString);
    }
}
