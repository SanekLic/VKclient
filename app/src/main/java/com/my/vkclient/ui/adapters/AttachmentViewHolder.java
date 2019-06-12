package com.my.vkclient.ui.adapters;

import android.view.View;

import com.my.vkclient.entities.Attachment;

class AttachmentViewHolder extends BaseAttachmentViewHolder<Attachment> {

    AttachmentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Attachment data) {
        super.bindAttachment(data);
    }
}
