package achievers.ieps.frontend.setting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Setting {
    @Value("${achievers.admin.url}")
    public String ADMIN_SERVER_URL;
}
