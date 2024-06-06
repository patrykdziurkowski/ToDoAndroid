package com.example.todoandroid.repository;

import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.domain.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

public class AttachmentRepository {
    private List<Attachment> attachments;

    @Inject
    public AttachmentRepository() {
        attachments = new ArrayList<>();
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void save(Attachment attachment) {
        Optional<Attachment> matchingAttachment = attachments.stream()
                .filter((a) -> {
                    return a.getId() == attachment.getId();
                })
                .findFirst();
        if (!matchingAttachment.isPresent()) {
            attachments.add(attachment);
        }

        Attachment attachmentToDelete = attachments.stream()
                .filter((a) -> {
                    return a.getId() == attachment.getId();
                })
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        if (attachment.isMarkedForDeletion()) {
            attachments.remove(attachmentToDelete);
        }
    }
}
