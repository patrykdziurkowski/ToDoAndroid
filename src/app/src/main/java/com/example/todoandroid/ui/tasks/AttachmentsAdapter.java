package com.example.todoandroid.ui.tasks;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoandroid.databinding.FrameAttachmentBinding;
import com.example.todoandroid.domain.Attachment;

import java.util.List;

public class AttachmentsAdapter extends RecyclerView.Adapter<AttachmentsAdapter.ViewHolder> {
    private List<Attachment> taskAttachments;
    private AttachmentClickListener attachmentRemoveClickListener;

    public AttachmentsAdapter(List<Attachment> attachments) {
        this.taskAttachments = attachments;
    }

    @NonNull
    @Override
    public AttachmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameAttachmentBinding holderBinding = FrameAttachmentBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(holderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsAdapter.ViewHolder holder, int position) {
        FrameAttachmentBinding holderBinding = holder.getBinding();

        Uri uri = taskAttachments.get(position).getUri();
        holderBinding.attachmentImage.setImageURI(uri);
        holderBinding.attachmentRemove.setOnClickListener((view) -> {
            if (attachmentRemoveClickListener == null) return;
            attachmentRemoveClickListener.onClick(taskAttachments.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return taskAttachments.size();
    }

    public void setAttachmentRemoveClickListener(AttachmentClickListener attachmentRemoveClickListener) {
        this.attachmentRemoveClickListener = attachmentRemoveClickListener;
    }

    public List<Attachment> getTaskAttachments() {
        return taskAttachments;
    }

    public AttachmentsAdapter setTaskAttachments(List<Attachment> taskAttachments) {
        this.taskAttachments = taskAttachments;
        return this;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FrameAttachmentBinding binding;

        public ViewHolder(FrameAttachmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public FrameAttachmentBinding getBinding() {
            return binding;
        }
    }

    public interface AttachmentClickListener {
        void onClick(Attachment attachment);
    }
}
