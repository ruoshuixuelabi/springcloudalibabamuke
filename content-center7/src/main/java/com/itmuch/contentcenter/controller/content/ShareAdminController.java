//package com.itmuch.contentcenter.controller.content;
//
//import com.itmuch.contentcenter.domain.dto.content.ShareAuditDTO;
//import com.itmuch.contentcenter.domain.entity.content.Share;
//import com.itmuch.contentcenter.service.content.ShareService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/admin/shares")
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//public class ShareAdminController {
//    private final ShareService shareService;
//
//    @PutMapping("/audit/{id}")
//    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO) {
//        return this.shareService.auditById(id, auditDTO);
//    }
//
//}
