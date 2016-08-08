package development.sai.podiocalendar.authentication;

/**
 * Created by sai on 8/1/16.
 */
public interface IAuthManager {

    void authenticateWithTransferToken();

    void authenticateWithCredentials(String email, String password);
}
