package achievers.ieps.backend.service;

import achievers.ieps.backend.model.Vendor;
import achievers.ieps.backend.repository.VendorDb;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {
    @Autowired
    VendorDb vendorDb;

    @Override
    public Vendor getVendorByEmail(String email){
        return vendorDb.findByEmail(email);
    };

    @Override
    public void toggleSubmission(String email) {
        Vendor vendor = vendorDb.findByEmail(email);
        if (vendor != null) {
            vendor.setHasSubmitted(true);
            vendorDb.save(vendor);
        }
    }
}
