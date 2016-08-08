package development.sai.podiocalendar.account;

import android.content.Context;
import android.os.Bundle;

import com.podio.sdk.Request;

/**
 * Created by sai on 8/1/16.
 */
public interface IAccountManager {

    public boolean hasSession();

    public void clearSession();

    public Bundle readPodioAccountInfo();

    public String readPodioAccountTransferToken();

    public void addSession(String accessToke, String refreshToken, String transferToken, long expires);

    public String readSdkAccessToken();

    public String readSdkRefreshToken();

    public String readSdkTransferToken();

    public long readSdkTokenExpires();

    public void signOut();

    public void resetPodioSdk(Context context, Request.SessionListener globalSessionListener,
                              Request.ErrorListener globalErrorListener);
}
