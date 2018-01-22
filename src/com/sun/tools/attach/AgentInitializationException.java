package com.sun.tools.attach;

/**
 * Created by Ethan on 1/22/2018.
 */
import jdk.Exported;

@Exported
public class AgentInitializationException
        extends Exception
{
    static final long serialVersionUID = -1508756333332806353L;
    private int returnValue;

    public AgentInitializationException()
    {
        this.returnValue = 0;
    }

    public AgentInitializationException(String paramString)
    {
        super(paramString);
        this.returnValue = 0;
    }

    public AgentInitializationException(String paramString, int paramInt)
    {
        super(paramString);
        this.returnValue = paramInt;
    }

    public int returnValue()
    {
        return this.returnValue;
    }
}
