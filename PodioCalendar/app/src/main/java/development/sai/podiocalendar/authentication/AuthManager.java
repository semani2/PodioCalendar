package development.sai.podiocalendar.authentication;

import android.content.Context;

import com.podio.sdk.Podio;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.sdk.GlobalRequestListener;

/**
 * Created by sai on 8/1/16.
 */
public class AuthManager implements IAuthManager {

    private final EventBus eventBus;
    private final IAccountManager accountManager;
    private final GlobalRequestListener globalRequestListener;

    private AuthManager(Context context) {
        eventBus = EventBus.getDefault();
        accountManager = PodioAccountManager.getInstance(context);
        globalRequestListener = GlobalRequestListener.getInstance(context);
    }

    private static AuthManager authManager = null;

    public static AuthManager getInstance(Context context) {
        if(authManager == null) {
            authManager = new AuthManager(context);
        }
        return authManager;
    }
    @Override
    public void authenticateWithTransferToken() {
        eventBus.post(new ProgressBarEvent(true));

        Podio.client.authenticateWithTransferToken(accountManager.readPodioAccountTransferToken())
                .withSessionListener(globalRequestListener)
                .withErrorListener(globalRequestListener);
    }

    @Override
    public void authenticateWithCredentials(String email, String password) {
        eventBus.post(new ProgressBarEvent(true));

        Podio.client.authenticateWithUserCredentials(email, password)
                .withSessionListener(globalRequestListener)
                .withErrorListener(globalRequestListener);
    }
}
