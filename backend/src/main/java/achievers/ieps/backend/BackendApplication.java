package achievers.ieps.backend;

import achievers.ieps.backend.dto.UserMapper;
import achievers.ieps.backend.dto.request.CreateUserRequestDTO;
import achievers.ieps.backend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.backend.model.*;
import achievers.ieps.backend.service.RoleService;
import achievers.ieps.backend.service.UserService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;
import java.util.Random;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run (UserService userService, RoleService roleService, UserMapper userMapper){
		return args -> {
			var faker = new Faker(new Locale("in-ID"));

			for (int i = 0; i < 1; i++){
				Random rand = new Random();
				var vendorDTO = new CreateVendorRequestDTO();
				vendorDTO.setAlamat(faker.address().fullAddress());
				vendorDTO.setRole("Vendor");
				vendorDTO.setPassword("Xx1234567");
				vendorDTO.setNama(faker.name().fullName());
				vendorDTO.setEmail(faker.internet().emailAddress());
				vendorDTO.setNamaPerusahaan(faker.company().name());
				vendorDTO.setNomorTelefon(faker.phoneNumber().phoneNumber());

				Vendor vendor = (Vendor) userMapper.createVendorRequestDTOToVendor(vendorDTO);
				userService.addUser(vendor, vendorDTO);

				var userDTO = new CreateUserRequestDTO();
				userDTO.setEmail(faker.internet().emailAddress());
				userDTO.setNama(faker.name().fullName());
				userDTO.setNomorTelefon(faker.phoneNumber().phoneNumber());
				int rand_int = rand.nextInt(3);
				if (rand_int == 0){
					userDTO.setRole("Admin");
					userDTO.setPassword("Xx1234567");
					Admin admin = new Admin();
					admin.setNama(userDTO.getNama());
					admin.setEmail(userDTO.getEmail());
					admin.setNomorTelefon(userDTO.getNomorTelefon());
					admin.setRole(roleService.getRoleByRoleName(userDTO.getRole()));
					admin.setPassword(userDTO.getPassword());
					userService.addUser(admin, userDTO);
				} else if (rand_int == 1){
					userDTO.setRole("Procurement Staff");
					userDTO.setPassword("Xx1234567");
					ProcStaff staf = new ProcStaff();
					staf.setNama(userDTO.getNama());
					staf.setEmail(userDTO.getEmail());
					staf.setNomorTelefon(userDTO.getNomorTelefon());
					staf.setRole(roleService.getRoleByRoleName(userDTO.getRole()));
					staf.setPassword(userDTO.getPassword());
					userService.addUser(staf, userDTO);
				} else {
					userDTO.setRole("Procurement Manager");
					userDTO.setPassword("Xx1234567");
					ProcManager manager = new ProcManager();
					manager.setNama(userDTO.getNama());
					manager.setEmail(userDTO.getEmail());
					manager.setNomorTelefon(userDTO.getNomorTelefon());
					manager.setRole(roleService.getRoleByRoleName(userDTO.getRole()));
					manager.setPassword(userDTO.getPassword());
					userService.addUser(manager, userDTO);
				}
			}
		};
	}

}
