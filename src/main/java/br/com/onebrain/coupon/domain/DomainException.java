package br.com.onebrain.coupon.domain;

import java.util.Arrays;

public class DomainException extends RuntimeException
{
    private final String messageKey;
    private final Object[] args;

    public DomainException(String messageKey, Object... args)
    {
        super(messageKey);

        this.messageKey = messageKey;
        this.args = args == null ? new Object[0] : Arrays.copyOf(args, args.length);
    }

    public String getMessageKey()
    {
        return messageKey;
    }

    public Object[] getArgs()
    {
        return Arrays.copyOf(args, args.length);
    }
}
