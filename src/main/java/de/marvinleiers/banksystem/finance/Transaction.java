package de.marvinleiers.banksystem.finance;

import de.marvinleiers.banksystem.utils.TransactionType;

public class Transaction
{
    private final TransactionType type;
    private final double amount;
    private final String to;

    public Transaction(String to, TransactionType type, double amount)
    {
        this.type = type;
        this.to = to;
        this.amount = amount;
    }

    public String getTo()
    {
        return to;
    }

    public double getAmount()
    {
        return amount;
    }

    public TransactionType getType()
    {
        return type;
    }
}
