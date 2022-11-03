package tcbs.com.cpm.controller;

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
import tcbs.com.cpm.dto.request.GroupReq;
import tcbs.com.cpm.entity.Department;
import tcbs.com.cpm.entity.Group;
import tcbs.com.cpm.error.RestApiException;
import tcbs.com.cpm.repository.DepartmentRepository;
import tcbs.com.cpm.repository.GroupRepository;
import tcbs.com.cpm.util.Constants;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("group")
public class GroupController {

  @Autowired
  private GroupRepository gRepo;

  @Autowired
  private DepartmentRepository dRepo;

  @PostMapping
  public ResponseEntity<Group> create(@RequestBody GroupReq gReq) {
    Optional<Department> optD = dRepo.findById(gReq.getDepartmentId());
    if (!optD.isPresent()) {
      throw new RestApiException(Constants.CODE_DEFAULT, "The Department not found!!!");
    }

    Group g = new Group();
    g.setDepartmentId(optD.get().getId());
    g.setName(gReq.getName());
    return ResponseEntity.ok(gRepo.save(g));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Group> update(@PathVariable int id, @RequestBody GroupReq gReq) {
    Optional<Department> optD = dRepo.findById(gReq.getDepartmentId());
    if (!optD.isPresent()) {
      throw new RestApiException(Constants.CODE_DEFAULT, "The Department not found!!!");
    }

    Optional<Group> optG = gRepo.findById(id);
    if (!optG.isPresent()) {
      throw new RestApiException(Constants.CODE_DEFAULT, "The Group not found!!!");
    }

    Group g = optG.get();
    g.setDepartmentId(optD.get().getId());
    g.setName(gReq.getName());
    return ResponseEntity.ok(gRepo.save(g));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
    Optional<Group> optG = gRepo.findById(id);
    if (!optG.isPresent()) {
      throw new RestApiException(Constants.CODE_DEFAULT, "The Group not found!!!");
    }

    Group g = optG.get();
    gRepo.delete(g);
    return ResponseEntity.ok(new ResponseDTO("", Constants.STATE_SUCCESS, Instant.now()));
  }

  @GetMapping
  public ResponseEntity<List<Group>> get() {
    return ResponseEntity.ok(gRepo.findAll());
  }

}
