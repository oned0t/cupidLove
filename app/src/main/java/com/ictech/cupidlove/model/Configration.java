package com.ictech.cupidlove.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankita on 5/2/2018.
 */

public class Configration {
    @SerializedName("GOOGLE_PLACE_API_KEY")
    @Expose
    public String gOOGLEPLACEAPIKEY;
    @SerializedName("FACEBOOK_KEY")
    @Expose
    public String fACEBOOKKEY;
    @SerializedName("XMPP_ENABLE")
    @Expose
    public String xMPPENABLE;
    @SerializedName("APP_XMPP_SERVER")
    @Expose
    public String aPPXMPPSERVER;
    @SerializedName("APP_XMPP_HOST")
    @Expose
    public String aPPXMPPHOST;
    @SerializedName("XMPP_DEFAULT_PASSWORD")
    @Expose
    public String xMPPDEFAULTPASSWORD;
    @SerializedName("PUSH_ENABLE_SANDBOX")
    @Expose
    public String pUSHENABLESANDBOX;
    @SerializedName("PUSH_SANDBOX_GATEWAY_URL")
    @Expose
    public String pUSHSANDBOXGATEWAYURL;
    @SerializedName("PUSH_GATEWAY_URL")
    @Expose
    public String pUSHGATEWAYURL;
    @SerializedName("ANDROID_FCM_KEY")
    @Expose
    public String aNDROIDFCMKEY;
    @SerializedName("INSTAGRAM_CALLBACK_BASE")
    @Expose
    public String iNSTAGRAMCALLBACKBASE;
    @SerializedName("INSTAGRAM_CLIENT_SECRET")
    @Expose
    public String iNSTAGRAMCLIENTSECRET;
    @SerializedName("INSTAGRAM_CLIENT_ID")
    @Expose
    public String iNSTAGRAMCLIENTID;
    @SerializedName("adMobKey")
    @Expose
    public String adMobKey;
    @SerializedName("adMobVideoKey")
    @Expose
    public String adMobVideoKey;
    @SerializedName("RemoveAddInAppPurchase")
    @Expose
    public String removeAddInAppPurchase;
    @SerializedName("RemoveAddInAppBilling")
    @Expose
    public String removeAddInAppBilling;
    @SerializedName("PaidChatInAppBilling")
    @Expose
    public String paidChatInAppBilling;
    @SerializedName("LocationInAppBilling")
    @Expose
    public String locationInAppBilling;
    @SerializedName("SuperLikeInAppBilling")
    @Expose
    public String superLikeInAppBilling;
    @SerializedName("PaidChatInAppPurchase")
    @Expose
    public String paidChatInAppPurchase;
    @SerializedName("LocationInAppPurchase")
    @Expose
    public String locationInAppPurchase;
    @SerializedName("SuperLikeInAppPurchase")
    @Expose
    public String superLikeInAppPurchase;
    @SerializedName("TermsAndConditionsUrl")
    @Expose
    public String termsAndConditionsUrl;
    @SerializedName("PAID_CHAT")
    @Expose
    public String pAIDCHAT;
    @SerializedName("PAID_LOCATION")
    @Expose
    public String pAIDLOCATION;
    @SerializedName("PAID_SUPERLIKE")
    @Expose
    public String pAIDSUPERLIKE;
    @SerializedName("PER_DAY_SUPERLIKE")
    @Expose
    public String pERDAYSUPERLIKE;
    @SerializedName("PAID_AD")
    @Expose
    public String pAIDAD;

    public Configration withGOOGLEPLACEAPIKEY(String gOOGLEPLACEAPIKEY) {
        this.gOOGLEPLACEAPIKEY = gOOGLEPLACEAPIKEY;
        return this;
    }

    public Configration withFACEBOOKKEY(String fACEBOOKKEY) {
        this.fACEBOOKKEY = fACEBOOKKEY;
        return this;
    }

    public Configration withXMPPENABLE(String xMPPENABLE) {
        this.xMPPENABLE = xMPPENABLE;
        return this;
    }

    public Configration withAPPXMPPSERVER(String aPPXMPPSERVER) {
        this.aPPXMPPSERVER = aPPXMPPSERVER;
        return this;
    }

    public Configration withAPPXMPPHOST(String aPPXMPPHOST) {
        this.aPPXMPPHOST = aPPXMPPHOST;
        return this;
    }

    public Configration withXMPPDEFAULTPASSWORD(String xMPPDEFAULTPASSWORD) {
        this.xMPPDEFAULTPASSWORD = xMPPDEFAULTPASSWORD;
        return this;
    }

    public Configration withPUSHENABLESANDBOX(String pUSHENABLESANDBOX) {
        this.pUSHENABLESANDBOX = pUSHENABLESANDBOX;
        return this;
    }

    public Configration withPUSHSANDBOXGATEWAYURL(String pUSHSANDBOXGATEWAYURL) {
        this.pUSHSANDBOXGATEWAYURL = pUSHSANDBOXGATEWAYURL;
        return this;
    }

    public Configration withPUSHGATEWAYURL(String pUSHGATEWAYURL) {
        this.pUSHGATEWAYURL = pUSHGATEWAYURL;
        return this;
    }

    public Configration withANDROIDFCMKEY(String aNDROIDFCMKEY) {
        this.aNDROIDFCMKEY = aNDROIDFCMKEY;
        return this;
    }

    public Configration withINSTAGRAMCALLBACKBASE(String iNSTAGRAMCALLBACKBASE) {
        this.iNSTAGRAMCALLBACKBASE = iNSTAGRAMCALLBACKBASE;
        return this;
    }

    public Configration withINSTAGRAMCLIENTSECRET(String iNSTAGRAMCLIENTSECRET) {
        this.iNSTAGRAMCLIENTSECRET = iNSTAGRAMCLIENTSECRET;
        return this;
    }

    public Configration withINSTAGRAMCLIENTID(String iNSTAGRAMCLIENTID) {
        this.iNSTAGRAMCLIENTID = iNSTAGRAMCLIENTID;
        return this;
    }

    public Configration withAdMobKey(String adMobKey) {
        this.adMobKey = adMobKey;
        return this;
    }

    public Configration withAdMobVideoKey(String adMobVideoKey) {
        this.adMobVideoKey = adMobVideoKey;
        return this;
    }

    public Configration withRemoveAddInAppPurchase(String removeAddInAppPurchase) {
        this.removeAddInAppPurchase = removeAddInAppPurchase;
        return this;
    }

    public Configration withRemoveAddInAppBilling(String removeAddInAppBilling) {
        this.removeAddInAppBilling = removeAddInAppBilling;
        return this;
    }

    public Configration withPaidChatInAppBilling(String paidChatInAppBilling) {
        this.paidChatInAppBilling = paidChatInAppBilling;
        return this;
    }

    public Configration withLocationInAppBilling(String locationInAppBilling) {
        this.locationInAppBilling = locationInAppBilling;
        return this;
    }

    public Configration withSuperLikeInAppBilling(String superLikeInAppBilling) {
        this.superLikeInAppBilling = superLikeInAppBilling;
        return this;
    }

    public Configration withPaidChatInAppPurchase(String paidChatInAppPurchase) {
        this.paidChatInAppPurchase = paidChatInAppPurchase;
        return this;
    }

    public Configration withLocationInAppPurchase(String locationInAppPurchase) {
        this.locationInAppPurchase = locationInAppPurchase;
        return this;
    }

    public Configration withSuperLikeInAppPurchase(String superLikeInAppPurchase) {
        this.superLikeInAppPurchase = superLikeInAppPurchase;
        return this;
    }

    public Configration withTermsAndConditionsUrl(String termsAndConditionsUrl) {
        this.termsAndConditionsUrl = termsAndConditionsUrl;
        return this;
    }

    public Configration withPAIDCHAT(String pAIDCHAT) {
        this.pAIDCHAT = pAIDCHAT;
        return this;
    }

    public Configration withPAIDLOCATION(String pAIDLOCATION) {
        this.pAIDLOCATION = pAIDLOCATION;
        return this;
    }

    public Configration withPAIDSUPERLIKE(String pAIDSUPERLIKE) {
        this.pAIDSUPERLIKE = pAIDSUPERLIKE;
        return this;
    }

    public Configration withPERDAYSUPERLIKE(String pERDAYSUPERLIKE) {
        this.pERDAYSUPERLIKE = pERDAYSUPERLIKE;
        return this;
    }

    public Configration withPAIDAD(String pAIDAD) {
        this.pAIDAD = pAIDAD;
        return this;
    }
}
