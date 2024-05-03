package com.vrrom.admin.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.vrrom.admin.model.Admin;
import com.vrrom.admin.model.Admin.AdminRole;
import com.vrrom.admin.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminSyncService {
    private final FirebaseAuth firebaseAuth;
    private final AdminRepository adminRepository;

    @Autowired
    public AdminSyncService(FirebaseAuth firebaseAuth, AdminRepository adminRepository) {
        this.firebaseAuth = firebaseAuth;
        this.adminRepository = adminRepository;
    }

    @PostConstruct
    public void syncAdminsFromFirebase() throws FirebaseAuthException {
        ListUsersPage page = firebaseAuth.listUsers(null);
        while (page != null) {
            page.getValues().forEach(this::saveAdminFromFirebaseUser);
            page = page.getNextPage();
        }
    }

    private void saveAdminFromFirebaseUser(com.google.firebase.auth.UserRecord firebaseUser) {
        String uid = firebaseUser.getUid();
        String email = firebaseUser.getEmail();

        // Retrieve the name and surname from the user's custom claims
        String name = null;
        String surname = null;
        Map<String, Object> customClaims = firebaseUser.getCustomClaims();
        if (customClaims != null) {
            if (customClaims.containsKey("name")) {
                name = (String) customClaims.get("name");
            }
            if (customClaims.containsKey("surname")) {
                surname = (String) customClaims.get("surname");
            }
        }

        // Retrieve the role from the custom claims
        AdminRole role = null;
        if (customClaims != null && customClaims.containsKey("role")) {
            String roleString = (String) customClaims.get("role");
            role = convertToAdminRole(roleString);
        }

        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            admin = new Admin();
            admin.setUid(uid);
            admin.setEmail(email);
            admin.setName(name);
            admin.setSurname(surname);
            admin.setRole(role);
            adminRepository.save(admin);
        }
    }

    private AdminRole convertToAdminRole(String roleString) {
        if ("admin".equalsIgnoreCase(roleString)) {
            return AdminRole.ADMIN;
        } else if ("superAdmin".equalsIgnoreCase(roleString)) {
            return AdminRole.SUPER_ADMIN;
        } else {
            // Handle the case when the role string is not recognized
            throw new IllegalArgumentException("Invalid admin role: " + roleString);
        }
    }
}