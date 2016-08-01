package development.sai.podiocalendar.sdk;

import com.podio.sdk.Request;
import com.podio.sdk.provider.ClientProvider;
import com.podio.sdk.provider.ContactProvider;
import com.podio.sdk.provider.ConversationProvider;
import com.podio.sdk.provider.UserProvider;

/**
 * Created by sai on 8/1/16.
 */
public interface ISdkManager {
    ContactProvider getContactProvider();

    ClientProvider getClientProvider();

    ConversationProvider getConversationProvider();

    UserProvider getUserProvider();

    void registerGlobalErrorListener(Request.ErrorListener errorListener);

    void unregisterGlobalErrorListener(Request.ErrorListener errorListener);
}
