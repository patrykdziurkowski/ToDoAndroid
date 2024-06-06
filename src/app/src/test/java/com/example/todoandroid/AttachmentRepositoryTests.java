package com.example.todoandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import android.net.Uri;

import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.repository.AttachmentRepository;

import org.junit.Test;

import java.util.UUID;

public class AttachmentRepositoryTests {
    @Test
    public void save_addsNewTask_whenItsUuidNotInRepository() {
        AttachmentRepository attachmentRepository = new AttachmentRepository();
        int numberOfAttachments = attachmentRepository.getAttachments().size();
        Attachment attachment = new Attachment(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Uri.EMPTY
        );

        attachmentRepository.save(attachment);

        int updatedNumberOfAttachments = attachmentRepository.getAttachments().size();
        assertEquals(numberOfAttachments + 1, updatedNumberOfAttachments);
    }

    @Test
    public void save_doesntAddNewAttachment_whenItsUuidAlreadyInRepository() {
        AttachmentRepository attachmentRepository = new AttachmentRepository();
        int numberOfAttachments = attachmentRepository.getAttachments().size();
        Attachment attachment = new Attachment(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Uri.EMPTY
        );

        attachmentRepository.save(attachment);
        attachmentRepository.save(attachment);

        int updatedNumberOfAttachments = attachmentRepository.getAttachments().size();
        assertEquals(numberOfAttachments + 1, updatedNumberOfAttachments);
    }

    @Test
    public void save_removesAttachment_thatsMarkedForDeletion() {
        AttachmentRepository attachmentRepository = new AttachmentRepository();
        UUID attachmentId = UUID.randomUUID();
        Attachment attachment = new Attachment(
                attachmentId,
                UUID.randomUUID(),
                Uri.EMPTY
        );
        attachment.setMarkedForDeletion(true);

        attachmentRepository.save(attachment);

        assertTrue(attachmentRepository.getAttachments().stream()
                .noneMatch((a) -> a.getId() == attachmentId));
    }
}
