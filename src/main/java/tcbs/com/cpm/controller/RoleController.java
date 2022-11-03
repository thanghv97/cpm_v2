package tcbs.com.cpm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcbs.com.cpm.dto.ResponseDTO;
import tcbs.com.cpm.dto.request.RoleReq;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.error.RestApiException;
import tcbs.com.cpm.repository.RoleRepository;
import tcbs.com.cpm.service.BPMService;
import tcbs.com.cpm.service.WSO2Service;
import tcbs.com.cpm.util.BeanUtils;
import tcbs.com.cpm.util.Constants;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {
  @Autowired
  private RoleRepository rRepo;

  @Autowired
  private BPMService bpmService;

  @Autowired
  private WSO2Service wso2Service;

  @PostMapping
  public ResponseEntity<Role> create(@RequestBody RoleReq rReq) {
    Role r = validate(null);
    BeanUtils.copyPropertiesIgnoreNull(rReq, r);

    if (rReq.getSystem() != null) {
      List<String> systems = rReq.getSystem();
      for (String s : systems) {
        if (Constants.SYSTEM_BPM.equals(s)) {
          bpmService.addRole(rReq.getName(), rReq.getCfgBpm());
        } else if (Constants.SYSTEM_WSO2.equals(s)) {
          wso2Service.addRole(rReq.getName(), rReq.getCfgWso2());
        } else {
          log.info("Unsupported system {}", s);
        }
      }
      r.setSystem(String.join(",", rReq.getSystem()));
    }
    return ResponseEntity.ok(rRepo.save(r));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseDTO> update(@PathVariable int id, @RequestBody RoleReq rReq) {
    Role r = validate(id);
    BeanUtils.copyPropertiesIgnoreNull(rReq, r);

    if (rReq.getSystem() != null) {
      r.setSystem(String.join(",", rReq.getSystem()));
    }
    return ResponseEntity.ok(new ResponseDTO(Constants.CODE_DEFAULT, "Function is currently not supported", Instant.now()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
    Role r = validate(id);
    rRepo.delete(r);
    return ResponseEntity.ok(new ResponseDTO(Constants.CODE_DEFAULT, Constants.STATE_SUCCESS, Instant.now()));
  }

  @GetMapping()
  public ResponseEntity<List<Role>> get() {
    return ResponseEntity.ok(rRepo.findAll());
  }

  private Role validate(Integer id) {
    // check id is exists
    if (id != null) {
      Optional<Role> optR = rRepo.findById(id);
      if (!optR.isPresent()) {
        throw new RestApiException(Constants.CODE_DEFAULT, String.format("The Role with 'id'[%d] not found!!!", id));
      }
      return optR.get();
    }
    return new Role();
  }
}
