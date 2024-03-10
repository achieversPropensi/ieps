package achievers.ieps.backend.service;

import achievers.ieps.backend.model.Vendor;

public interface VendorService {
    Vendor getVendorByEmail(String email);
    void toggleSubmission(String email);
}
