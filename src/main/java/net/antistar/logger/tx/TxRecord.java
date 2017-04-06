package net.antistar.logger.tx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author : syfer
 * @version 0.1
 * @since 12. 7. 5.
 */
public class TxRecord {
    public static final int SUCCEED = 1;
    public static final int ERROR = 2;

    private static int sequence = -1;
    private static final String dateFormat = "yyyyMMddHHmmssSSS"; //default
    private static final String LOG_KIND = "ENG";
    private static final Object seqLock = new Object();

    private String seqID = "X";
    private String hostName = "X";
    private String reqTime = "X";
    private String sessionKey = "X";
    private String fileID = "X";

    private int status = 2;
    private String remoteAddress = "X";
    private String requestUri = "X";
    private String requestMethod = "X";
    private int httpResponseCode = -1;
    private int internalCode = -1;

    private String apiCategory = "X";
    private String fileType = "X";
    private long fileSize = -1;
    private String deviceID = "X";
    private String platform = "X";
    private String platformVer = "X";
    private String productType = "X";
    private String productCode = "X";

    private String userId = "X";
    private String appName = "X";
    private String appVer = "X";
    private String deviceModel = "X";
    private String pcClientKind = "X";
    private String userExternalId = "X";
    
    // BEGIN shk.20140221
    private String itemName = "X"; // DIVIDE26
    private String parentId = "X"; // DIVIDE27
    private String newItemId = "X"; // DIVIDE28
    private String newItemName = "X"; // DIVIDE29
    private String newParentId = "X"; // DIVIDE30
    // END shk.20140221

    public TxRecord(String apiCategory) {
        this.apiCategory = apiCategory;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        String dateString = sdf.format(Calendar.getInstance().getTime());
        String rand = String.format("%02d",(int)(Math.random() * 100));
        synchronized (seqLock) {
            sequence = (sequence + 1)%10000;
            this.seqID = dateString + rand + sequence;
        }
        reqTime = dateString;
    }

    public String getMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        String logTime = sdf.format(Calendar.getInstance().getTime());
        StringBuilder message = new StringBuilder("SEQ_ID="+ seqID);
        message.append("|").append("HOST_NAME=").append(hostName).append("|").
                append("LOG_TIME=").append(logTime).append("|").
                append("REQ_TIME=").append(reqTime).append("|").
                append("LOG_KIND=").append(LOG_KIND).append("|").
                append("KT_USER_ID=").append(userExternalId).append("|").
                append("KT_SVC_ID=").append(userId).append("|").
                append("SESSION_KEY=").append(sessionKey == null ? "X" : sessionKey).append("|").
                append("FILE_ID=").append(fileID == null ? "X" : fileID).append("|").
                append("RT_CODE=").append(status).append("|").
                append("DIVIDE1=").append(remoteAddress == null ? "X" : remoteAddress).append("|").
                append("DIVIDE2=").append(requestUri == null ? "X" : requestUri).append("|").
                append("DIVIDE3=").append(requestMethod == null ? "X" : requestMethod).append("|").
                append("DIVIDE4=").append(httpResponseCode == -1 ? "X" : httpResponseCode).append("|").
                append("DIVIDE5=").append(internalCode == -1 ? "X" : String.format("%04d", internalCode)).append("|").
                append("DIVIDE6=").append(platform).append("|").
                append("DIVIDE7=").append(platformVer).append("|").
                append("DIVIDE8=").append(appName).append("|").
                append("DIVIDE9=").append(appVer).append("|").
                append("DIVIDE10=").append(deviceModel).append("|").
                append("DIVIDE11=").append(TxLogger.getAPIName()).append("|").
                append("DIVIDE12=").append(apiCategory).append("|").
                append("DIVIDE13=X").append("|").
                append("DIVIDE14=").append(fileSize == -1 ? "X" : fileSize).append("|").
                append("DIVIDE15=").append(fileType ==null?"X":fileType).append("|").
                append("DIVIDE16=").append(productType).append("|").
                append("DIVIDE17=").append(productCode).append("|").
                append("DIVIDE18=").append(pcClientKind).append("|").
                append("DIVIDE19=X").append("|").
                append("DIVIDE20=X").append("|").
                append("DIVIDE21=X").append("|").
                append("DIVIDE22=X").append("|").
                append("DIVIDE23=X").append("|").
                append("DIVIDE24=X").append("|").
                append("DIVIDE25=").append(deviceID).append("|").                
                // BEGIN shk.20140221
                append("DIVIDE26=").append(itemName).append("|").
                append("DIVIDE27=").append(parentId).append("|").
                append("DIVIDE28=").append(newItemId).append("|").
                append("DIVIDE29=").append(newItemName).append("|").
                append("DIVIDE30=").append(newParentId);
                // END shk.20140221

        return message.toString();
    }

    public void setStatus(int status, int httpRespCode, int internalCode) {
        this.status = status;
        this.httpResponseCode = httpRespCode;
        this.internalCode = internalCode;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserExternalId(String userExternalId) {
            this.userExternalId = userExternalId;
        }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPlatformVer(String platformVer) {
        this.platformVer = platformVer;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }


    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setPcClientKind(String pcClientKind) {
        this.pcClientKind = pcClientKind;
    }
    
    // BEGIN shk.20140221
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
        
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public void setNewItemId(String newItemId) {
        this.newItemId = newItemId;
    }
    
    public void setNewItemName(String newItemName) {
        this.newItemName = newItemName;
    }
    
    public void setNewParentId(String newParentId) {
        this.newParentId = newParentId;
    }
    // END shk.20140221
    
}
