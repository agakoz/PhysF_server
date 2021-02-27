package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.ExternalAttachmentDTO;
import com.agakoz.physf.model.DTO.VisitAttachmentDTO;
import com.agakoz.physf.model.UploadedFile;
import com.agakoz.physf.model.Visit;
import com.agakoz.physf.model.VisitAttachment;
import com.agakoz.physf.repositories.FileRepository;
import com.agakoz.physf.repositories.VisitAttachmentRepository;
import com.agakoz.physf.repositories.VisitRepository;
import com.agakoz.physf.security.FileService;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisitAttachmentService {

    private final VisitAttachmentRepository visitAttachmentRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final VisitRepository visitRepository;

    @Autowired
    public VisitAttachmentService(
            VisitAttachmentRepository visitAttachmentRepository,
            FileRepository fileRepository,
            FileService fileService,
            VisitRepository visitRepository) {
        this.visitAttachmentRepository = visitAttachmentRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
        this.visitRepository = visitRepository;
    }

    public void createOrUpdateAttachment(VisitAttachmentDTO attachmentDetails, int visitId) throws NoSuchFileException {
        VisitAttachment attachment;
        Optional<Visit> visitOpt = visitRepository.findById(visitId);
        if (visitOpt.isEmpty()) {
            throw new InternalError();
        }

        Visit visit = visitOpt.get();
        if (attachmentDetails.getId() == -1) {
            attachment = new VisitAttachment();
            attachment = ObjectMapperUtils.map(attachmentDetails, attachment);
            attachment.setId(getIdForNewAttachment());
            attachment.setVisit(visit);
        } else {
            Optional<VisitAttachment> retrievedAttachment = visitAttachmentRepository.findById(attachmentDetails.getId());
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
        visitAttachmentRepository.save(attachment);
    }

    private int getIdForNewAttachment() {
        Optional<Integer> idOpt = visitAttachmentRepository.getLastId();
        return idOpt.map(integer -> integer + 1).orElse(0);
    }

    public List<VisitAttachmentDTO> getAttachmentsAssignedToVisit(int visitId) {
        return visitAttachmentRepository.findAttachmentsAssignedToVisit(visitId);
    }

    public Optional<VisitAttachment> getOptionalAttachment(int attachmentId) {
        return visitAttachmentRepository.findById(attachmentId);
    }

    public boolean deleteExternalAttachmentWithFile(VisitAttachment attachmentToDelete) {
        UploadedFile fileToDelete = attachmentToDelete.getFile();
        this.visitAttachmentRepository.deleteById(attachmentToDelete.getId());
        return true;
    }


    public void deleteExternalAttachmentWithFile(Integer attachmentId) {
        Optional<VisitAttachment> attachmentOpt = this.visitAttachmentRepository.findById(attachmentId);
        if (attachmentOpt.isPresent()) {
            deleteExternalAttachmentWithFile(attachmentOpt.get());
        }
    }
}
