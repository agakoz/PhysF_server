package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.TreatmentCycleAttachmentDTO;
import com.agakoz.physf.model.ExternalAttachment;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.model.UploadedFile;
import com.agakoz.physf.repositories.ExternalAttachmentRepository;
import com.agakoz.physf.repositories.FileRepository;
import com.agakoz.physf.security.FileService;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalAttachmentService {

    private final ExternalAttachmentRepository externalAttachmentRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;

    @Autowired
    public ExternalAttachmentService(ExternalAttachmentRepository externalAttachmentRepository, FileRepository fileRepository, FileService fileService) {
        this.externalAttachmentRepository = externalAttachmentRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    public void createOrUpdateAttachment(TreatmentCycleAttachmentDTO attachmentDetails, TreatmentCycle treatmentCycle) throws NoSuchFileException {
        ExternalAttachment attachment;
        if (attachmentDetails.getId() == -1) {
            attachment = new ExternalAttachment();
            attachment = ObjectMapperUtils.map(attachmentDetails, attachment);
            attachment.setId(getIdForNewAttachment());
            attachment.setTreatmentCycle(treatmentCycle);
        } else {
            Optional<ExternalAttachment> retrievedAttachment = externalAttachmentRepository.findById(attachmentDetails.getId());
            if (retrievedAttachment.isPresent()) {
                attachment = retrievedAttachment.get();
                attachment = ObjectMapperUtils.map(attachmentDetails, attachment);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (attachmentDetails.getFileId() > -1) {
            Optional<UploadedFile> uploadedFile = fileRepository.findById(attachmentDetails.getFileId());
            if (uploadedFile.isEmpty()) {
                throw new NoSuchFileException("");
            } else {
                attachment.setFile(uploadedFile.get());
            }
        } else {
            attachment.setFile(null);
        }

        externalAttachmentRepository.save(attachment);
    }

    private int getIdForNewAttachment() {
        Optional<Integer> idOpt = externalAttachmentRepository.getLastId();
        return idOpt.map(integer -> integer + 1).orElse(0);
    }

    public List<TreatmentCycleAttachmentDTO> getAttachmentsAssignedToTreatmentCycle(int treatmentCycleId) {
        List<TreatmentCycleAttachmentDTO> attachments = externalAttachmentRepository.findAttachmentsAssignedToTreatmentCycle(treatmentCycleId);
        attachments.forEach(attachment -> {
            if (attachment.getFileId() > -1)
                attachment.setFileName(fileService.getFileNameWithType(attachment.getFileId()));
            ;
        });
        return attachments;
    }

    public Optional<ExternalAttachment> getOptionalAttachment(int attachmentId) {
        return externalAttachmentRepository.findById(attachmentId);
    }

    public boolean deleteExternalAttachmentWithFile(ExternalAttachment attachmentToDelete) {
        UploadedFile fileToDelete = attachmentToDelete.getFile();
        this.externalAttachmentRepository.deleteById(attachmentToDelete.getId());
//        if (fileToDelete != null) {
////            fileService.deleteFile(fileToDelete);
//            fileRepository.deleteById(fileToDelete.getId());
//        }
        return true;
    }

    public List<Integer> getAttachmentIdsAssignedToTreatmentCycle(int treatmentCycleId) {
        return this.externalAttachmentRepository.findAllIdByTreatmentCycleId(treatmentCycleId);
    }

    public void deleteExternalAttachmentWithFile(Integer attachmentId) {
        Optional<ExternalAttachment> attachmentOpt = this.externalAttachmentRepository.findById(attachmentId);
        if (attachmentOpt.isPresent()) {
            deleteExternalAttachmentWithFile(attachmentOpt.get());
        }
    }

    public void updateAssignedAttachmentsDeleteOld(int treatmentCycleId, List<Integer> updatedAttachmentIds) {
        List<Integer> assignedAttachments = getAttachmentIdsAssignedToTreatmentCycle(treatmentCycleId);
        List<Integer> attachmentsToDelete = new ArrayList<>(assignedAttachments);
        attachmentsToDelete.removeAll(updatedAttachmentIds);
        attachmentsToDelete.forEach(this::deleteExternalAttachmentWithFile);
    }
}
