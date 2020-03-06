package api.requestLibary.CORE;

public class coreReserveAndTransactV3POJO {

    private String clientId;

    private String sourceIdentifier;

    private String productId;

    private String accountIdentifier;

    private String purchaseAmount;

    private String feeAmount;

    private String targetIdentifier;

    private String clientTxnRef;

    private String channelSessionId;

    private String channelName;

    private String channelId;

    private String timestamp;

    public coreReserveAndTransactV3POJO(){

    }

    public coreReserveAndTransactV3POJO(String accountIdentifier, String purchaseAmount, String channelId, String channelName, String channelSessionId, String clientId, String clientTxnRef, String productId, String sourceIdentifier, String targetIdentifier, String timestamp, String feeAmount){

        this.accountIdentifier = accountIdentifier;
        this.purchaseAmount = purchaseAmount;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelSessionId = channelSessionId;
        this.clientId = clientId;
        this.clientTxnRef = clientTxnRef;
        this.productId = productId;
        this.sourceIdentifier = sourceIdentifier;
        this.targetIdentifier = targetIdentifier;
        this.timestamp = timestamp;
        this.feeAmount = feeAmount;

    }

    public String getClientId ()
    {
        return clientId;
    }

    public void setClientId (String clientId)
    {
        this.clientId = clientId;
    }

    public String getSourceIdentifier ()
    {
        return sourceIdentifier;
    }

    public void setSourceIdentifier (String sourceIdentifier)
    {
        this.sourceIdentifier = sourceIdentifier;
    }

    public String getProductId ()
    {
        return productId;
    }

    public void setProductId (String productId)
    {
        this.productId = productId;
    }

    public String getAccountIdentifier ()
    {
        return accountIdentifier;
    }

    public void setAccountIdentifier (String accountIdentifier)
    {
        this.accountIdentifier = accountIdentifier;
    }

    public String getPurchaseAmount ()
    {
        return purchaseAmount;
    }

    public void setPurchaseAmount (String purchaseAmount)
    {
        this.purchaseAmount = purchaseAmount;
    }

    public String getFeeAmount ()
    {
        return feeAmount;
    }

    public void setFeeAmount (String feeAmount)
    {
        this.feeAmount = feeAmount;
    }

    public String getTargetIdentifier ()
    {
        return targetIdentifier;
    }

    public void setTargetIdentifier (String targetIdentifier)
    {
        this.targetIdentifier = targetIdentifier;
    }

    public String getClientTxnRef ()
    {
        return clientTxnRef;
    }

    public void setClientTxnRef (String clientTxnRef)
    {
        this.clientTxnRef = clientTxnRef;
    }

    public String getChannelSessionId ()
    {
        return channelSessionId;
    }

    public void setChannelSessionId (String channelSessionId)
    {
        this.channelSessionId = channelSessionId;
    }

    public String getChannelName ()
    {
        return channelName;
    }

    public void setChannelName (String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelId ()
    {
        return channelId;
    }

    public void setChannelId (String channelId)
    {
        this.channelId = channelId;
    }

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [accountIdentifier = "+accountIdentifier+", purchaseAmount = "+purchaseAmount+", channelId = "+channelId+", channelName = "+channelName+", channelSessionId = "+channelSessionId+", clientId = "+clientId+", clientTxnRef = "+clientTxnRef+", productId = "+productId+", sourceIdentifier = "+sourceIdentifier+", targetIdentifier = "+targetIdentifier+", timestamp = "+timestamp+", feeAmount = "+feeAmount+"]";
    }

}
