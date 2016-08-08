package development.sai.podiocalendar.sdk;

import com.podio.sdk.Request;
import com.podio.sdk.provider.ClientProvider;
import com.podio.sdk.provider.ContactProvider;
import com.podio.sdk.provider.ConversationProvider;
import com.podio.sdk.provider.UserProvider;

/**
 * Created by sai on 8/1/16.
 */
public class SdkManager implements ISdkManager {

    private static SdkManager instance = null;

    private static SdkManager getInstance() {
        if(instance == null) {
            instance = new SdkManager();
        }
        return instance;
    }

    @Override
    public ContactProvider getContactProvider() {
        return SuperPodio.contact;
    }

    @Override
    public ClientProvider getClientProvider() {
        return SuperPodio.client;
    }

    @Override
    public ConversationProvider getConversationProvider() {
        return SuperPodio.conversation;
    }

    @Override
    public UserProvider getUserProvider() {
        return SuperPodio.user;
    }

    @Override
    public void registerGlobalErrorListener(Request.ErrorListener errorListener) {
        SuperPodio.addGlobalErrorListener(errorListener);
    }

    @Override
    public void unregisterGlobalErrorListener(Request.ErrorListener errorListener) {
        SuperPodio.removeGlobalErrorListener(errorListener);
    }
}
